package com.bbng.dao.util.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ResponseDto<T> {
    private int statusCode;
    private boolean status;
    private String message;
    private T data;

}
