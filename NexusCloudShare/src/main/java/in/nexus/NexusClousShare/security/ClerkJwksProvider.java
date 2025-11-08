package in.nexus.NexusClousShare.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClerkJwksProvider {

    private static final Logger log = LoggerFactory.getLogger(ClerkJwksProvider.class);

    @Value("${clerk.jwks-url}")
    private String jwksUrl;

    private final Map<String, PublicKey> keyCache = new HashMap<>();
    private long lastFetchTime = 0;
    private static final long CACHE_TTL = 3600000; //1 hour

    public PublicKey getPublicKey(String kid) throws Exception{
        if (keyCache.containsKey(kid) && System.currentTimeMillis() - lastFetchTime < CACHE_TTL) {
            return keyCache.get(kid);
        }

        refreshKeys();
        return keyCache.get(kid);
    }

    private void refreshKeys() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        URL url = new URL(jwksUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestProperty("User-Agent", "NexusClousShare/1.0");
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            String msg = "Unable to fetch JWKS, status=" + status;
            log.error(msg);
            throw new RuntimeException(msg);
        }

        try (InputStream is = conn.getInputStream()) {
            JsonNode jwks = mapper.readTree(is);

            JsonNode keys = jwks.get("keys");
            if (keys == null || !keys.isArray()) {
                throw new RuntimeException("JWKS response does not contain keys array");
            }

            for(JsonNode keyNode: keys) {
                String kid = keyNode.get("kid").asText();
                String kty = keyNode.get("kty").asText();
                String alg = keyNode.get("alg").asText();

                if ("RSA".equals(kty) && "RS256".equals(alg)) {
                    String n = keyNode.get("n").asText();
                    String e = keyNode.get("e").asText();

                    PublicKey publicKey = createPublicKey(n, e);
                    keyCache.put(kid, publicKey);
                }
            }
        }
        lastFetchTime = System.currentTimeMillis();
    }

    private PublicKey createPublicKey(String modulus, String exponent) throws Exception {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(modulus);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(exponent);

        BigInteger modulusBigInt = new BigInteger(1, modulusBytes);
        BigInteger exponentBigInt = new BigInteger(1, exponentBytes);

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulusBigInt, exponentBigInt);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }
}
