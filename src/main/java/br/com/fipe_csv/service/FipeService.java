package br.com.fipe_csv.service;

import br.com.fipe_csv.DTO.DadosDTO;
import br.com.fipe_csv.DTO.DetalhesDTO;
import br.com.fipe_csv.client.Client;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FipeService {

    private final Client client;
    private final CsvService csvService;

    public FipeService(Client client, CsvService csvService) {
        this.client = client;
        this.csvService = csvService;
    }

    public byte[] consultar(String tipo, String marca, String modelo, boolean anos) {

        // Nenhum filtro - marcas
        if (marca == null) {
            List<DadosDTO> marcas = client.buscarDados(tipo);

            return csvService.marcasToCsv(marcas);

        }else if (modelo == null) { // Marca sem modelo - modelos
            String codigoMarca = resolverCodigoMarca(tipo, marca);
            List<DadosDTO> modelos = client.buscarModelos(tipo, codigoMarca);

            return csvService.modelosToCsv(modelos);

        } else if (!anos) { // - Marca + modelo - anos
            String codigoMarca = resolverCodigoMarca(tipo, marca);
            String codigoModelo = resolverCodigoModelo(tipo, codigoMarca, modelo);

            List<DadosDTO> anosList = client.buscarAnos(tipo, codigoMarca, codigoModelo);

            return csvService.anosToCsv(anosList);

        }
        else {

            String codigoMarca = resolverCodigoMarca(tipo, marca);
            String codigoModelo = resolverCodigoModelo(tipo, codigoMarca, modelo);

            List<DetalhesDTO> detalhes = client.buscarDetalhesPorAno(tipo, codigoMarca, codigoModelo);
            return csvService.gerarCsvDatalhes(detalhes);
        }
    }

    private String resolverCodigoMarca(String tipo, String marca) {
        return client.buscarDados(tipo).stream()
                .filter(m ->
                        m.nome().equalsIgnoreCase(marca) ||
                                m.codigo().equals(marca)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Marca não encontrada"))
                .codigo();
    }

    private String resolverCodigoModelo(
            String tipo,
            String codigoMarca,
            String modelo
    ) {
        return client.buscarModelos(tipo, codigoMarca).stream()
                .filter(m ->
                        m.nome().equalsIgnoreCase(modelo) ||
                                m.codigo().equals(modelo)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Modelo não encontrado"))
                .codigo();
    }

}
