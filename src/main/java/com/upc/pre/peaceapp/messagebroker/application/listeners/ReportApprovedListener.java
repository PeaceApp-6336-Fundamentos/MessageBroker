package com.upc.pre.peaceapp.messagebroker.application.listeners;

import com.upc.pre.peaceapp.messagebroker.events.ReportApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class ReportApprovedListener {

    private static final Logger log = LoggerFactory.getLogger(ReportApprovedListener.class);
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.broker.exchange.location}")
    private String locationExchange;

    @Value("${app.broker.routing-key.location.deleted}")
    private String locationRoutingKey;

    public ReportApprovedListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${app.broker.queue.report.approved}")
    public void handleReportApproved(ReportApprovedEvent event) {
        log.info("📩 ReportApprovedEvent received: {}", event.getReportId());

        try {
            rabbitTemplate.convertAndSend(locationExchange, locationRoutingKey, event);
            log.info("📨 APPROVED → sent to LocationService");
        } catch (Exception e) {
            log.error("⚠️ Failed to forward APPROVED to LocationService", e);
        }

        log.info("ℹ️ APPROVED does NOT notify AlertService");
    }
}
