package br.com.fipe_csv.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DetalhesDTO(
        @JsonAlias({"CodigoFipe"}) String codigoFipe,
        @JsonAlias({"Valor"}) String valor,
        @JsonAlias({"Marca"}) String marca,
        @JsonAlias({"Modelo"}) String modelo,
        @JsonAlias({"AnoModelo"}) String anoModelo,
        @JsonAlias({"Combustivel"}) String combustivel,
        @JsonAlias({"MesReferencia"}) String mesReferencia
) {}
