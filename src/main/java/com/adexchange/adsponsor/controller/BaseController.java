package com.adexchange.adsponsor.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    //protected HttpSession session;

    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
        //this.session = request.getSession();
    }
}
