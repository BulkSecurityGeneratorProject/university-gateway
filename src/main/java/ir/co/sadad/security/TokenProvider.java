package ir.co.sadad.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ir.co.sadad.config.SecurityConfig;
import org.eclipse.microprofile.jwt.Claims;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static io.jsonwebtoken.Header.JWT_TYPE;
import static io.jsonwebtoken.Header.TYPE;
import static java.lang.Thread.currentThread;

public class TokenProvider {

    private PrivateKey privateKey;

    private String issuer;

    private long tokenValidityMillis;

    private long tokenValidityMillisForRememberMe;

    @Inject
    private Logger log;

    @Inject
    private SecurityConfig securityConfig;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = readPrivateKey("privateKey.pem");
        } catch (Exception ex) {
            log.error("Unable to read privateKey.pem ", ex);
            throw new IllegalStateException(ex);
        }

        this.issuer = securityConfig.getIssuer();
        this.tokenValidityMillis
                = 1000 * securityConfig.getTokenValidityInSeconds();
        this.tokenValidityMillisForRememberMe
                = 1000 * securityConfig.getTokenValidityInSecondsForRememberMe();
    }

    public String createToken(String username, Set<String> groups, Boolean rememberMe) {
        long issuedTime = System.currentTimeMillis();
        long expirationTime = issuedTime
                + (rememberMe ? tokenValidityMillisForRememberMe : tokenValidityMillis);

        return Jwts.builder()
                .setHeaderParam(TYPE, JWT_TYPE)
                .setId(UUID.randomUUID().toString())
                .setSubject(username)
                .claim("upn",username)
                .claim(Claims.groups.name(), groups)
                .setIssuer(issuer)
                .setIssuedAt(new Date(issuedTime))
                .setExpiration(new Date(expirationTime))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    private PrivateKey readPrivateKey(String resourceName) throws Exception {
        byte[] byteBuffer = new byte[16384];
        int length = currentThread().getContextClassLoader()
                .getResource(resourceName)
                .openStream()
                .read(byteBuffer);

        String key = new String(byteBuffer, 0, length)
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)----", "")
                .replaceAll("\r\n", "")
                .replaceAll("\n", "")
                .trim();

        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key)));
    }

}