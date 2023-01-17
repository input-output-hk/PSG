package iog.psg.demo;

import io.grpc.CallOptions;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import iog.psg.service.metadata.client.Metadata;
import iog.psg.service.metadata.client.MetadataBuilder;
import iog.psg.service.storeandhash.client.StoreAndHash;
import iog.psg.service.storeandhash.client.StoreAndHashBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class PsgBootApplication implements WebMvcConfigurer {
    @Value("${clientId}")
    private String clientId;
    @Value("${token}")
    private String token;

    @Value("${host}")
    private String host;
    @Value("${port}")
    private Integer port;

    @Bean
    public Metadata metadata() {
//        ManagedChannel managedChannel  = ManagedChannelBuilder.forAddress(host, port)
//                .usePlaintext()
//                .build();
        return  MetadataBuilder.create(token, clientId)
//                .withManagedChannel(managedChannel)
                .build();
    }
    @Bean
    public StoreAndHash storeAndHash() {

        return StoreAndHashBuilder.create(token, clientId)
                .withHost("dev.iog.services")
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(PsgBootApplication.class, args);
    }
}
