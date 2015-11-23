package com.widetns.webapp.framework.mybatis;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RefreshSqlSessionFactoryBean extends SqlSessionFactoryBean {

    private static final Logger llog = LoggerFactory.getLogger(RefreshSqlSessionFactoryBean.class);
    private final Map<Resource, Long> map = new HashMap<Resource, Long>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
    private SqlSessionFactory proxy;
    private Resource configLocation;
    private Resource[] mapperLocations;
    private Properties configurationProperties;

    public void setConfigurationProperties(Properties sqlSessionFactoryProperties) {
        super.setConfigurationProperties(sqlSessionFactoryProperties);
        this.configurationProperties = sqlSessionFactoryProperties;
    }

    public void setConfigLocation(Resource configLocation) {
        super.setConfigLocation(configLocation);
        this.configLocation = configLocation;
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        super.setMapperLocations(mapperLocations);
        this.mapperLocations = mapperLocations;
    }

    private void refresh() throws Exception {
        if (llog.isInfoEnabled()) {
            llog.info("refreshing SqlSessionFactory.");
        }
        w.lock();
        try {
            super.afterPropertiesSet();
        } finally {
            w.unlock();
        }
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        setRefreshable();
    }

    private void setRefreshable() {
        proxy = (SqlSessionFactory) Proxy.newProxyInstance(
                SqlSessionFactory.class.getClassLoader(),
                new Class[]{SqlSessionFactory.class},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (isModified()) {
                            try {
                                refresh();
                            } catch (Exception e) {
                                llog.error("caught exception", e);
                            }
                        }
                        return method.invoke(getParentObject(), args);
                    }
                });
    }

    private Object getParentObject() throws Exception {
        r.lock();
        try {
            return super.getObject();
        } finally {
            r.unlock();
        }
    }

    public SqlSessionFactory getObject() {
        return this.proxy;
    }

    public Class<? extends SqlSessionFactory> getObjectType() {
        return (this.proxy != null ? this.proxy.getClass() : SqlSessionFactory.class);
    }

    public boolean isSingleton() {
        return true;
    }

    private boolean isModified() {
        boolean retVal = false;
        if (mapperLocations != null) {
            for (Resource mappingLocation : mapperLocations) {
                retVal = findModifiedResource(mappingLocation);
                if (retVal) {
                    break;
                }
            }
        } else if (configLocation != null) {
            Configuration configuration = null;
            XMLConfigBuilder xmlConfigBuilder = null;
            try {
                xmlConfigBuilder = new XMLConfigBuilder(configLocation.getInputStream(), null, configurationProperties);
                configuration = xmlConfigBuilder.getConfiguration();
            } catch (IOException e) {
                llog.error("error", e);
            }

            if (xmlConfigBuilder != null) {
                try {
                    xmlConfigBuilder.parse();
                    // Configuration Ŭ������ protected member field �� loadedResources �� ��� ���� reflection �� �����.
                    Field loadedResourcesField = Configuration.class.getDeclaredField("loadedResources");
                    loadedResourcesField.setAccessible(true);

                    @SuppressWarnings("unchecked")
                    Set<String> loadedResources = (Set<String>) loadedResourcesField.get(configuration);
                    for (String resourceStr : loadedResources) {
                        if (resourceStr.endsWith(".xml")) {
                            Resource mappingLocation = new ClassPathResource(resourceStr);
                            retVal = findModifiedResource(mappingLocation);
                            if (retVal) {
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    throw new RuntimeException("Failed to parse config resource: " + configLocation, ex);
                } finally {
                    ErrorContext.instance().reset();
                }
            }
        }
        return retVal;
    }

    private boolean findModifiedResource(Resource resource) {
        boolean retVal = false;
        List<String> modifiedResources = new ArrayList<String>();
        try {
            long modified = resource.lastModified();
            if (map.containsKey(resource)) {
                long lastModified = map.get(resource);
                if (lastModified != modified) {
                    map.put(resource, modified);
                    modifiedResources.add(resource.getDescription());
                    retVal = true;
                }
            } else {
                map.put(resource, modified);
            }
        } catch (IOException e) {
            llog.error("caught exception", e);
        }
        if (retVal) {
            if (llog.isInfoEnabled()) {
                llog.info("modified files : " + modifiedResources);
            }
        }
        return retVal;
    }
}
