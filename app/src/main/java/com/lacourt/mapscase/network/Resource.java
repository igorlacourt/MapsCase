package com.lacourt.mapscase.network;

import androidx.annotation.Nullable;

public class Resource<T> {
    public Status status;
    public T data;
    public Error error;

    public enum Status {
        SUCCESS, ERROR, LOADING
    }

    public Resource(){
    }

    private Resource(Status status, T data, Error error){
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public Resource<T> success(T data){
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public Resource<T> error(Error error){
        return new Resource<>(Status.ERROR, null, error);
    }

    public Resource<T> loading(){
        return new Resource<>(Status.LOADING, null, null);
    }

}
