package com.account.bam.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientService {
    private static final String SUCCESS_RESPONSE = "success";
    private static final String FAILURE_RESPONSE = "failure";

    @Autowired
    private RestTemplate restTemplate;

    public String invokeExternalWebService() {
        String statusResponse = null;
        try {
            String response = restTemplate
                    .getForObject("https://httpstat.us/200", String.class);
            if (StringUtils.hasText(response) && response.contains("200")) {
                statusResponse = SUCCESS_RESPONSE;
            }
        } catch (Exception e) {
            statusResponse = FAILURE_RESPONSE;
        }
        return statusResponse;
    }
}
