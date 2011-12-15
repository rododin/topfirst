package com.topfirst.web;

import java.lang.System;


/**
 * Created by IntelliJ IDEA.
 * User: Vofka
 * Date: 12.12.11
 * Time: 1:56
 * To change this template use File | Settings | File Templates.
 */

public class UserLogin {
    // getter / setter ------------------------------------
    public void setUsername(String username)
    {
        this.username=username;
    };
    public String getUsername()
    {
        return username;
    };
    public void setPassword(String password)
    {
        this.password=password;
    };
    public String getPassword()
    {
        return password;
    };
    public void setAuthResult(boolean authResult)
    {
        this.authResult=authResult;
    }
    public boolean getAuthResult()
    {
        return authResult;
    };
// bottom listener ------------------------------------


    public void loginBottomPressed()
    {
        if((username.equals("q"))&&(password.equals("q")))
        {
            authResult=true;
        }else{
            authResult=false;
        };

        System.out.println("user auth reasult -"+authResult);

    };
    public void logoutBottomPressed()
    {
        authResult=false;
    };

    // attributes -----------------------------------------
    private String username = "";
    private String password = "";
    private boolean authResult=false;
}
