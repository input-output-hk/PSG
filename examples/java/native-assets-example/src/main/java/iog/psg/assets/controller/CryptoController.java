package iog.psg.assets.controller;

import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.util.HexUtil;
import iog.psg.assets.service.MultisigNativeAssetService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
public class CryptoController {
    @Autowired
    private MultisigNativeAssetService multisigNativeAssetService;

    @PostMapping("/addresses/{vKey}")
    @ResponseStatus(HttpStatus.CREATED)
    public Address createAddress(@PathVariable String vKey) throws CborSerializationException {
        return multisigNativeAssetService.generateAddress(vKey);

    }

    @PostMapping("/keys")
    @ResponseStatus(HttpStatus.CREATED)
    public Keys createKeys() throws CborSerializationException {
        return multisigNativeAssetService.generateKeys();

    }

    @PostMapping("/vkey/{sKey}")
    @ResponseStatus(HttpStatus.CREATED)
    public VerificationKey createVKey(@PathVariable String sKey) throws CborSerializationException {
        return multisigNativeAssetService.generateVerKey(sKey);

    }

    @GetMapping("/vkey/hex")
    @ResponseStatus(HttpStatus.CREATED)
    public String hexVKey(@RequestBody VerificationKey vkey) throws CborSerializationException {
        return HexUtil.encodeHexString(vkey.getBytes());

    }

    @GetMapping("/skey/hex")
    @ResponseStatus(HttpStatus.CREATED)
    public String hexSKey(@RequestBody SecretKey skey) throws CborSerializationException {
        return HexUtil.encodeHexString(skey.getBytes());

    }
}
