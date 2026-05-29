package com.example.workmanager.store.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessType {

    FOOD_BEVERAGE("식음료"),
    RETAIL("소매"),
    LIFESTYLE("라이프스타일"),
    PROFESSIONAL("전문직");

    private final String label;
}