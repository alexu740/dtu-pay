package tokenservice.lib;

import tokenservice.impl.CorrelationId;

public interface IService {
    void handleTokensRequested(String id, CorrelationId correlationId);

}
