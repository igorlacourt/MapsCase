package com.lacourt.mapscase.network;

public class Resource<T> {
    public Status status = null;
    public  T data = null;
    public Error error = null;

    public enum Status {
        SUCCESS, ERROR, LOADING
    }

    public Resource(Status status, T data, Error error){
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public Resource<T> success(){
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public Resource<T> error(){
        return new Resource<>(Status.ERROR, null, error);
    }

    public Resource<T> loading(){
        return new Resource<>(Status.LOADING, data, null);
    }

    static {

    }
}
