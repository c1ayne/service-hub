package ru.falcons.security;

public record LaravelUser(
        Long id,
        String email,
        String role
) {
}
