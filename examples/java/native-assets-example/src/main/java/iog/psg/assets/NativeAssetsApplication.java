package iog.psg.assets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import iog.psg.client.nativeassets.NativeAssetsApi;
import iog.psg.client.nativeassets.NativeAssetsBuilder;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NativeAssetsApplication implements WebMvcConfigurer  {

	@Value("${clientId}")
	private String clientId;
	@Value("${token}")
	private String token;

	@Bean
	public NativeAssetsApi nativeAssetsApi() {
		Config config = ConfigFactory.defaultApplication()
				.getConfig("akka.grpc.client.test")
				.resolve();
		return NativeAssetsBuilder.create(config).build();
	}
	@Bean
	public NativeAssetsMultisigApi nativeAssetsMultisigApi() {
		Config config = ConfigFactory.defaultApplication()
				.getConfig("akka.grpc.client.test")
				.resolve();
		return NativeAssetsBuilder.create(config).buildMultisig();
	}

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(NativeAssetsApplication.class, args);
	}

}
