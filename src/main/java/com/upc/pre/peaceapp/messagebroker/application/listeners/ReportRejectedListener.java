package com.upc.pre.peaceapp.messagebroker.application.listeners;

import com.upc.pre.peaceapp.messagebroker.events.ReportRejectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class ReportRejectedListener {

    private static final Logger log = LoggerFactory.getLogger(ReportRejectedListener.class);
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.broker.exchange.location}")
    private String locationExchange;

    @Value("${app.broker.routing-key.location.deleted}")
    private String locationRoutingKey;

    @Value("${app.broker.exchange.alert}")
    private String alertExchange;

    @Value("${app.broker.routing-key.alert.rejected}")
    private String alertRoutingKey;

    public ReportRejectedListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${app.broker.queue.report.rejected}")
    public void handleReportRejected(ReportRejectedEvent event) {
        log.info("📩 ReportRejectedEvent received: {}", event.getReportId());

        // LOCATION
        try {
            rabbitTemplate.convertAndSend(locationExchange, locationRoutingKey, event);
            log.info("📨 REJECTED → sent to LocationService");
        } catch (Exception e) {
            log.error("⚠️ Failed to forward REJECTED to LocationService", e);
        }

        // ALERT
        try {
            rabbitTemplate.convertAndSend(alertExchange, alertRoutingKey, event);
            log.info("📨 REJECTED → sent to AlertService");
        } catch (Exception e) {
            log.warn("⚠️ AlertService unavailable");
        }
    }
}
