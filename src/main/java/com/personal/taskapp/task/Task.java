package com.personal.taskapp.task;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public record Task(
    Integer id,
    @NotEmpty
    String taskTitle,
    LocalDateTime startedOn,
    LocalDateTime completedOn,
    @Positive
    TimeSet timeSet
) {
    public Task {
        if (!completedOn.isAfter(startedOn)) {
            throw new IllegalArgumentException("Time Completed On must be set after Started On");
        }
    }

    public Duration getDuration() {
        return Duration.between(startedOn, completedOn);
    }
}
