package com.csaba79coder.truckloadingoptimization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a truck with its physical and capacity constraints.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Truck {
    /**
     * The unique identifier or name of the truck.
     */
    private String id;
    /**
     * The maximum weight (in kilograms) that the truck can carry.
     */
    private double maxWeight;
    /**
     * The dimensions of the truck's cargo space (length, width, height).
     */
    private Dimension dimension;
    /**
     * The maximum permissible weight for each axle.
     * This could be an array where each index represents an axle.
     */
    private double[] maxAxleWeights;
    /**
     * Indicates whether the truck has a refrigerated area for perishable goods.
     */
    private boolean hasRefrigeratedArea;
    /**
     * The dimensions of the refrigerated area in the truck, if available.
     * This is null if the truck does not have a refrigerated area.
     */
    private Dimension refrigeratedAreaDimension;
}
