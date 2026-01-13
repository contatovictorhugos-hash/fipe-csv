package br.com.fipe_csv.client;

import br.com.fipe_csv.DTO.DadosDTO;
import br.com.fipe_csv.DTO.ModelosResponse;
import br.com.fipe_csv.webClientConfig.WebClientConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class Client {

    private final WebClient webClient;

    public Client(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<DadosDTO> buscarDados(String tipoVeiculo) {
        return webClient.get()
                .uri("/{tipo}/marcas", tipoVeiculo)
                .retrieve()
                .bodyToFlux(DadosDTO.class)
                .collectList()
                .block();
    }

    public List<DadosDTO> buscarModelos(String tipo, String codigoMarca) {
        return webClient.get()
                .uri("/{tipo}/marcas/{marca}/modelos", tipo, codigoMarca)
                .retrieve()
                .bodyToMono(ModelosResponse.class)
                .map(ModelosResponse::modelos)
                .block();
    }

    public List<DadosDTO> buscarAnos(String tipo, String codigoMarca, String codigoModelo) {
        return webClient.get()
                .uri("/{tipo}/marcas/{marca}/modelos/{modelo}/anos",
                        tipo, codigoMarca, codigoModelo)
                .retrieve()
                .bodyToFlux(DadosDTO.class)
                .collectList()
                .block();
    }

}
