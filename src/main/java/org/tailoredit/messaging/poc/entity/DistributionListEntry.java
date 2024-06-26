package org.tailoredit.messaging.poc.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@RegisterForReflection
public class DistributionListEntry {

    @NotBlank(message = "Name may not be blank")
    private String name;

    @NotBlank(message = "Number may not be blank")
    @Pattern(regexp = "\\d{10}", message = "Number my comprise of 10 digits")
    private String number;

}
