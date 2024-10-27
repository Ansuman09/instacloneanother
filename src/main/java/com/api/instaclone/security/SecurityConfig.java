package com.api.instaclone.security;


import java.util.Arrays;
import java.util.List;

// import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.api.instaclone.security.filter.AuthenticationFilter;
import com.api.instaclone.security.filter.JWTAuthorizationFilter;
import com.api.instaclone.security.manager.CustomAuthenticationManager;
import com.api.instaclone.service.RoleService;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    CustomAuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @Value("${myapp.deployment.frontend.dns}")
    private String allowedOrigins;

    @PostConstruct
    public void init() {
        System.out.println("CORS Allowed Origin: " + allowedOrigins);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager,roleService);
        authenticationFilter.setFilterProcessesUrl("/authenticate");
        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .headers(headers-> headers.frameOptions(fr->fr.disable()))
            .csrf(csrf-> csrf.disable())
            .authorizeHttpRequests((authorizeHttpRequests)->
                        authorizeHttpRequests
                            // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                            .requestMatchers("/userinfo/getuserid/forlogin/***").permitAll()
                            .requestMatchers(HttpMethod.POST,"/user/register").permitAll()
                            .requestMatchers("/posts/admin/api/").hasRole("ADMIN")
                            .anyRequest().authenticated())
                            .addFilter(authenticationFilter)
                            .addFilterAfter(new JWTAuthorizationFilter(),AuthenticationFilter.class)
                            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                                        
        return http.build();           
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        config.setAllowedOrigins(origins); // Allow requestst from this origin
        config.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
        config.setAllowedMethods(Arrays.asList("*")); // Allow all methods
        config.setAllowCredentials(true); // Allow all HTTP methods
        source.registerCorsConfiguration("/**", config);
        return source;
} 
}
