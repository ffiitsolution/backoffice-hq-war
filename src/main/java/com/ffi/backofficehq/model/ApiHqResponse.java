package com.ffi.backofficehq.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USER
 * @param <T>
 */
public class ApiHqResponse<T> {
    
    private Boolean success;
    private String message;
    private T data;

    @JsonProperty("success")
    public Boolean getSuccess() { return success; }
    @JsonProperty("success")
    public void setSuccess(Boolean value) { this.success = value; }

    @JsonProperty("message")
    public String getMessage() { return message; }
    @JsonProperty("message")
    public void setMessage(String value) { this.message = value; }

    @JsonProperty("data")
    public T getData() { return data; }
    @JsonProperty("data")
    public void setData(T value) { this.data = value; }
}
