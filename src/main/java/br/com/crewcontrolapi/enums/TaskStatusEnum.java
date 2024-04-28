package br.com.crewcontrolapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.lang.reflect.Field;

public enum TaskStatusEnum {
    @JsonProperty(value = "Não iniciada")
    NOT_STARTED,
    @JsonProperty(value = "Em andamento")
    IN_PROGRESS,
    @JsonProperty(value = "Cancelada")
    CANCELLED,
    @JsonProperty(value = "Atrasada")
    DELAYED,
    @JsonProperty(value = "Concluída")
    COMPLETED;

    @JsonCreator
    public static TaskStatusEnum forValue(String value) {
        for (TaskStatusEnum role : values()) {
            if (role.toString().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Status de atividade inválido: " + value);
    }

    @JsonValue
    public String toValue() {
        for (Field field : TaskStatusEnum.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonProperty.class) && this.name().equals(field.getName())) {
                return field.getAnnotation(JsonProperty.class).value();
            }
        }
        return this.name();
    }
}
