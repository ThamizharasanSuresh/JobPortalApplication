package com.example.companyservice.dto;

import com.sharepersistence.entity.User;
import lombok.Data;

@Data
public class AuthUserResponse {
    private boolean success;
    private String message;
    private Object data;
}
