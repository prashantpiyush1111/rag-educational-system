package com.education.rag.security.config;

import java.util.List;
import com.education.rag.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
 private final JwtFilter jwtFilter;
 public SecurityConfig(JwtFilter jwtFilter){this.jwtFilter=jwtFilter;}
 @Bean public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
  http.csrf(csrf->csrf.disable()).cors(cors->cors.configurationSource(corsConfigurationSource()))
   .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(auth->auth.requestMatchers("/api/auth/register","/api/auth/login","/actuator/health","/actuator/prometheus").permitAll().anyRequest().authenticated())
   .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
  return http.build();
 }
 @Bean public CorsConfigurationSource corsConfigurationSource(){
  CorsConfiguration c=new CorsConfiguration();
  c.setAllowedOriginPatterns(List.of("http://localhost:*","http://127.0.0.1:*"));
  c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
  c.setAllowedHeaders(List.of("Authorization","Content-Type","X-Requested-With"));
  c.setAllowCredentials(true);
  UrlBasedCorsConfigurationSource src=new UrlBasedCorsConfigurationSource();src.registerCorsConfiguration("/**",c);return src;
 }
 @Bean public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
}
