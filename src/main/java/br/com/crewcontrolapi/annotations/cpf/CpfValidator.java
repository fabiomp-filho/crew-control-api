package br.com.crewcontrolapi.annotations.cpf;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {
    @Override
    public void initialize(Cpf constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        return (cpf != null) && cpf.matches("[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}-[0-9]{2}") && isCpfValid(cpf);
    }

    private boolean isCpfValid(String cpf) {
        cpf = cpf.replace(".", "").replace("-", "");

        if (cpf.length() != 11 || cpf.matches(cpf.charAt(0) + "{11}")) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Integer.parseInt(cpf.substring(i, i + 1)) * (10 - i);
        }
        int firstDigit = (sum * 10) % 11;
        if (firstDigit == 10) {
            firstDigit = 0;
        }
        if (firstDigit != Integer.parseInt(cpf.substring(9, 10))) return false;

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Integer.parseInt(cpf.substring(i, i + 1)) * (11 - i);
        }
        int secondDigit = (sum * 10) % 11;
        if (secondDigit == 10) {
            secondDigit = 0;
        }
        return secondDigit == Integer.parseInt(cpf.substring(10, 11));
    }


}
