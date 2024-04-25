package br.com.crewcontrolapi.exception;

public record ExceptionsResponse(int code, String Status, String message, Object details){

}
