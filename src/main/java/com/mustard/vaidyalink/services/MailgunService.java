package com.mustard.vaidyalink.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class MailgunService {

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.from.email}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    public void sendPasswordEmail(String recipient, String subject, String password) {
        String mailgunUrl = "https://api.mailgun.net/v3/" + domain + "/messages";

        MultiValueMap<String, String> form = basicFormat(recipient, subject);
        form.add("text", "Thank you for registering on VaidyaLink 🙌. Your temporary password for login is: " + password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
        restTemplate.exchange(mailgunUrl, HttpMethod.POST, request, String.class);
    }

    public void sendEmail(String recipient, String subject, String body) {
        MultiValueMap<String, String> form = basicFormat(recipient, subject);
        form.add("text", body);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
        restTemplate.exchange("https://api.mailgun.net/v3/" + domain + "/messages", HttpMethod.POST, request, String.class);
    }

    private MultiValueMap<String, String> basicFormat(String recipient, String subject) {
        headers.setBasicAuth("api", apiKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("from", fromEmail);
        form.add("to", recipient);
        form.add("subject", subject);

        return form;
    }
}
