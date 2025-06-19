package com.bbng.dao.util.exceptions.customExceptions;

public class TokenNotValidException extends  RuntimeException{
    public TokenNotValidException(String message){
        super(message);
    }
}
