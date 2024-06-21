package ru.ylab_learning.coworking.domain.dto;

import java.time.LocalTime;

public record Slot(LocalTime startTime, LocalTime endTime) {
}
