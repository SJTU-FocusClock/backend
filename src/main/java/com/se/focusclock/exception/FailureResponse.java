package com.se.focusclock.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailureResponse {
    private Object result;

    /*
     * status:
     *  0 - Success
     * -1 - Unauthorized
     * -2 - Incorrect parameters
     */
    private int status;

    public FailureResponse(Object result, int status){
        this.result = result;
        this.status = status;
    }
}
