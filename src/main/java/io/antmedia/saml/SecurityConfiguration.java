package io.antmedia.saml;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	private String metadataUrl;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
				.saml2Login(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
		RelyingPartyRegistration relyingPartyRegistration = RelyingPartyRegistrations
				.fromMetadataLocation(metadataUrl)
				.registrationId("okta")
				.build();

		return new InMemoryRelyingPartyRegistrationRepository(relyingPartyRegistration);
	}

	public String getMetadataUrl() {
		return metadataUrl;
	}

	public void setMetadataUrl(String metadataUrl) {
		this.metadataUrl = metadataUrl;
	}
}