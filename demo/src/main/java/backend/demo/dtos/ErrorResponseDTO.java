package backend.demo.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> campos
) {
    public static ErrorResponseDTO of(int status, String error, String message, String path) {
        return new ErrorResponseDTO(LocalDateTime.now(), status, error, message, path, null);
    }

    public static ErrorResponseDTO withFields(
            int status,
            String error,
            String message,
            String path,
            Map<String, String> campos
    ) {
        return new ErrorResponseDTO(LocalDateTime.now(), status, error, message, path, campos);
    }
}