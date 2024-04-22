package br.com.crewcontrolapi.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskItemStatusEnum {

    @JsonProperty(value = "Concluído")
    COMPLETED,
    @JsonProperty(value = "Não concluído")
    NOT_COMPLETED

}
