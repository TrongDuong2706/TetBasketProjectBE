package com.hanu.isd.repository.httpclient;

import com.hanu.isd.dto.email.request.EmailRequest;
import com.hanu.isd.dto.email.response.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "email-client",
        url = "https://api.brevo.com"
)
public interface EmailClient {
    @PostMapping(
            value = {"/v3/smtp/email"},
            produces = {"application/json"}
    )
    EmailResponse sendEmail(@RequestHeader("api-key") String apiKey, @RequestBody EmailRequest body);
}
