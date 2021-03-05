package com.github.moboxs.firstweb.dao;

import com.github.moboxs.firstweb.entity.Student;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class StudentDAOTest {

    @Test
    public void persist() {
        //获得实体管理类
        EntityManager manager = JpaUtils.getEntityManager();
        //获得事务管理器
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        Student student = new Student();
        student.setStuName("张三");
        student.setStuAge(18);
        manager.persist(student);

        transaction.commit();

        manager.close();
    }
}
