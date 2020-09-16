package com.fxbin.demo.filter;

import com.fxbin.demo.config.MyResponseWrapper;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "changeResponseFilter", urlPatterns = "/*")
public class ChangeResponseFilter implements Filter {
    private static Pattern p=Pattern.compile("\\$\\{(.*?)\\}\\$");
    @Override
    public void destroy() {
        System.out.println("destroy...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest =(HttpServletRequest)request;
        if (!httpServletRequest.getRequestURI().equals("/index")) {
           chain.doFilter(request,response);
        }else {
            MyResponseWrapper responseWrapper = new MyResponseWrapper((HttpServletResponse) response);
            chain.doFilter(request, responseWrapper);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(new String(responseWrapper.getResponseData()));
            response.getWriter().write(format(stringBuilder.toString()));
        }
    }



    private static String format(String str) {
        Matcher matcher = p.matcher(str);
        String result = matcher.replaceAll("This a new replace");
        return result;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("init...");
    }
}

