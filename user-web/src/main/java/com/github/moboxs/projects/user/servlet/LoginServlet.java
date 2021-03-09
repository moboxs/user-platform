package com.github.moboxs.projects.user.servlet;

import java.io.*;
import java.util.Enumeration;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class LoginServlet extends HttpServlet {
    private String message;

    @Override
    public void init() {
        message = "Hello World!";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String loginName = request.getParameter("name"); // 返回请求参数中的第一个值
        String[] parameterValues = request.getParameterValues("");// 返回一个参数的多个值

        Enumeration<String> parameterNames = request.getParameterNames(); // 参数值类型String, 通过StringToXXX 转换

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + loginName + "</h1>");
        out.println("登录成功");
        out.println("</body></html>");
    }

    @Override
    public void destroy() {
    }
}