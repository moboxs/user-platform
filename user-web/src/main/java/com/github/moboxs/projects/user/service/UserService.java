package com.github.moboxs.projects.user.service;

import com.github.moboxs.projects.user.domain.User;

public interface UserService {
    boolean register(User user) throws Exception;
}
