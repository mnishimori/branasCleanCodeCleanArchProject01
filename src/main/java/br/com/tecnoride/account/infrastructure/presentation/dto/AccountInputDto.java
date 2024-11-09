package br.com.tecnoride.account.infrastructure.presentation.dto;

public record AccountInputDto(
    String email, String name, String cpf, String carPlate, boolean isPassenger,
    boolean isDriver) {
}
