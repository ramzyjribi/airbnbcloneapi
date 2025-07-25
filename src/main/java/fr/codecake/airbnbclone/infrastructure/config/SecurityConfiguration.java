package fr.codecake.airbnbclone.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.HttpHeaders;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName(null);
        http.cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "api/tenant-listing/get-all-by-category").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/tenant-listing/get-one").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/tenant-listing/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/booking/check-availability").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/auth/get-authenticated-user").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/landlord-listing/create").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/landlord-listing/get-all").permitAll()
                        .requestMatchers(HttpMethod.GET, "assets/*").permitAll()
                        .anyRequest()
                        .permitAll())
                .csrf(csrf -> csrf.disable())
                .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    // (Optionnel) Génération d'un token ou traitement de l'utilisateur ici

                    // Redirection vers le frontend Angular
                    response.sendRedirect("https://myvoyage.onrender.com");
                })
        )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            System.out.println("ici out of grantedAuthority");
            authorities.forEach(grantedAuthority -> {
                if (grantedAuthority instanceof OidcUserAuthority oidcUserAuthority) {
                    System.out.println("ici grantedAuthority");
                    System.out.println(oidcUserAuthority);
                    oidcUserAuthority.getUserInfo().getClaims().keySet().forEach(s -> System.out.println("key : "+s));
                    System.out.println("roles :"+oidcUserAuthority.getUserInfo().getClaims().get("https://codecake.fr/roles"));
                    grantedAuthorities
                            .addAll(SecurityUtils.extractAuthorityFromClaims(oidcUserAuthority.getUserInfo().getClaims()));
                }
            });
            return grantedAuthorities;
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://myvoyage.onrender.com")); // autorise Angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // important si tu utilises les cookies ou le header Authorization

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // applique à toutes les routes
        return source;
    }

    /*@Bean
    public FilterRegistrationBean<Filter> sameSiteCookieFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter((request, response, chain) -> {
            chain.doFilter(request, response);

            if (response instanceof HttpServletResponse res) {
                Collection<String> headers = res.getHeaders(HttpHeaders.SET_COOKIE);
                System.out.println("==> Filter exécuté, cookies existants : " + headers);

                if (!headers.isEmpty()) {
                    res.setHeader(HttpHeaders.SET_COOKIE, null); // efface tous les cookies envoyés
                }

                for (String header : headers) {
                    String result = header;

                    if (!header.toLowerCase().contains("secure")) {
                        result += "; Secure";
                    }

                    if (!header.toLowerCase().contains("samesite")) {
                        result += "; SameSite=None";
                    }

                    res.addHeader(HttpHeaders.SET_COOKIE, result); // renvoie uniquement les versions corrigées
                    System.out.println("==> Filter exécuté, cookies apres ajout : " + result);
                }
            }
        });
        registrationBean.setOrder(1);
        return registrationBean;
    }*/

}
