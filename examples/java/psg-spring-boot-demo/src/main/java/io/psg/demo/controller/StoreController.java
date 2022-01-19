package io.psg.demo.controller;

import io.psg.demo.model.StoreResult;
import io.psg.demo.service.StoreService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log
public class StoreController {
    @Autowired
    StoreService storeService;

    @PostMapping("/storeAws")
    public void storeFileAtAws(@RequestParam("path") String path, @RequestParam("content") String fileContent) {
        storeService.storeAtAws(path, fileContent);
    }

    @PostMapping("/storeIpfs")
    public void storeFileAtIpfs(@RequestParam("host") String host, @RequestParam("port") Integer port, @RequestParam("content") String fileContent) {
        storeService.storeAtIpfs(host, port, fileContent);
    }

    @GetMapping("/store/result")
    public List<StoreResult> result() {
        return storeService.getResults();
    }
}
