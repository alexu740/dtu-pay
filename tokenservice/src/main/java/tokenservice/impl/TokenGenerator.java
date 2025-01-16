package tokenservice.impl;

import lombok.NoArgsConstructor;
import java.util.Stack;

@NoArgsConstructor
public class TokenGenerator {
    private int maxTokens = 6;

    public Stack<String> generateTokens() throws Exception {
        Stack<String> tokens = new Stack<>();
        try {
            for (int i = 0; i < maxTokens; i++) {
                tokens.push(java.util.UUID.randomUUID().toString());
            }
        } catch (Exception e) {
            throw new Exception("Failed to generate tokens");
        }
        return tokens;
    }
}
