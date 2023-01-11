package iog.psg.demo.controller;

import iog.psg.demo.model.StoreResult;
import iog.psg.demo.service.StoreService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

@RestController
@Log
public class StoreController {
    @Autowired
    StoreService storeService;

    @PostMapping("/storeAws")
    public ResponseEntity<ResponseBodyEmitter> storeFileAtAws(@RequestParam("path") String path, @RequestParam("content") String fileContent) {
        ResponseBodyEmitter emitter = storeService.storeAtAws(path, fileContent);
        return ResponseEntity.ok()
                .body(emitter);
    }

    @PostMapping("/storeIpfs")
    public  ResponseEntity<ResponseBodyEmitter> storeFileAtIpfs(@RequestParam("host") String host, @RequestParam("port") Integer port, @RequestParam("content") String fileContent) {
        ResponseBodyEmitter emitter = storeService.storeAtIpfs(host, port, fileContent);
        return ResponseEntity.ok()
                .body(emitter);
    }

}
