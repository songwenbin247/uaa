package org.cloudfoundry.identity.uaa.provider.token;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cloudfoundry.identity.uaa.oauth.token.ClaimConstants;
import org.cloudfoundry.identity.uaa.util.JsonUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;

public class ClientAssertionHeaderAuthenticator {
    private static final Log logger = LogFactory.getLog(ClientAssertionHeaderAuthenticator.class);

    private final Integer maxHeaderLifeInSeconds;
    
    public ClientAssertionHeaderAuthenticator(int maxHeaderLifeInSeconds) {
        this.maxHeaderLifeInSeconds = maxHeaderLifeInSeconds;
    }
    
    public ClientAssertionHeaderAuthenticator() {
        this(15);
    }

    /**
     * @param clientAssertionHeader  - a JWT with 'iat', 'sub' and 'tenant_id' claims for the client
     * @return verified claims in 'clientAssertionHeader' token
     * @throws BadCredentialsException must throw this if authentication fails
     */
    public Map<String, Object> authenticate(final String clientAssertionHeader, final String proxyPublicKey) 
            throws BadCredentialsException {
        Jwt clientAssertion = null;
        try {
            if (StringUtils.hasText(clientAssertionHeader)) {
                clientAssertion = JwtHelper.decode(clientAssertionHeader);
                clientAssertion.verifySignature(new RsaVerifier(proxyPublicKey));
                
                Map<String, Object> claims = JsonUtils.readValue(clientAssertion.getClaims(), 
                        new TypeReference<Map<String, Object>>() { });
                
                validateClaims(claims);
                return claims;
            }
        } catch (RuntimeException e) {
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
            logger.debug("Validation failed for client assertion header. Header:{" + clientAssertion + "}; error: " + e);
        }

        // Do not include error detail in this exception.
        throw new BadCredentialsException("Validation of client assertion header failed.");
    }
    
    private void validateClaims(Map<String, Object> claims) {
        long currentTime = System.currentTimeMillis();
        
        //require iat claim and check time skew
        Integer iat = (Integer) claims.get(ClaimConstants.IAT);
        if (iat == null || isSkewed(iat, currentTime) ) {
            throw new BadCredentialsException("iat claim is required in Predix-Client-Assertion token.");
        } 
        
        //force max TTL for header token
        if (currentTime/1000 - iat > this.maxHeaderLifeInSeconds) {
            throw new BadCredentialsException("Predix-Client-Assertion token has expired.");
        }

    }

    private boolean isSkewed(Integer iat, long time) {
        long delta = Math.abs(time/1000 - iat);
        return  delta > 5;
    }

}
