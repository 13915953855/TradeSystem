package com.jason.trade.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoRequest extends UserInfo implements Serializable {

    private String oldPasswd;
    private String newPasswd;
    private String newPasswd2;

}