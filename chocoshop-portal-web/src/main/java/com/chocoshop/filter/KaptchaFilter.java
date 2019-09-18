package com.chocoshop.filter;

import com.google.code.kaptcha.Constants;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter("/user/login")
//@Order(1)
public class KaptchaFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if(!httpServletRequest.getMethod().equals("POST")) {
            chain.doFilter(request, response);
            return;
        }

        String sessVerifyCode  = (String) httpServletRequest.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        System.out.println("sessVerifyCode-->"+sessVerifyCode);

        String reqVerifyCode = httpServletRequest.getParameter("verifyCode");
        if(sessVerifyCode!=null && sessVerifyCode.equals(reqVerifyCode)){
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect("login");
            return;
        }
    }
}
