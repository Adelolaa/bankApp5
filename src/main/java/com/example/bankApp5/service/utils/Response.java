package com.example.bankApp5.service.utils;


public class Response {
    public String message;
    public int status;
    public Object data;
    public Response(String message,int status,Object data) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Response{}";
    }
}
