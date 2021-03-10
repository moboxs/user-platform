package com.github.moboxs.projects.user.service;

import com.github.moboxs.projects.user.domain.User;
import com.github.moboxs.projects.user.respository.DatabaseUserRepository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    //private DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository();

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;


    @Override
    public boolean register(User user) throws Exception {

        user.setName("moboxs");
        user.setPhoneNumber("13683619335");

        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        try {
            entityManager.persist(user);

            List<User> resultList = entityManager.createQuery("from User", User.class).getResultList();
            resultList.forEach(System.out::println);
            transaction.commit();
        } catch (Exception e ) {
            transaction.rollback();
            throw new RuntimeException(e);
        } finally {
            System.out.println("transactional finally");
        }

        //boolean flag = databaseUserRepository.save(user);
        return true;
    }
}
