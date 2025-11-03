package com.upc.pre.peaceapp.messagebroker.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Event emitted when a report is deleted.
 * The Location and Alert bounded contexts listen to this event
 * to remove their associated entities (location and alert).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDeletedEvent {

    /** The ID of the deleted report */
    private Long reportId;

    /** The ID of the user who deleted the report */
    private Long userId;

    /** The date and time when the event occurred */
    private LocalDateTime timestamp;

    /** Optional message for logging or debugging */
    private String message;
}
