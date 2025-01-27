package com.practiceBackend.practiceBackend.entity.ShareRepository.config;

import com.practiceBackend.practiceBackend.modules.security.filter.MyAuthenticationFilter;
import com.practiceBackend.practiceBackend.modules.security.service.CookieService;
import com.practiceBackend.practiceBackend.modules.security.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.GenericFilterBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final AuthenticationSuccessHandler oAuth2LoginSuccessHandler;
//    private SimpleUrlAuthenticationSuccessHandler oAuth2LoginSuccessHandler;

    private final AuthenticationProvider oAuth2AuthenticationProvider;
    private final SimpleUrlAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final MyAuthenticationFilter myauthenticationFilter;




    @Bean
    public AuthenticationManager authenticationManager(
    ) throws Exception {
        List<AuthenticationProvider> providers = Arrays.asList(oAuth2AuthenticationProvider);
        return new ProviderManager(providers);
    }
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .cors(c -> c.configurationSource(mucors()))
                .authorizeHttpRequests(a -> a.requestMatchers("/registers","/register","/logins","/mail/**","/users/{a}/{b}","/users"
                            ,   "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/oauth2/authorization/**",
                        "/login/oauth2/code/**").permitAll()

                );


        // oauth에 대한 처리
        http.
                oauth2Login(e -> e.loginPage("/login").successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()

                )
                .addFilterBefore(myauthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//CORS 설정은 클라이언트(특히 브라우저)가 원래 **동일 출처 정책(Same-Origin Policy)**에 따라 다른 서버의 리소스 요청을 차단하는 보안 메커니즘입니다.
//하지만 서버에서 CORS 설정을 통해 허용된 출처(origin)를 명시하면, 클라이언트는 다른 서버에서 받아온 정보도 신뢰할 수 있는 것으로 간주하고 이를 허용합니다.
    @Bean
    public CorsConfigurationSource mucors()
    {
        //이 클래스는 Cross-Origin Resource Sharing(CORS) 정책을 프로그래밍 방식으로 설정하고 구성할 수 있도록 도와줍니다.
        CorsConfiguration  cors = new CorsConfiguration();

        //`CorsConfigurationSource`는 URL 경로 패턴을 사용하여 요청에 대한 `CorsConfiguration`을 선택하는 데 사용됩니다.
        //패턴 매칭은 `PathMatcher`를 사용하거나 미리 파싱된 `PathPatterns`을 통해 수행할 수 있습니다. 두 방법의 문법은 대체로 유사하지만,
        // 후자는 웹 사용에 더 적합하고 더 효율적입니다.
        // 선택은 `String`으로 확인된 `lookupPath` 또는 파싱된 `RequestPath`의 존재 여부에 따라 달라지며,
        // 기본적으로 `PathMatcher`로 대체할 수 있지만, 이 대체 동작은 비활성화할 수도 있습니다.
        //자세한 내용은 `setAllowInitLookupPath(boolean)`을 참조하세요.

        //UrlBasedCorsConfigurationSource는 정책을 URL 경로별로 설정할 수 있는 기능을 제공합니다.
        //이를 통해 서버의 특정 URL에 대해 CORS 정책을 지정하거나, 모든 URL에 대해 동일한 CORS 정책을 적용할 수 있습니다.
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //addAllowedOrigin: 요청을 허용할 도메인(Origin)을 지정.
        cors.addAllowedOrigin("http://localhost:3000"); // 또는 "*"

        //addAllowedMethod: 허용할 HTTP 메서드를 지정
        cors.addAllowedMethod("*"); // 모든 메서드 허용

        //addAllowedHeader: 허용할 요청 헤더를 지정.
        cors.addAllowedHeader("*"); // 모든 헤더 허용

        //setAllowCredentials: 쿠키 및 인증 정보를 요청에 포함할지 여부를 설정.
        cors.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", cors);
        return source;

    }
}
