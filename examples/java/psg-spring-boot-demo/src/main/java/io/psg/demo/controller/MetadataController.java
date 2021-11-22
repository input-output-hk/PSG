package io.psg.demo.controller;

import io.psg.demo.model.TransactionStatus;
import io.psg.demo.service.DataService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log
public class MetadataController {
    @Autowired
    DataService dataService;

    @GetMapping("/listmetadata")
    public List<TransactionStatus> list() {
        return dataService.getMetadata();
    }

    @PostMapping("/submitmetadata")
    public List<TransactionStatus> submit(@RequestParam("metadata") String metadata) {
        return dataService.submitMetadata(metadata);
    }
}
