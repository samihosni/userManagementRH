package com.example.userManagementRH.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static java.lang.invoke.VarHandle.AccessMode.GET;
import static javax.swing.text.html.FormSubmitEvent.MethodType.POST;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                                .requestMatchers(
                                        "/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configurations/ui",
                                        "/configurations/ui/security",
                                        "/swagger-ui/**",
                                        "/webjars/**",
                                        "/swagger-ui.html",
                                        "/complexe-sportif/all",
                                        "/complexe-sportif/show/**",
                                        "/pool/all",
                                        "/pool/show/**",
                                        "/salle-muscu/all",
                                        "/salle-muscu/show/**",
                                        "/terrainsfoot/all",
                                        "/terrainsfoot/show/**",
                                        "/terrainspadel/all",
                                        "/terrainspadel/show/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/employee/**").hasAuthority("employee:read")
                                .requestMatchers(HttpMethod.POST, "/api/employee/**").hasAuthority("employee:write")
                                .requestMatchers(HttpMethod.GET, "/api/admin/**").hasAuthority("admin:read")
                                .requestMatchers(HttpMethod.POST, "/api/admin/**").hasAuthority("admin:write")
                                .requestMatchers(HttpMethod.GET, "/api/hr/**").hasAuthority("hr:read")
                                .requestMatchers(HttpMethod.POST, "/api/hr/**").hasAuthority("hr:write")
                                .anyRequest().permitAll()
                        //authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
