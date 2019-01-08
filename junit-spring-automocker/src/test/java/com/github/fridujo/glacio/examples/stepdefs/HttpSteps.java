package com.github.fridujo.glacio.examples.stepdefs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicReference;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.hjson.JsonValue;
import org.hjson.Stringify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.github.fridujo.glacio.model.DocString;
import com.github.fridujo.glacio.running.api.Then;
import com.github.fridujo.glacio.running.api.When;

public class HttpSteps {

    private final MockMvc mockMvc;
    private final AtomicReference<MvcResult> lastMvcResult = new AtomicReference<>();

    public HttpSteps(@Autowired MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @When("HTTP (.*) on '(.*)'")
    public void http_exchange(String verb, String uri) throws Exception {
        mockMvc.perform(request(HttpMethod.resolve(verb), uri)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(lastMvcResult::set);
    }

    @When("HTTP (.*) on '(.*)' with content")
    public void http_exchange_with_body(String verb, String uri, DocString body) throws Exception {
        mockMvc.perform(request(HttpMethod.resolve(verb), uri)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(toJson(body.getContent()))).andDo(lastMvcResult::set);
    }

    @Then("last HTTP response status is '(.*)'")
    public void last_http_response_status_is(int code) {
        int status = lastMvcResult.get().getResponse().getStatus();
        assertThat(status)
            .as("last HTTP response status code: " + HttpStatus.valueOf(status).getReasonPhrase())
            .isEqualTo(code);
    }

    @Then("last HTTP response body matches json-path '(.*)' with value '(.*)'")
    public void last_http_response_body_matches_jsonPath(String jsonPathExpression, String expectedValue) throws UnsupportedEncodingException {
        String body = lastMvcResult.get().getResponse().getContentAsString();
        Object value = extractSingleValue(JsonPath.read(body, jsonPathExpression));
        assertThat(String.valueOf(value)).isEqualTo(expectedValue);
    }

    private Object extractSingleValue(Object object) {
        if (object instanceof JSONArray && ((JSONArray) object).size() == 1) {
            return ((JSONArray) object).iterator().next();
        }
        return object;
    }

    private String toJson(String hjson) {
        return JsonValue.readHjson(hjson).toString(Stringify.FORMATTED);
    }
}
