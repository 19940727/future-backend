package com.xuzekun.backend.config;

import com.xuzekun.backend.annotation.PermitApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         ???????????????????????????
         */
        Set<String> permitUrls = new HashSet<>();

        //????????????????????????????????????
        //permitUrls.add("/api/**");

        /*
        ?????????@PermitAllRequest???????????????????????????????????????????????????
         */
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            // ???IgnoreAuth???????????????????????????
            if (handlerMethod.getMethodAnnotation(PermitApi.class) != null) {
                // ????????????????????????????????????
                for (RequestMethod method : requestMappingInfo.getMethodsCondition().getMethods()) {
                    PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
                    if (patternsCondition == null) {
                        continue;
                    }
                    Set<String> patterns = patternsCondition.getPatterns();
                    permitUrls.addAll(patterns);
                }
            }
        }

        http.authorizeRequests().antMatchers(permitUrls.toArray(String[]::new)).permitAll().anyRequest().authenticated().and()
                // ?????????session
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors().and().csrf().disable().httpBasic().disable();
    }

}
