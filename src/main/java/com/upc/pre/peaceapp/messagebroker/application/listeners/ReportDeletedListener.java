package com.upc.pre.peaceapp.messagebroker.application.listeners;

import com.upc.pre.peaceapp.messagebroker.events.ReportDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Listens for ReportDeletedEvent messages and forwards them
 * to the relevant exchanges so other bounded contexts (like
 * Location and Alert) can handle related data deletion.
 */
@Component
public class ReportDeletedListener {

    private static final Logger log = LoggerFactory.getLogger(ReportDeletedListener.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.broker.exchange.location}")
    private String locationExchange;

    @Value("${app.broker.routing-key.location.deleted}")
    private String locationRoutingKey;

    @Value("${app.broker.exchange.alert}")
    private String alertExchange;

    @Value("${app.broker.routing-key.alert.deleted}")
    private String alertRoutingKey;

    public ReportDeletedListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Handles incoming ReportDeletedEvent messages.
     * This listener is triggered whenever a report is deleted.
     * It republishes the event to Location and Alert services.
     */
    @RabbitListener(queues = "${app.broker.queue.report.deleted}")
    public void handleReportDeleted(ReportDeletedEvent event) {
        log.info("📩 ReportDeletedEvent received: {}", event.getReportId());

        // Enviar a LocationService
        try {
            rabbitTemplate.convertAndSend(locationExchange, locationRoutingKey, event);
            log.info("📨 Event forwarded to LocationService ✅");
        } catch (Exception e) {
            log.error("⚠️ Failed to forward event to LocationService", e);
        }

        // Enviar a AlertService (puede no existir)
        try {
            rabbitTemplate.convertAndSend(alertExchange, alertRoutingKey, event);
            log.info("📨 Event forwarded to AlertService ✅");
        } catch (Exception e) {
            log.warn("⚠️ AlertService not available, skipping event for report {}", event.getReportId());
        }
    }

}
