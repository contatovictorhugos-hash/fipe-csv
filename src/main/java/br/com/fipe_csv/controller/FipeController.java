package br.com.fipe_csv.controller;

import br.com.fipe_csv.service.FipeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/fipe")
public class FipeController {

    private final FipeService service;

    public FipeController(FipeService service) {
        this.service = service;
    }

    @GetMapping(value = "/{tipo}", produces = "text/csv")
    public ResponseEntity<byte[]> consultar(
            @PathVariable String tipo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(defaultValue = "false") boolean anos
    ) {
        byte[] csv = service.consultar(tipo, marca, modelo, anos);

        String dataHora = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        String filename = "fipe_" + dataHora + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename="+filename)
                .body(csv);
    }
}
