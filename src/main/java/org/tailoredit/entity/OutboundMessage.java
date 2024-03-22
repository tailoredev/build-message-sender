package org.tailoredit.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@RegisterForReflection
public class OutboundMessage {

    private String number;
    private String message;

}
