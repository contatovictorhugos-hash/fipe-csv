package br.com.fipe_csv.service;

import br.com.fipe_csv.DTO.DadosDTO;
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

    private String escape(String valor) {
        if (valor == null) return "";

        if (valor.contains(",") || valor.contains("\"")) {
            valor = valor.replace("\"", "\"\"");
            return "\"" + valor + "\"";
        }

        return valor;
    }


}
