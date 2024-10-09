package com.solidcode.library.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class RateLimitingFilterTest {

    private RateLimitingFilter rateLimitingFilter;
    private FilterChain filterChain;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        rateLimitingFilter = new RateLimitingFilter();
        filterChain = mock(FilterChain.class);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testRateLimitingExceeded() throws ServletException, IOException {

        for (int i = 0; i < 21; i++) {
            rateLimitingFilter.doFilterInternal(request, response, filterChain);
        }
        assertEquals(429, response.getStatus());
    }

    @Test
    void testRateLimitingNotExceeded() throws ServletException, IOException {
        rateLimitingFilter.doFilterInternal(request, response, filterChain);
        assertEquals(200, response.getStatus());
    }
}