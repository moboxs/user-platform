package com.github.moboxs.projects.user.service;

import com.github.moboxs.projects.user.domain.User;
import com.github.moboxs.projects.user.respository.DatabaseUserRepository;

public class UserServiceImpl implements UserService {

    private DatabaseUserRepository databaseUserRepository = new DatabaseUserRepository();

    @Override
    public boolean register(User user) throws Exception {
        user.setName("moboxs");
        user.setPhoneNumber("13683619335");
        boolean flag = databaseUserRepository.save(user);
        return flag;
    }
}
