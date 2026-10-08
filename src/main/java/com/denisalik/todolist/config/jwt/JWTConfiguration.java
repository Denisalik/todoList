package com.denisalik.todolist.config.jwt;

import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@ConfigurationProperties(prefix = "jwt")
@Setter
@Validated
public class JWTConfiguration {
    /**
     * Default is 30 minutes in milliseconds
     */
    private long accessTokenTtl = 1800000;
    /**
     * Default is 24 hours in milliseconds
     */
    private long refreshTokenTtl = 86400000;

    @NotNull
    private Resource publicKey;

    @NotNull
    private Resource privateKey;

    // SUPPRESS CHECKSTYLE MethodName
    public int REFRESH_TOKEN_TTL_IN_SECONDS () {
        return (int) (refreshTokenTtl / 1000);
    }

    // SUPPRESS CHECKSTYLE MethodName
    public long REFRESH_TOKEN_TTL() {
        return refreshTokenTtl;
    }

    // SUPPRESS CHECKSTYLE MethodName
    public long ACCESS_TOKEN_TTL() {
        return accessTokenTtl;
    }


    private String cleanKey(Resource resource, String beginMarker, String endMarker) {
        try {
            String key = new String(resource.getInputStream().readAllBytes());
            return key
                    .replace(beginMarker, "")
                    .replace(endMarker, "")
                    .replaceAll("\\s+", "");
        } catch (Exception e) {
            throw new RuntimeException("Failed to read key from resource: " + resource.getFilename(), e);
        }
    }

    private byte[] decodeKey(String key) {
        return Base64.getDecoder().decode(key);
    }

    private KeyFactory getRSA() throws NoSuchAlgorithmException {
        return KeyFactory.getInstance("RSA");
    }

    public PrivateKey getPrivateKey() {
        if (privateKey == null || !privateKey.exists()) {
            throw new IllegalArgumentException("Private key not found");
        }
        try {
            var privateKeyBytes = decodeKey(cleanKey(privateKey, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----"));
            return getRSA().generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read private key from resource: " + privateKey.getFilename(), e);
        }
    }
    public PublicKey getPublicKey() {
        if (publicKey == null || !publicKey.exists()) {
            throw new IllegalArgumentException("Public key not found");
        }
        try {
            var publicKeyBytes = decodeKey(cleanKey(publicKey, "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----"));
            return getRSA().generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read public key from resource: " + publicKey.getFilename(), e);
        }
    }
}
