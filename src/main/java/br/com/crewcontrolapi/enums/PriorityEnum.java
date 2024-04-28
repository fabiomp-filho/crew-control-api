package br.com.crewcontrolapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.lang.reflect.Field;

public enum PriorityEnum {
    @JsonProperty(value = "Alta")
    HIGH,
    @JsonProperty(value = "Normal")
    NORMAL,
    @JsonProperty(value = "Baixa")
    LOW;

    @JsonCreator
    public static PriorityEnum forValue(String value) {
        for (PriorityEnum role : values()) {
            if (role.toString().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Valor de prioridade inválida: " + value);
    }

    @JsonValue
    public String toValue() {
        for (Field field : PriorityEnum.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonProperty.class) && this.name().equals(field.getName())) {
                return field.getAnnotation(JsonProperty.class).value();
            }
        }
        return this.name();
    }
}
