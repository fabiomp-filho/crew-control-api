package br.com.crewcontrolapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.lang.reflect.Field;

public enum RoleEnum {
    @JsonProperty(value = "Administrador")
    ADMINISTRATOR,
    @JsonProperty(value = "Lider")
    LEADER,
    @JsonProperty(value = "Colaborador")
    COLLABORATOR;

    @JsonCreator
    public static RoleEnum forValue(String value) {
        for (RoleEnum role : values()) {
            if (role.toString().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Valor de função inválida : " + value);
    }

    @JsonValue
    public String toValue() {
        for (Field field : RoleEnum.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonProperty.class) && this.name().equals(field.getName())) {
                return field.getAnnotation(JsonProperty.class).value();
            }
        }
        return this.name();
    }

}

