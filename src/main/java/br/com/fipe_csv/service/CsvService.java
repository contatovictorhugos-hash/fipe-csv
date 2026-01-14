package br.com.fipe_csv.service;

import br.com.fipe_csv.DTO.DadosDTO;
import br.com.fipe_csv.DTO.DetalhesDTO;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvService {

    public byte[] marcasToCsv(List<DadosDTO> dados) {
        return gerarCsv(dados, "codigo,nome");
    }

    public byte[] modelosToCsv(List<DadosDTO> dados) {
        return gerarCsv(dados, "codigo,nome");
    }

    public byte[] anosToCsv(List<DadosDTO> dados) {
        return gerarCsv(dados, "codigo,nome");
    }

    public byte[] gerarCsvDatalhes(List<DetalhesDTO> dados) {
        return gerarCsvDetalhado(dados, "codigoFipe,valor,marca,modelo,anoModelo,combustivel,mesReferencia");
    }

    private byte[] gerarCsv(List<DadosDTO> dados, String header) {

        StringBuilder csv = new StringBuilder();
        csv.append(header).append("\n");

        for (DadosDTO d : dados) {
            csv.append(escape(d.codigo()))
                    .append(",")
                    .append(escape(d.nome()))
                    .append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private byte[] gerarCsvDetalhado(List<DetalhesDTO> dados, String header) {

        StringBuilder csv = new StringBuilder();
        csv.append(header).append("\n");

        for (DetalhesDTO d : dados) {
            csv.append(escape(d.codigoFipe())).append(",")
                    .append(escape(d.valor())).append(",")
                    .append(escape(d.marca())).append(",")
                    .append(escape(d.modelo())).append(",")
                    .append(escape(d.anoModelo())).append(",")
                    .append(escape(d.combustivel())).append(",")
                    .append(escape(d.mesReferencia()))
                    .append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String escape(String valor) {
        if (valor == null) return "";

        if (valor.contains(",") || valor.contains("\"")) {
            valor = valor.replace("\"", "\"\"");
            return "\"" + valor + "\"";
        }

        return valor;
    }


}
