package server.test.exception;

import org.springframework.http.HttpStatus;
import server.test.exception.enums.ServiceMessage;

public class ServiceProcessException extends RuntimeException {
    private int code = -1;
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public ServiceProcessException() {
        super("error");
    }

    public ServiceProcessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ServiceProcessException(String message, int code, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public ServiceProcessException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ServiceProcessException(String message) {
        super(message);
        this.code = -1;
    }

    public ServiceProcessException(ServiceMessage serviceMessage) {
        super(serviceMessage.getMessage());
        this.code = serviceMessage.getCode();
    }

    public ServiceProcessException(Throwable cause) {
        super("error", cause);
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}

