package com.lacourt.mapscase.network;

public class Error {
    private Integer code;
    private String message;

    public static String GENERIC = "Something when wrong. Try again later.";

    public Error(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
