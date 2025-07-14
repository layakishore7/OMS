package com.ordermanagement.domain.misc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {

    private boolean success;

    private String message;

    private int statusCode;

    private HttpStatusCode status;

    private Object data;

    public static ResponseEntity<APIResponse> success(String createdSuccessfully, Object data){
        APIResponse resposne = new APIResponse();
        resposne.setStatus(HttpStatus.OK);
        resposne.setMessage(createdSuccessfully);
        resposne.setStatusCode(200);
        resposne.setData(data);
        return ResponseEntity.ok(resposne);

    }

    public static ResponseEntity<APIResponse> success(Object data){
        APIResponse resposne = new APIResponse();
        resposne.setStatus(HttpStatus.OK);
        resposne.setStatusCode(200);
        resposne.setData(data);
        return ResponseEntity.ok(resposne);
    }

    public static ResponseEntity<APIResponse> success(String createdSuccessfully) {
        APIResponse resposne = new APIResponse();
        resposne.setStatus(HttpStatus.OK);
        resposne.setMessage(createdSuccessfully);
        resposne.setStatusCode(200);
        return ResponseEntity.ok(resposne);
    }

    public static ResponseEntity<APIResponse> created(String message, Object data){
        APIResponse resposne = new APIResponse();
        resposne.setStatus(HttpStatusCode.valueOf(201));
        resposne.setStatusCode(201);
        resposne.setSuccess(true);
        resposne.setData(data);
        resposne.setMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposne);

    }

    public static ResponseEntity<APIResponse> updated(String message, Object data){
        APIResponse resposne = new APIResponse();
        resposne.setStatus(HttpStatusCode.valueOf(201));
        resposne.setStatusCode(201);
        resposne.setSuccess(true);
        resposne.setData(data);
        resposne.setMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(resposne);
    }

    public static ResponseEntity<APIResponse> error(String message){
        APIResponse response = new APIResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        return ResponseEntity.badRequest().body(response);
    }

    public static ResponseEntity<APIResponse> error(String message ,HttpStatus httpStatus) {
        APIResponse response = new APIResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setStatus(httpStatus);
        response.setStatusCode(httpStatus.value());
        response.setData(null);
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static ResponseEntity<APIResponse> error(HttpStatus status,String message) {
        APIResponse response = new APIResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        return ResponseEntity.status(status).body(response);
    }

}
