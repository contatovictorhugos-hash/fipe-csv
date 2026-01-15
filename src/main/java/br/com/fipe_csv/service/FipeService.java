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

    private String resolverCodigoMarca(
            String tipo,
            String marca
    ) {
        String busca = marca.toLowerCase();

        return client.buscarDados(tipo).stream()
                // ordena pelos nomes mais parecidos primeiro
                .sorted((m1, m2) ->
                        score(m2.nome(), busca) - score(m1.nome(), busca)
                )

                // aceita código exato OU nome parecido (like)
                .filter(m ->
                        m.codigo().equals(marca) ||
                                m.nome().toLowerCase().contains(busca)
                )

                // pega o primeiro da lista ordenada
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Marca não encontrado: " + marca)
                )
                .codigo(); // pega o código do marca
    }

    private String resolverCodigoModelo(
            String tipo,
            String codigoMarca,
            String modelo
    ) {
        String busca = modelo.toLowerCase();

        return client.buscarModelos(tipo, codigoMarca).stream()

                // ordena pelos nomes mais parecidos primeiro
                .sorted((m1, m2) ->
                        score(m2.nome(), busca) - score(m1.nome(), busca)
                )

                // aceita código exato OU nome parecido (like)
                .filter(m ->
                        m.codigo().equals(modelo) ||
                                m.nome().toLowerCase().contains(busca)
                )

                // pega o primeiro da lista ordenada
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Modelo não encontrado: " + modelo)
                )
                .codigo(); // pega o código do modelo
    }

    // método para ordenar
    private int score(String nome, String busca) {
        nome = nome.toLowerCase();
        busca = busca.toLowerCase();

        if (nome.equals(busca)) {
            return 100;
        }

        if (nome.startsWith(busca)) {
            return 80;
        }

        if (nome.contains(busca)) {
            return 50;
        }

        return 0;
    }


}
