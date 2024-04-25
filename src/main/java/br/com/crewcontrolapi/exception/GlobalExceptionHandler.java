package br.com.crewcontrolapi.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado: " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ExceptionsResponse handleExceptionsBadRequest(MethodArgumentNotValidException e) {
        List<FieldErrorRecord> details = new ArrayList<>();

        for (FieldError violation : e.getBindingResult().getFieldErrors()) {
            String field = violation.getField();
            String message = violation.getDefaultMessage();
            details.add(new FieldErrorRecord(field, message));
        }

        return new ExceptionsResponse(
                400,
                HttpStatus.BAD_REQUEST.name(),
                "Requisição Inválida",
                details
        );
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionsResponse handleIllegalArgumentException(HttpMessageNotReadableException e) {
        String message = extractUsefulMessage(e);
        String detailedMessage = e.getMessage();

        return new ExceptionsResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                message,
                detailedMessage
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ExceptionsResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Throwable rootCause = e.getMostSpecificCause();

        String message = rootCause.getMessage();
        String detailedMessage = "";

        String violatedField = extractViolatedField(message);

        detailedMessage += violatedField;

        return new ExceptionsResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                detailedMessage,
                message
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BusinessException.class})
    public ExceptionsResponse handleBusinessException(BusinessException e) {
        return new ExceptionsResponse(
                400,
                "Bad Request",
                e.getMessage(),
                new ArrayList<>()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor: " + e.getClass());
    }

    private String extractViolatedField(String message) {
        Pattern pattern = Pattern.compile("Detalhe: Chave \\((.*?)\\)=\\((.*?)\\) já existe.");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            // Retornando a informação formatada do campo e valor violado
            return "Campo '" + matcher.group(1) + "' com valor '" + matcher.group(2) + "' já existe.";
        }
        return "Detalhe não especificado";
    }

    private String extractUsefulMessage(HttpMessageNotReadableException e) {
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            String message = e.getCause().getMessage();

            Pattern pattern = Pattern.compile("Invalid role value: (.+)");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                return "O valor '" + matcher.group(1) + "' não é válido. Por favor, informe um valor válido!";
            }

            pattern = Pattern.compile("Could not read document: (.*);");
            matcher = pattern.matcher(message);

            if (matcher.find()) {
                return "Could not read the provided input: " + matcher.group(1);
            }

            return "O valor informado é inválido!";
        }
        return "Error reading the provided input.";
    }
}

