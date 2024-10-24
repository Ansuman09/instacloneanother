package com.api.instaclone.security;


// import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.api.instaclone.security.filter.AuthenticationFilter;
import com.api.instaclone.security.filter.JWTAuthorizationFilter;
import com.api.instaclone.security.manager.CustomAuthenticationManager;
import com.api.instaclone.service.RoleService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    CustomAuthenticationManager authenticationManager;

    @Autowired
    RoleService roleService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager,roleService);
        authenticationFilter.setFilterProcessesUrl("/authenticate");
        http
            // .cors(cors->cors.disable())
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
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000"); // Allow requests from this origin
        config.addAllowedHeader("*"); // Allow all headers
        config.addAllowedMethod("*"); // Allow all HTTP methods
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
} 
}
