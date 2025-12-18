package ru.falcons.security;

public record User(
        Long id,
        String email,
        String role
) {
}
