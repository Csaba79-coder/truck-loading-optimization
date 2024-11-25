package com.csaba79coder.truckloadingoptimization.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the dimensions of a cargo space.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dimension {
    /**
     * Length of the cargo space in meters.
     */
    private double length;

    /**
     * Width of the cargo space in meters.
     */
    private double width;

    /**
     * Height of the cargo space in meters.
     */
    private double height;

    /**
     * Calculates the total volume of the cargo space in cubic meters.
     */
    public double calculateVolume() {
        return length * width * height;
    }
}
