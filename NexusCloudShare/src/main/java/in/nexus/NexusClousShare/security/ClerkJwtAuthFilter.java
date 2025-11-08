package in.nexus.NexusClousShare.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ClerkJwtAuthFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ClerkJwtAuthFilter.class);

    @Value("${clerk.issuer}")
    private String clerkIssuer;

    private final ClerkJwksProvider jwksProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // For webhook/public/download/health endpoints, skip JWT validation and continue the filter chain
        String uri = request.getRequestURI();
        if (uri.contains("/webhooks") || uri.contains("/public") || uri.contains("/download") || uri.contains("/health")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank()) {
            log.debug("Missing Authorization header for request {}", uri);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing");
            return;
        }

        try {
            // Accept both "Bearer <token>" and raw token in Authorization header to be more tolerant for various frontends.
            String token;
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7).trim();
            } else {
                token = authHeader.trim();
            }

            String[] chunks = token.split("\\.");
            if (chunks.length < 3) {
                log.warn("Invalid JWT token format for request {}", uri);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token format");
                return;
            }

            String headerJson = new String(Base64.getUrlDecoder().decode(chunks[0]));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode headerNode = mapper.readTree(headerJson);

            if (!headerNode.has("kid")) {
                log.warn("Token header missing kid for request {}", uri);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token header is missing kid");
                return;
            }

            String kid = headerNode.get("kid").asText();

            PublicKey publicKey = jwksProvider.getPublicKey(kid);
            if (publicKey == null) {
                log.error("Public key not found for kid={} (request={})", kid, uri);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unable to obtain public key for token");
                return;
            }

            // verify the token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .setAllowedClockSkewSeconds(60)
                    .requireIssuer(clerkIssuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String clerkId = claims.getSubject();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(clerkId, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            log.warn("JWT validation failed for request {}: {}", uri, e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token: " + e.getMessage());
            return;
        }

        // Continue the filter chain after token handling so downstream exceptions aren't caught here.
        filterChain.doFilter(request, response);

    }
}
