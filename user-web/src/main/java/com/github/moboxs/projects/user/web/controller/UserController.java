package com.github.moboxs.projects.user.web.controller;

import com.github.moboxs.context.ComponentContext;
import com.github.moboxs.projects.user.domain.User;
import com.github.moboxs.projects.user.respository.DatabaseUserRepository;
import com.github.moboxs.projects.user.service.UserService;
import com.github.moboxs.projects.user.service.UserServiceImpl;
import com.github.moboxs.web.mvc.controller.PageController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.Validator;
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

//    @Resource(name = "bean/Validator")
//    private Validator validator;

    @POST
    @Path(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws Throwable{
        User user = new User();
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        UserService userService = ComponentContext.getInstance().getComponent("bean/UserService");
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
