package tokenservice.impl;

import tokenservice.lib.IRepository;
import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import java.util.ArrayList;

public class Repository implements IRepository {
    @Getter
    @Setter
    private Map<String, Stack<String>> tokens = new HashMap<>();
    private Map<String, ArrayList<String>> usedTokens = new HashMap<>();

    @Override
    public void addTokens(String id, Stack<String> ts) {
        System.out.println("addTokens " + id);
        tokens.put(id, ts);
    }

    @Override
    public Stack<String> getTokens(String id) throws NotFoundException {
        System.out.println("getTokens " + id);
        Stack<String> ts = tokens.get(id);
        if (ts != null) {
            System.out.println("ts: " + ts);
            return ts;
        } else {
            System.out.println("AccountWithId_" + id + "_hasNoTokens");
            throw new NotFoundException("AccountWithId_" + id + "_hasNoTokens");
        }
    }

    public void useToken(String token) {
        System.out.println("useToken " + token);
        for (Map.Entry<String, Stack<String>> entry : tokens.entrySet()) {
            Stack<String> stack = entry.getValue();
            if (stack.remove(token)) {
                ArrayList<String> usedList = usedTokens.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());
                usedList.add(token);
            }
        }
    }

    public String getIdByToken(String token) throws NotFoundException {
        System.out.println("getIdByToken " + token);
        for (Map.Entry<String, Stack<String>> entry : tokens.entrySet()) {
            Stack<String> stack = entry.getValue();
            if (containsToken(stack, token)) {
                System.out.println("token found: " + token);
                System.out.println("customer id " + entry.getKey());
                return entry.getKey();
            }
        }
        throw new NotFoundException("Token_" + token + "_not_found");
    }

    private boolean containsToken(Stack<String> stack, String targetToken) {
        for (String token : stack) {
            if (token.equals(targetToken)) {
                return true;
            }
        }
        return false;
    }

}
