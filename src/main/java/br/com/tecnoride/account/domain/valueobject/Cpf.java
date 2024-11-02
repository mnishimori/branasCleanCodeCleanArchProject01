package br.com.tecnoride.account.domain.valueobject;

import static br.com.tecnoride.account.domain.field.AccountDomainField.ACCOUNT_CPF_FIELD;
import static br.com.tecnoride.account.domain.message.AccountDomainMessage.CPF_IS_INVALID;

import br.com.tecnoride.account.domain.exception.BusinessException;
import org.springframework.validation.FieldError;

public record Cpf(String cpfNumber) {

  public Cpf {
    verifyIfCpfNumberIsNullOrEmpty(cpfNumber);
    verifyIfCpfNumberPatternMatches(cpfNumber);
  }

  private void verifyIfCpfNumberIsNullOrEmpty(String cpfNumber) {
    if (cpfNumber == null || cpfNumber.trim().isEmpty()) {
      throw new BusinessException(
          new FieldError(this.getClass().getSimpleName(), ACCOUNT_CPF_FIELD, CPF_IS_INVALID.formatted(cpfNumber)));
    }
  }

  private void verifyIfCpfNumberPatternMatches(String cpfNumber) {
    var cpfValid = this.validateLengthOf(cpfNumber);
    cpfValid = cpfValid && this.validate(cpfNumber);
    if (!cpfValid) {
      throw new BusinessException(
          new FieldError(this.getClass().getSimpleName(), ACCOUNT_CPF_FIELD, CPF_IS_INVALID.formatted(cpfNumber)));
    }
  }

  private boolean validate(String rawCpf) {
    String cpf = removeNonDigits(rawCpf);
    if (hasAllDigitsEqual(cpf)) {
      return false;
    }
    int digit1 = calculateDigit(cpf, 10);
    int digit2 = calculateDigit(cpf, 11);
    return extractDigit(cpf).equals(String.valueOf(digit1) + digit2);
  }

  private String extractDigit(String cpf) {
    return cpf.substring(9);
  }

  private int calculateDigit(String cpf, int factor) {
    int total = 0;
    for (char digit : cpf.toCharArray()) {
      if (factor > 1) {
        total += Character.getNumericValue(digit) * factor--;
      }
    }
    int rest = total % 11;
    return (rest < 2) ? 0 : 11 - rest;
  }

  private boolean hasAllDigitsEqual(String cpf) {
    char firstCpfDigit = cpf.charAt(0);
    return cpf.chars().allMatch(digit -> digit == firstCpfDigit);
  }

  private String removeNonDigits(String cpf) {
    return cpf.replaceAll("\\D", "");
  }

  private boolean validateLengthOf(String cpfValue) {
    return cpfValue.trim().length() == 11;
  }
}
