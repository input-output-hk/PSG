package io.psg.nativeassets;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.psg.nativeassets.converters.PolicyConverter;
import iog.psg.client.nativeassets.NativeAssetsApi;
import iog.psg.client.nativeassets.NativeAssetsApiBuilder;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApiBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NativeAssetsApplication implements WebMvcConfigurer  {

	@Value("${clientId}")
	private String clientId;
	@Value("${token}")
	private String token;

	@Value("${host}")
	private String host;

	@Value("${port}")
	private Integer port;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new PolicyConverter());
	}

	@Bean
	public NativeAssetsApi nativeAssetsApi() {
		return NativeAssetsApiBuilder.create()
				.withClientId(clientId)
				.withApiKey(token)
				.withGrpcClientSettingsConfigName("test")
				.build();
	}

	@Bean
	public NativeAssetsMultisigApi multisigApi() {
		return NativeAssetsMultisigApiBuilder.create()
				.withClientId(clientId)
				.withApiKey(token)
				.withPort(port)
				.withHost(host)
				.withUseTls(true)
				.build();
	}

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(NativeAssetsApplication.class, args);
	}

}
