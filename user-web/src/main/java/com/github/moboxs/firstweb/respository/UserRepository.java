package com.github.moboxs.firstweb.respository;

import com.github.moboxs.firstweb.entity.User;

import java.util.Collection;

public interface UserRepository {

    boolean save(User user) throws Exception;

    Collection<User> getAll();

}
