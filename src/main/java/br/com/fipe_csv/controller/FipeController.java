package br.com.fipe_csv.controller;

import br.com.fipe_csv.service.FipeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=fipe.csv")
                .body(csv);
    }
}
