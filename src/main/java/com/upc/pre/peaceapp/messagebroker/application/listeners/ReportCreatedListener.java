package com.upc.pre.peaceapp.messagebroker.application.listeners;

import com.upc.pre.peaceapp.messagebroker.events.ReportCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class ReportCreatedListener {

    private static final Logger log = LoggerFactory.getLogger(ReportCreatedListener.class);

    @RabbitListener(queues = "${app.broker.queue.report.created}")
    public void handleReportCreated(ReportCreatedEvent event) {
        log.info("📩 ReportCreatedEvent received: {}", event.getReportId());
        log.info("ℹ️ No forwarding (created events do not trigger other services)");
    }
}
