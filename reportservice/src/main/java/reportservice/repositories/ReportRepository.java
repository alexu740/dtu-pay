package reportservice.repositories;

import reportservice.dto.Payment;

import java.util.HashMap;
import java.util.Map;

import boilerplate.Event;
import boilerplate.MessageQueue;
/**
 * @author: Nicolas Venizelou, s232523
 */
public class ReportRepository {
    private MessageQueue queue;
    private final Map<String, Payment> payments = new HashMap<>();

    public ReportRepository(MessageQueue queue) {
        this.queue = queue;
    }

    public void save(Payment payment) {
        payments.put(payment.getPaymentId(), payment);
        var event = new Event("ReportGenerated", new Object[] { payment });
        queue.publish(event);
    }
}
