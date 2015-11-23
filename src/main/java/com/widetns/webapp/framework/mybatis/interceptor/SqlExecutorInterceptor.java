package com.widetns.webapp.framework.mybatis.interceptor;

import com.widetns.webapp.framework.common.BaseObject;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
                , @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
                , @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
                , @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
        }
)
public class SqlExecutorInterceptor extends BaseObject implements Interceptor {

    boolean isMapUnderscoreToCamelCase = false;

    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getArgs().length > 1) {
            addSqlComment(invocation.getArgs());
            return invocation.proceed();
        }
        return changeColumnName(invocation.proceed());
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

    private void addSqlComment(final Object[] args) {

        MappedStatement statement = (MappedStatement) args[0];
        isMapUnderscoreToCamelCase = statement.getConfiguration().isMapUnderscoreToCamelCase();

        if (args[1] instanceof Map) {
            String fileName = statement.getResource().substring(statement.getResource().lastIndexOf(System.getProperty("file.separator")) + 1).replace("]", "");
            BoundSql boundSql = statement.getBoundSql(args[1]);
            String tsql = statement.getBoundSql(args[1]).getSql().trim();
            StringBuilder sqlBuilder = new StringBuilder(100);
            sqlBuilder.append(tsql.substring(0, tsql.indexOf(" "))).append("/* ").append(fileName).append(":").append(statement.getId()).append(" */").append(tsql.substring(tsql.indexOf(" ")));
            BoundSql newBoundSql = new BoundSql(statement.getConfiguration(), sqlBuilder.toString(), boundSql.getParameterMappings(), boundSql.getParameterObject());
            MappedStatement newMappedStatement = copyFromMappedStatement(statement, new BoundSqlSqlSource(newBoundSql));
            args[0] = newMappedStatement;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms
                .getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    private Object changeColumnName(Object result) {

        StringBuilder sb = new StringBuilder(100);
        if (isMapUnderscoreToCamelCase) {

            if (result instanceof ArrayList) {
                if (((ArrayList) result).size() > 0) {
                    ArrayList<HashMap> newResult = new ArrayList<HashMap>();
                    ArrayList rows = (ArrayList) result;
                    for (Object row : rows.toArray()) {
                        if (row instanceof HashMap) {
                            HashMap<String, Object> newRow = new HashMap<String, Object>();
                            for (Object key : ((HashMap) row).keySet()) {
                                newRow.put(JdbcUtils.convertUnderscoreNameToPropertyName(key.toString()), ((HashMap) row).get(key));
                            }
                            newResult.add(newRow);
                        }
                    }
                    return newResult;
                } else {
                    return result;
                }
            } else if (result instanceof HashMap) {
                if (((HashMap) result).size() > 0) {
                    HashMap row = (HashMap) result;
                    HashMap<String, Object> newRow = new HashMap<String, Object>();
                    for (Object key : row.keySet()) {
                        newRow.put(JdbcUtils.convertUnderscoreNameToPropertyName(key.toString()), row.get(key));
                    }
                    return newRow;
                } else {
                    return result;
                }
            } else {
                return result;
            }
        } else {
            return result;
        }
    }

    public static class BoundSqlSqlSource implements SqlSource {

        final BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
