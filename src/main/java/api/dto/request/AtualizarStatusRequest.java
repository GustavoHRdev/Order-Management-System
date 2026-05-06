package api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AtualizarStatusRequest(
        @NotBlank(message = "status: não deve estar em branco.")
        String status
) {
}
