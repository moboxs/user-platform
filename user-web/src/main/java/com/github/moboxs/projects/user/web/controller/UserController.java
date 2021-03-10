package com.github.moboxs.projects.user.web.controller;

import com.github.moboxs.projects.user.domain.User;
import com.github.moboxs.projects.user.respository.DatabaseUserRepository;
import com.github.moboxs.projects.user.service.UserService;
import com.github.moboxs.projects.user.service.UserServiceImpl;
import com.github.moboxs.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author liuhonghao
 * @date 2021-03-09 18:17
 */
@Path("/user")
public class UserController implements PageController {

    UserService userService = new UserServiceImpl();

    @POST
    @Path(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws Throwable{
        User user = new User();
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        userService.register(user);
        return "/success.jsp";
    }

    @GET
    @Path("/register")
    public String register() throws Throwable {
        return "login-form.jsp";
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return "/login-form.jsp";
    }
}
