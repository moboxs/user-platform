package com.github.moboxs.projects.user.respository;

import com.github.moboxs.projects.user.domain.User;

import java.util.Collection;

public interface UserRepository {

    boolean save(User user) throws Exception;

    Collection<User> getAll();

}
