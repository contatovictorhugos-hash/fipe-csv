package br.com.fipe_csv.client;

import br.com.fipe_csv.DTO.DadosDTO;
import br.com.fipe_csv.DTO.DetalhesDTO;
import br.com.fipe_csv.DTO.ModelosResponse;
import br.com.fipe_csv.webClientConfig.WebClientConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

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

    public DetalhesDTO buscarDetalhe(
            String tipo,
            String codigoMarca,
            String codigoModelo,
            String codigoAno
    ) {
        return webClient.get()
                .uri("/{tipo}/marcas/{codigoMarca}/modelos/{codigoModelo}/anos/{codigoAno}",
                        tipo, codigoMarca, codigoModelo, codigoAno)
                .retrieve()
                .bodyToMono(DetalhesDTO.class)
                .block();  // bloqueia para pegar o resultado
    }

    public List<DetalhesDTO> buscarDetalhesPorAno(String tipo, String codigoMarca, String codigoModelo) {
        // Primeiro pega a lista de anos
        List<DadosDTO> anos = buscarAnos(tipo, codigoMarca, codigoModelo);

        // Para cada ano, chama o endpoint detalhado
        return anos.stream()
                .map(ano -> {
                    DetalhesDTO detalhe = buscarDetalhe(tipo, codigoMarca, codigoModelo, ano.codigo());
                    return detalhe;
                })
                .collect(Collectors.toList());
    }

}
