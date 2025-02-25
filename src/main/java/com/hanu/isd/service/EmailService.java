package com.hanu.isd.service;

import com.hanu.isd.dto.email.request.EmailRequest;
import com.hanu.isd.dto.email.request.SendEmailRequest;
import com.hanu.isd.dto.email.request.Sender;
import com.hanu.isd.dto.email.response.EmailResponse;
import com.hanu.isd.exception.AppException;
import com.hanu.isd.exception.ErrorCode;
import com.hanu.isd.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;
    @NonFinal
    @Value("${email.email_key}")
    protected String apiKey;
    public EmailResponse sendEmail(SendEmailRequest request){
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Luxy Tet Gift")
                        .email("vojungkook209@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try{
            return emailClient.sendEmail(apiKey,emailRequest);
        }
        catch (FeignException e){
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }

    }
}
