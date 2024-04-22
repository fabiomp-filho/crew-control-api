package br.com.crewcontrolapi.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RoleEnum {
    @JsonProperty(value = "Administrador")
    ADMINISTRATOR,
    @JsonProperty(value = "Líder")
    LEADER,
    @JsonProperty(value = "Colaborador")
    COLLABORATOR


}

