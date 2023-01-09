import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import iog.psg.client.nativeassets.NativeAssetsMultisigApiBuilder;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import iog.psg.service.nativeassets.multisig.proto.v1.native_assets_multisig_service.AddressedNft;
import iog.psg.service.nativeassets.multisig.proto.v1.native_assets_multisig_service.AddressedNft$;
import iog.psg.service.nativeassets.native_assets.Nft;
import iog.psg.service.nativeassets.native_assets.Nft$;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MultiSigNativeAssets {
    public static void main(String[] args) throws CborSerializationException, InterruptedException, ExecutionException {
        NativeAssetsMultisigApi api = NativeAssetsMultisigApiBuilder.create()
                .withClientId("CLIENT_ID")
                .withApiKey("API_KEY")
                .withHost("localhost")
                .withPort(2000)
                .withUseTls(false)
                .build();

        Keys keysp = api.generateKeys();
        Keys keys1 = api.generateKeys();
        Keys keys2 = api.generateKeys();
        Address payAddress = api.generateAddress(keysp.getVkey(), Networks.preprod());
        Address targetAddress = api.generateAddress(keysp.getVkey(), Networks.preprod());
        String assetName = "assetName";
        Nft nft = Nft$.MODULE$.defaultInstance()
                .withName(assetName)
                .withAssetName(assetName);

        AddressedNft addressedNft = AddressedNft$.MODULE$.defaultInstance()
                .withNft(nft)
                .withAddress(targetAddress.getAddress());

        String policyId = api.createPolicy("mytestpolicy2", Arrays.asList(keysp.getVkey()))
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy().policyId();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                })
                .get();
//        Thread.sleep(20000);
        System.out.printf(policyId);
        String txId = api.createMintTransaction(policyId, payAddress.getAddress(), addressedNft)
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                })
                .get();
//
        System.out.println(txId);


    }
}
