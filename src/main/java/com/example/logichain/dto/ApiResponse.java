package com.example.logichain.dto;

public class ApiResponse<T>{
    private T data;
    private int page;
    private int size;
    private long totalElements;
    private String message;

    public ApiResponse(T data, int page, int size, int totalElements, String message){
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
