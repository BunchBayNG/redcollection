package com.bbng.dao.util.email.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO {

    @NotEmpty(message = "Key should not be empty")
    private String key;
    private Message message;
    @NotEmpty(message = "Email Type should not be empty")
    private String emailType;
    private String async;
    @JsonProperty("ip_pool")
    private String ipPool;
    @JsonProperty("send_at")
    private String sendAt;


}
