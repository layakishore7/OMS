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
        APIResponse response = new APIResponse();
        response.setStatus(HttpStatus.OK);
        response.setMessage(createdSuccessfully);
        response.setStatusCode(200);
        response.setData(data);
        return ResponseEntity.ok(response);

    }

    public static ResponseEntity<APIResponse> success(Object data){
        APIResponse response = new APIResponse();
        response.setSuccess(true);
        response.setMessage("Success");
        response.setStatus(HttpStatus.OK);
        response.setStatusCode(200);
        response.setData(data);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<APIResponse> success(String createdSuccessfully) {
        APIResponse response = new APIResponse();
        response.setStatus(HttpStatus.OK);
        response.setMessage(createdSuccessfully);
        response.setStatusCode(200);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<APIResponse> created(String message, Object data) {
        APIResponse response = new APIResponse();
        response.setStatus(HttpStatusCode.valueOf(201));
        response.setStatusCode(201);
        response.setSuccess(true);
        response.setData(data);
        response.setMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static ResponseEntity<APIResponse> updated(String message, Object data){
        APIResponse response = new APIResponse();
        response.setStatus(HttpStatusCode.valueOf(201));
        response.setStatusCode(201);
        response.setSuccess(true);
        response.setData(data);
        response.setMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
