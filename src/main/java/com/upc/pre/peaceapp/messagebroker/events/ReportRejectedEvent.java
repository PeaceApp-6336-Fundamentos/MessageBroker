package com.upc.pre.peaceapp.messagebroker.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRejectedEvent {

    private Long reportId;
    private Long userId;
    private String reason;
    private LocalDateTime timestamp;
}
