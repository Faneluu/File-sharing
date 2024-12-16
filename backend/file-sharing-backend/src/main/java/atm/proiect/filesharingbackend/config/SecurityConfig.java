package atm.proiect.filesharingbackend.config;

import atm.proiect.filesharingbackend.util.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig configurează securitatea aplicației utilizând Spring Security.
 *
 * <p>Această clasă definește configurarea accesului, gestionarea sesiunilor, autentificarea
 * și mecanismele de tratare a excepțiilor legate de securitate.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Handler personalizat pentru gestionarea accesului interzis.
     */
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    /**
     * Configurează lanțul de filtre de securitate al aplicației.
     *
     * <p>Această metodă definește regulile de acces pentru diverse endpoint-uri,
     * dezactivează protecția CSRF, gestionează sesiunile în mod stateless și adaugă
     * un filtru de autorizare JWT. De asemenea, setează un handler personalizat pentru
     * erorile de acces interzis.</p>
     *
     * @param http obiectul HttpSecurity utilizat pentru configurarea securității
     * @param jwtAuthorizationFilter filtru personalizat pentru autorizare bazată pe JWT
     * @return un obiect {@link SecurityFilterChain} care definește lanțul de filtre de securitate
     * @throws Exception dacă apare o eroare în timpul configurării securității
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/user/register", "/api/v1/user/login").permitAll() // Permite accesul public pentru endpoint-urile cu prefixul "/user/"
                .requestMatchers("/api/v1/admin/login").permitAll() // Permite accesul public pentru login-ul de admin
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")// Restricționează accesul pentru utilizatorii cu rolul "ADMIN"
                .anyRequest().authenticated() // Orice altă cerere necesită autentificare
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Gestionează sesiunile în mod stateless
                .and()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) // Adaugă filtrul de JWT înaintea autentificării cu username și parolă
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler) // Setează handler-ul personalizat pentru erori de acces interzis
                .and()
                .cors();

        return http.build();
    }

    /**
     * Creează un bean pentru encoder-ul de parole.
     *
     * <p>Utilizează {@link BCryptPasswordEncoder} pentru a codifica parolele stocate în sistem.</p>
     *
     * @return un obiect {@link PasswordEncoder} bazat pe algoritmul BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
