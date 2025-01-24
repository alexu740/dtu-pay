package tokenservice.repositories;

import java.util.HashMap;
import java.util.Map;
/**
 * @author: Senhao Zou, s242606
 */
public class TokenRepository {
    private Map<String, String> tokens;

    public TokenRepository() {
        this.tokens = new HashMap<>();
    }
    
    public Map<String, String> getTokens() {
        return tokens;
    }

    public void setTokens(Map<String, String> tokens) {
        this.tokens = tokens;
    }

    public void put(String token, String status) {
        this.tokens.put(token, status);
    }

    public String get(String key) {
        return this.tokens.get(key);
    }

    public void remove(String key) {
        this.tokens.remove(key);
    }
}
