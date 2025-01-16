package tokenservice.lib;

import javax.ws.rs.NotFoundException;
import java.util.Stack;

public interface IRepository {

    void addTokens(String id, Stack<String> ts) throws Exception;

    Stack<String> getTokens(String id) throws NotFoundException;

    String getIdByToken(String token) throws NotFoundException;

    void useToken(String token);
}
