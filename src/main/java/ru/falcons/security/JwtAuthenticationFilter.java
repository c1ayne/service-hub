package ru.falcons.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate;

    @Value("${laravel.api.url}")
    private String laravelUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<LaravelUser> backendResponse = restTemplate.exchange(
                    laravelUrl + "/auth/validate",
                    HttpMethod.GET,
                    entity,
                    LaravelUser.class
            );
            if (backendResponse.getStatusCode().is2xxSuccessful() && backendResponse.getBody() != null) {
                LaravelUser user = backendResponse.getBody();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.emptyList()
                );

                log.debug("User authorized: id={}, email={}", user.id(), user.email());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("Authorization failed for token. Status: {}", backendResponse.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Auth service error: {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }
}