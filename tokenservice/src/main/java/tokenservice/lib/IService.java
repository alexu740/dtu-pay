package tokenservice.lib;

import tokenservice.service.CorrelationId;

public interface IService {
    void handleTokensRequested(String id, CorrelationId correlationId);

}
