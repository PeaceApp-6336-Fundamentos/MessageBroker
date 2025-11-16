package com.upc.pre.peaceapp.messagebroker.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportApprovedEvent {

    private Long reportId;
    private Long userId;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
}
