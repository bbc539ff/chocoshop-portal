package com.chocoshop.filter;


import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

//@WebFilter({"/user/login", "/user/register"})
//@Order(1)
//@Component
public class XssFilter implements Filter {
    final String[] charset = {"\"","/","\\","<",">"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        if(req.getMethod().equals("POST")){
            System.out.println("------");
            Map<String, String[]> map = req.getParameterMap();
            for(Map.Entry<String, String[]> entry : map.entrySet()){
                // “”,/,\,<,>”
                for(String v : entry.getValue()){
                    System.out.println(v);
                    for(String ch : charset){
                        if(v.contains(ch)){
                            response.getWriter().append("error(contains illegal character)");
                            return;
                        }
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

}
