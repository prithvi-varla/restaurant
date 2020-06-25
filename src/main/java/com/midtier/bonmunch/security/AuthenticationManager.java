package com.midtier.bonmunch.security;

import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.security.model.Role;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.security.model.Role;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author prithvi
 */
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        String username, companyId, userId;
        ApiPrincipal apiPrincipal = null;
        try {
            username = jwtUtil.getUsernameFromToken(authToken);
            companyId = jwtUtil.getCompanyIdFromToken(authToken);
            userId = jwtUtil.getUserIdFromToken(authToken);
            apiPrincipal = ApiPrincipal.builder()
                                       .userName(username)
                                       .companyId(UUID.fromString(companyId))
                                       .userId(UUID.fromString(userId))
                                       .build();
        } catch (Exception e) {
            username = null;
        }
        if (username != null && jwtUtil.validateToken(authToken)) {
            Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
            List<String> rolesMap = claims.get("role", List.class);
            List<Role> roles = new ArrayList<>();
            for (String rolemap : rolesMap) {
                roles.add(Role.valueOf(rolemap));
            }
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    apiPrincipal,
                    null,
                    roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList())
            );
            return Mono.just(auth);
        } else {
            return Mono.empty();
        }
    }
}
