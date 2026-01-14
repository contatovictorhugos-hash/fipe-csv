package br.com.fipe_csv.DTO;

public record DetalhesDTO(String codigoFipe,
                          String valor,
                          String marca,
                          String modelo,
                          String anoModelo,
                          String combustivel,
                          String mesReferencia
) {}
