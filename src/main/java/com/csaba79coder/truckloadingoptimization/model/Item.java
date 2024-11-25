package com.csaba79coder.truckloadingoptimization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String id;
    private double length;
    private double width;
    private double height;
    private double weight;
    /**
     * ha valami törékeny, akkor a top layer-en kell lennie, és súlyban nem lehet nehezebb, mint az alatta lévő áru.
     * Ez biztosítja, hogy a törékeny áruk ne sérüljenek meg, és a súlyeloszlás stabil maradjon
     */
    private boolean isFragile;
    private boolean isRefrigerated;
    // Térfogat számítása
    public double getVolume() {
        return length * width * height;
    }
}
