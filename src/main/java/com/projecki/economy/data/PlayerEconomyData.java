package com.projecki.economy.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PlayerEconomyData {
    private final UUID uuid;
    private final Instant creationDate;
    private double balance;
}
