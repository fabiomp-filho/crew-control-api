package br.com.crewcontrolapi.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.lang.reflect.Field;

public enum TaskItemStatusEnum {

    @JsonProperty(value = "Concluído")
    COMPLETED,
    @JsonProperty(value = "Não concluído")
    NOT_COMPLETED;

    @JsonCreator
    public static TaskItemStatusEnum forValue(String value) {
        for (TaskItemStatusEnum role : values()) {
            if (role.toString().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Status de item inválido: " + value);
    }

    @JsonValue
    public String toValue() {
        for (Field field : TaskItemStatusEnum.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(JsonProperty.class) && this.name().equals(field.getName())) {
                return field.getAnnotation(JsonProperty.class).value();
            }
        }
        return this.name();
    }
}
