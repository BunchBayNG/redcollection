package com.bbng.dao.util.exceptions.customExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistException extends RuntimeException{

    public ResourceAlreadyExistException(String message){
        super(message);
    }
}
