package io.psg.nativeassets;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.psg.nativeassets.converters.PolicyConverter;
import iog.psg.client.nativeassets.NativeAssetsApi;
import iog.psg.client.nativeassets.NativeAssetsApiBuilder;
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

	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(NativeAssetsApplication.class, args);
	}

}
