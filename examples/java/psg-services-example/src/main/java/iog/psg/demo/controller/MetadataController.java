package iog.psg.demo.controller;

import iog.psg.demo.service.DataService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@RestController
@Log
public class MetadataController {
    @Autowired
    DataService dataService;

    @GetMapping("/listmetadata")
    public ResponseEntity<ResponseBodyEmitter> list() {
        ResponseBodyEmitter emitter = dataService.getMetadata();
        return ResponseEntity.ok()
                .body(emitter);
    }
    @PostMapping("/submitmetadata")
    public ResponseEntity<ResponseBodyEmitter> submit(@RequestParam("metadata") String metadata) {
        ResponseBodyEmitter emitter = dataService.submitMetadata(metadata);
        return ResponseEntity.ok()
                .body(emitter);
    }
}
