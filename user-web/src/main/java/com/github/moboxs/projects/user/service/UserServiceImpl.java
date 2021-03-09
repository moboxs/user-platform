package com.github.moboxs.projects.user.service;

import com.github.moboxs.projects.user.domain.User;
import com.github.moboxs.projects.user.respository.DatabaseUserRepository;

public class UserServiceImpl implements UserService {

    private DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository();

    @Override
    public boolean register(User user) throws Exception {
        user.setName("moboxs");
        user.setEmail("liuhh26@sina.com");
        user.setPhoneNumber("13622222211");
        user.setPassword("***");
        boolean flag = databaseUserRepository.save(user);
        return flag;
    }
}
