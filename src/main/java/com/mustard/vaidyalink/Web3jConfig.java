package com.mustard.vaidyalink;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    @Value("${WEB3J_ETHEREUM_ENDPOINT:http://localhost:8545}")
    private String ethereumRpcUrl;

    @Value("${ETHEREUM_PRIVATE_KEY}")
    private String ethereumPrivateKey;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(ethereumRpcUrl));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(ethereumPrivateKey);
    }
}
