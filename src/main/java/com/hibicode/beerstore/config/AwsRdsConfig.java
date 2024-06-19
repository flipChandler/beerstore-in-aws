package com.hibicode.beerstore.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.hibicode.beerstore.dto.AwsRdsSecret;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Configuration
public class AwsRdsConfig {
	
	private Gson gson = new Gson();
	
	@Value("${aws.rds.host}")
	private String host;
	
	@Value("${aws.access-key}")
	private String accessKey;
	
	@Value("${aws.secret-key}")
	private String secretKey;
	
	@Bean
	public DataSource dataSource() {
		AwsRdsSecret rdsSecret = getSecret();
		return DataSourceBuilder.create()
				.username(rdsSecret.getUsername())
				.password(rdsSecret.getPassword())
				.driverClassName("org.postgresql.Driver")
				.url("jdbc:postgresql://" + host + "/beerstoredb")
				// jdbc:postgresql://hibicode-beerstore-rds.chy6ke26akam.us-east-1.rds.amazonaws.com:5432/beerstoredb
				.build();
	}
	
	private AwsRdsSecret getSecret() {

	    String secretName = "db-creds-hibicode";
	    Region region = Region.of("us-east-1");
	    
	    AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
	    StaticCredentialsProvider provider = StaticCredentialsProvider.create(awsCreds);
	    
	    // Create a Secrets Manager client
	    SecretsManagerClient client = SecretsManagerClient.builder()
	    		.credentialsProvider(provider)
	            .region(region)
	            .build();

	    GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
	            .secretId(secretName)
	            .build();

	    GetSecretValueResponse getSecretValueResponse;

	    try {
	        getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
	    } catch (Exception e) {
	        // For a list of exceptions thrown, see
	        // https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
	        throw e;
	    }

	    String secret = getSecretValueResponse.secretString();	    

	    return gson.fromJson(secret, AwsRdsSecret.class);
	}
}
