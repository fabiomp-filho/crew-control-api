package br.com.crewcontrolapi.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PriorityEnum {
    @JsonProperty(value = "Alta")
    HIGH,
    @JsonProperty(value = "Normal")
    NORMAL,
    @JsonProperty(value = "Baixa")
    LOW
}
