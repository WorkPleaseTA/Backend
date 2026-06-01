package com.example.workmanager.store.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreSize {

    SMALL("1-5명"),
    MEDIUM("6-10명"),
    LARGE("11-15명"),
    EXTRA_LARGE("16명 이상");

    private final String label;
}