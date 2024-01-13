package com.jobinow.model.dto.requests;

import com.jobinow.model.dto.responses.PackResponse;
import com.jobinow.model.dto.responses.UserResponses;
import com.jobinow.model.entities.Subscription;
import com.jobinow.model.entities.Tag;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for representing a request related to {@link Subscription}.
 * This DTO includes information such as the Subscription pack and jobSeeker for creating or updating a Subscription.
 *
 * @author <a href="mailto:ouharri.outman@gmail.com">ouharri</a>
 */
public record SubscriptionRequest(
        @NotNull
        PackResponse pack,

        @NotNull
        UserResponses jobSeeker
) implements _Request {
}