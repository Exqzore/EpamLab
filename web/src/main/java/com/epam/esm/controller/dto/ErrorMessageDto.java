package com.epam.esm.controller.dto;

import java.util.List;

public class ErrorMessageDto {
    private int errorCode;
    private List<String> errorMessages;

    public ErrorMessageDto(int errorCode, List<String> errorMessages) {
        this.errorCode = errorCode;
        this.errorMessages = errorMessages;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorMessageDto{");
        sb.append("errorCode=").append(errorCode);
        sb.append(", errorMessages=").append(errorMessages);
        sb.append('}');
        return sb.toString();
    }
}
