package io.psg.nativeassets.controller;

import io.psg.nativeassets.service.MultisigService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
public class MultisigController {
    @Autowired
    private MultisigService multisigService;

    @GetMapping("/multisig/transactions/{transactionId}")
    public String getTransaction(@PathVariable String transactionId) {
        return multisigService.getTransaction(transactionId);
    }

    @GetMapping("/multisig/witnesses/{transactionId}")
    public String listWitnesses(@PathVariable String transactionId) {
        return multisigService.listWitnesses(transactionId);
    }

    @PostMapping("/multisig/transactions/send/{transactionId}")
    public String sendTransaction(@PathVariable String transactionId) {
        return multisigService.sendTransaction(transactionId);
    }

    @PostMapping("/multisig/witnesses/add")
    public String addWitness(@RequestParam String id, @RequestParam String key, @RequestParam String signature) {
        return multisigService.addWitness(id, key, signature);
    }

    @PostMapping("/multisig/mint/create")
    public String prepareMint(@RequestParam String policyId,
                              @RequestParam String paymentAddress,
                              @RequestParam String addressToMint,
                              @RequestParam String assetName) {
        return multisigService.createMint(policyId, paymentAddress, addressToMint, assetName);
    }
}
