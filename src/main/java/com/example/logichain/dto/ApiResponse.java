package com.example.logichain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {



    private T data;
    private Integer page;
    private Integer size;
    private Integer totalElements;
    private String message;

    public ApiResponse(T data, Integer page, Integer size, Integer totalElements, String message){
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.message = message;
    }

    public ApiResponse(T data, String message){
        this.data = data;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
