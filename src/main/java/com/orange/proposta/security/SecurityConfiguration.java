package com.orange.proposta.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                {
                    try {
                        authorizeRequests
                                .antMatchers(HttpMethod.GET, "/propostas/**").hasAuthority("SCOPE_escopo-proposta")
                                .antMatchers(HttpMethod.POST, "/propostas/**").hasAuthority("SCOPE_escopo-proposta")
                                .antMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated()
                                .and().headers().frameOptions().sameOrigin()
                                .and().csrf().disable();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}

