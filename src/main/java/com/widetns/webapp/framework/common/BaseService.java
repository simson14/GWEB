package com.widetns.webapp.framework.common;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService extends BaseObject {

    @Autowired
    private SqlSession sqlSession;

    public SqlSession getSqlSession() {
        return sqlSession;
    }
}
