package ru.ylab_learning.coworking.domain.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class Slot {

    private final LocalTime startTime;

    private final LocalTime endTime;
}
