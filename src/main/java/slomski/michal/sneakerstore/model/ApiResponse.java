package slomski.michal.sneakerstore.model;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ApiResponse {
    private Boolean success;
    private Date timeStamp;
    private int status;
    private String message;

    public ApiResponse(Boolean success, HttpStatus status, String message) {
        this.timeStamp = new Date();
        this.success = success;
        this.status = status.value();
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}