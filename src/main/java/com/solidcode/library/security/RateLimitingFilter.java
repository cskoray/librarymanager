package com.solidcode.library.security;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.bucket4j.Bandwidth.classic;
import static io.github.bucket4j.Refill.intervally;
import static java.time.Duration.ofHours;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {

        return Bucket.builder().addLimit(classic(5, intervally(5, ofHours(1)))).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        Bucket tokenBucket = buckets.computeIfAbsent(clientIp, k -> createNewBucket());

        if (tokenBucket.tryConsume(1)) {

            filterChain.doFilter(request, httpServletResponse);
        } else {

            httpServletResponse.setStatus(429);

            httpServletResponse.getWriter().write("Rate limit exceeded. Try again later.");
        }
    }
}
