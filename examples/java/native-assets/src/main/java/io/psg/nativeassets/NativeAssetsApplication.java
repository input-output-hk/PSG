package io.psg.nativeassets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.psg.nativeassets.converters.PolicyConverter;
import io.psg.nativeassets.model.Nft;
import io.psg.nativeassets.model.NftFile;
import iog.psg.client.nativeassets.NativeAssetsApi;
import iog.psg.client.nativeassets.NativeAssetsApiBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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
		Nft nft = Nft.builder()
				.name("daco")
				.assetName("assetNameDaco")
				.image(List.of("image1","image2"))
				.mediaType("mediaType1")
				.description(List.of("desc1","desc2"))
				.nftFiles(List.of(NftFile.builder().name("nftFile1").mediaType("mediaType1").srcs(List.of("src1","src2")).build()))
				.build();

		ObjectMapper mapper = new ObjectMapper();


		String serialized = mapper.writeValueAsString(nft);
		System.out.println(serialized);
		System.out.println(mapper.readValue(serialized, Nft.class));
		SpringApplication.run(NativeAssetsApplication.class, args);
	}

}
