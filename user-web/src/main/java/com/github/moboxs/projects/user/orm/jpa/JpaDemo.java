package com.github.moboxs.projects.user.orm.jpa;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

public class JpaDemo {

//    @Resource
//    private EntityManager entityManager;

    @PersistenceContext(name = "")
    private EntityManager entityManager;

    @Resource(name = "primaryDataSource")
    private DataSource dataSource;

    public static void main(String[] args) {
        
    }
}
