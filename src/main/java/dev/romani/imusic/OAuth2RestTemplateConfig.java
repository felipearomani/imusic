package dev.romani.imusic;

import dev.romani.imusic.music.SpotifyResourceURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
public class OAuth2RestTemplateConfig {

    @Value("${rest.api.spotify.client-id}")
    private String spotifyClientId;

    @Value("${rest.api.spotify.client-secret}")
    private String spotifyClientSecret;

    @Value("${rest.api.spotify.token-uri}")
    private String spotifyTokenURI;

    private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails() {
        var resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setClientId(spotifyClientId);
        resourceDetails.setClientSecret(spotifyClientSecret);
        resourceDetails.setGrantType("client_credentials");
        resourceDetails.setAccessTokenUri(spotifyTokenURI);
        return resourceDetails;
    }

    @Bean
    public OAuth2RestTemplate spotifyOauth2RestTemplate() {
        return new OAuth2RestTemplate(oAuth2ProtectedResourceDetails());
    }

    @Bean
    public SpotifyResourceURI spotifyResourceURI() {
        return new SpotifyResourceURI();
    }

}
