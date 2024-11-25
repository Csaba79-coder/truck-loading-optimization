package com.csaba79coder.truckloadingoptimization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPositionInTruck {
    private int layer; // függőleges koordinálta
    private int row; // x koordinálta
    private int column; // y koordinálta
}
