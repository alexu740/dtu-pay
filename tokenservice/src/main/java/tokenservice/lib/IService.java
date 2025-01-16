package tokenservice.lib;

import messaging.Event;

public interface IService {
    void handleTokensRequested(Event ev);

}
