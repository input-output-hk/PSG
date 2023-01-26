package iog.psg.demo.service;

import iog.psg.demo.Observer;
import iog.psg.demo.model.StoreResult;
import iog.psg.service.storeandhash.client.AwsConf;
import iog.psg.service.storeandhash.client.StoreAndHash;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.nio.charset.StandardCharsets;

@Service
@Log
public class StoreService {
    @Value("${s3.key}")
    private String key;
    @Value("${s3.secret}")
    private String secret;
    @Value("${s3.bucket}")
    private String bucket;
    @Value("${s3.region}")
    private String region;

    @Autowired
    private StoreAndHash storeAndHash;

    public ResponseBodyEmitter storeAtAws(String path, String fileContent) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        storeAndHash.storeAndHashHttp(path, fileContent.getBytes(StandardCharsets.UTF_8), awsConf(), Observer.observer(emitter, res ->
                StoreResult.builder()
                        .hash(res.getHash().hashBase64())
                        .errorCode(res.getProblem().code())
                        .errorMessage(res.getProblem().msg())
                        .url(res.getUrl())
                        .build())
        );
        return emitter;
    }

    public ResponseBodyEmitter storeAtIpfs(String host, Integer port, String fileContent) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        storeAndHash.storeAndHashIpfs(host, port, fileContent, Observer.observer(emitter, res ->
                StoreResult.builder()
                        .hash(res.getHash().hashBase64())
                        .errorCode(res.getProblem().code())
                        .errorMessage(res.getProblem().msg())
                        .url(res.getUrl())
                        .build())

        );
        return emitter;
    }


    private AwsConf awsConf() {
        return new AwsConf(key, secret, bucket, region);
    }


}
