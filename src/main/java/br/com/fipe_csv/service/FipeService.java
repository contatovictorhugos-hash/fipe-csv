package br.com.fipe_csv.service;

import br.com.fipe_csv.DTO.DadosDTO;
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

        // 1️⃣ Nenhum filtro → marcas
        if (marca == null) {
            List<DadosDTO> marcas = client.buscarDados(tipo);

            marcas.forEach(m ->
                    System.out.println("Código: " + m.codigo() + " | Nome: " + m.nome())
            );

            return csvService.marcasToCsv(marcas);
        }

        // 2️⃣ Marca sem modelo → modelos
        if (modelo == null) {
            String codigoMarca = resolverCodigoMarca(tipo, marca);
            List<DadosDTO> modelos = client.buscarModelos(tipo, codigoMarca);

            modelos.forEach(m ->
                    System.out.println("Código: " + m.codigo() + " | Nome: " + m.nome())
            );

            return csvService.modelosToCsv(modelos);
        }

        // 3️⃣ Marca + modelo → anos
        String codigoMarca = resolverCodigoMarca(tipo, marca);
        String codigoModelo = resolverCodigoModelo(tipo, codigoMarca, modelo);

        List<DadosDTO> anosList = client.buscarAnos(tipo, codigoMarca, codigoModelo);

        anosList.forEach(m ->
                System.out.println("Código: " + m.codigo() + " | Nome: " + m.nome())
        );

        return csvService.anosToCsv(anosList);
    }

    // -------------------------
    // Métodos auxiliares
    // -------------------------

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
