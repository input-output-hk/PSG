package iog.psg.assets;

import iog.psg.client.nativeassets.NativeAssetsApi;
import iog.psg.client.nativeassets.NativeAssetsBuilder;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NativeAssetsApplication implements WebMvcConfigurer {

    @Value("${clientId}")
    private String clientId;
    @Value("${token}")
    private String token;

    @Bean
    public NativeAssetsApi nativeAssetsApi() {
        return NativeAssetsBuilder.create(token, clientId).build();
    }

    @Bean
    public NativeAssetsMultisigApi nativeAssetsMultisigApi() {
        return NativeAssetsBuilder.create(token, clientId).buildMultisig();
    }

    public static void main(String[] args) {
        SpringApplication.run(NativeAssetsApplication.class, args);
    }

}
