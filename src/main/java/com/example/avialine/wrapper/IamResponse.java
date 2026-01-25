package com.example.avialine.wrapper;

import java.io.Serializable;

public record IamResponse<P extends Serializable>(
        String message,
        P payload,
        boolean done
) {
    public static <P extends Serializable> IamResponse<P> createdSuccessfully(P payload){
        return new IamResponse<>("request successfully accepted", payload,true);
    }
}
