package com.github.moboxs.projects.user.management;

/**
 * MBean接口描述
 */
public interface UserManagerMBean {

     Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getPassword();

    void setPassword(String password);

    String getEmail();

    void setEmail(String email);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);

    //String toString();

}
