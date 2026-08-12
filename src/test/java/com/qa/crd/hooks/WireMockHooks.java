package com.qa.crd.hooks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class WireMockHooks {

    private static final int WIREMOCK_PORT = 8089;

    // Shared across all scenarios; reset between them
    static final WireMockServer wireMockServer = new WireMockServer(
            WireMockConfiguration.wireMockConfig()
                    .port(WIREMOCK_PORT)
                    .usingFilesUnderClasspath("wiremock")
    );

    static {
        wireMockServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(wireMockServer::stop));
    }

    @Before
    public void beforeScenario() {
        wireMockServer.resetAll();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = WIREMOCK_PORT;
        RestAssured.basePath = "";
    }

    @After
    public void afterScenario() {
        // Verify no unexpected requests were made (optional – remove if too strict)
        // wireMockServer.checkForUnmatchedRequests();
    }

    public static WireMockServer getServer() {
        return wireMockServer;
    }
}