package org.tailoredit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OutboundMessage {

    private String number;
    private String message;

}
