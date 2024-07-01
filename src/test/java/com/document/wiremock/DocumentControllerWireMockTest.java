package com.document.wiremock;

import com.document.dto.DocumentDTO;
import com.document.entity.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.document.utils.DocumentUtils.getExpectedResponse;
import static com.document.utils.DocumentUtils.getRequest;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;


class DocumentControllerWireMockTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private String BASE_URL = "http://localhost:8080/api/v1/document";


    private WireMockServer wireMockServer;

    @BeforeEach
    public void init() {
        wireMockServer =  new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    public void tearDown(){
        if(wireMockServer != null){
            wireMockServer.stop();
        }
    }

    @Test
    void saveDocument() throws Exception {
        DocumentDTO request = getRequest();
        DocumentDTO expectedResponse = getExpectedResponse();

        // Stubbing the response for the POST request
        stubFor(post(urlEqualTo("/api/v1/document"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedResponse))));


        // Sending the HTTP POST request
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(BASE_URL);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(request)));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        // Parsing the response content
        DocumentDTO actualResponse = objectMapper.readValue(httpResponse.getEntity().getContent(), DocumentDTO.class);
        Assertions.assertEquals(expectedResponse, actualResponse);
        verify(1,
                postRequestedFor(urlPathEqualTo("/api/v1/document"))
                        .withRequestBody(equalTo(objectMapper.writeValueAsString(request))));
    }
}
