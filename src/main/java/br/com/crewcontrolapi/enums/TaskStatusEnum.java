package br.com.crewcontrolapi.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    COMPLETED
}
