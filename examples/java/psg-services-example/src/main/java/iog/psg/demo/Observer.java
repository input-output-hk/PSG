package iog.psg.demo;


import io.grpc.stub.StreamObserver;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.function.Function;

public class Observer {

    public static <E,R> StreamObserver<E> observer(ResponseBodyEmitter emitter, Function<E, R> converter) {
        return new StreamObserver<>() {
            @Override
            public void onNext(E response) {
                try {
                    emitter.send(converter.apply(response).toString() + "\n", MediaType.TEXT_PLAIN);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(Throwable ex) {
                emitter.completeWithError(ex);
            }

            @Override
            public void onCompleted() {
                emitter.complete();
            }
        };
    }
}
