package io.psg.nativeassets.service;

import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.util.HexUtil;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import iog.psg.service.nativeassets.multisig.proto.v1.native_assets_multisig_service.AddressedNft;
import iog.psg.service.nativeassets.multisig.proto.v1.native_assets_multisig_service.UnsignedTxResponse;
import iog.psg.service.nativeassets.native_assets.Nft;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MultisigService {
    @Autowired
    private NativeAssetsMultisigApi multisigApi;

    public String getTransaction(String txId) {
        return multisigApi.getTx(txId)
                .result().tx().get().tx().get().tx();
    }

    public String listWitnesses(String txId) {
        return multisigApi.listWitnesses(txId).toProtoString();
    }

    public String addWitness(String txId, String keyHex, String signature) {
        VerificationKey key = null;
        try {
            key = VerificationKey.create(HexUtil.decodeHexString(keyHex));
        } catch (CborSerializationException ex) {
            log.info("Unable to convert key to CBOR");
        }

        return multisigApi.addWitness(txId, key, HexUtil.decodeHexString(signature)).result().toString();
    }

    public String sendTransaction(String txId) {
        return multisigApi.sendTransaction(txId).result().toString();
    }

    public String createMint(String policyId, String paymentAddress, String addressToMint, String assetName) {
        UnsignedTxResponse response = multisigApi.createMintTransaction(policyId, paymentAddress,
                AddressedNft.defaultInstance()
                        .withAddress(addressToMint)
                        .withNft(Nft.defaultInstance()
                                .withAssetName(assetName)
                        ));

        if (response.result().isProblem()) {
            return response.toString();
        }
        return String.format("id: %s, body: %s", response.getTx().txId(), response.getTx().tx());
    }
}
