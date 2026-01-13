package br.com.fipe_csv.DTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ModelosResponse(
        List<DadosDTO> modelos
) {}
