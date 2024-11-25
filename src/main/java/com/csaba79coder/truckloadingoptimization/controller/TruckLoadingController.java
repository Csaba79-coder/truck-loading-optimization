package com.csaba79coder.truckloadingoptimization.controller;

import com.csaba79coder.truckloadingoptimization.model.Item;
import com.csaba79coder.truckloadingoptimization.model.Truck;
import com.csaba79coder.truckloadingoptimization.service.ObjectiveFunction;
import com.csaba79coder.truckloadingoptimization.service.BaseConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/optimize")
public class TruckLoadingController {

    @Autowired
    private ObjectiveFunction objectiveFunction;

    @Autowired
    private BaseConstraint baseConstraint;

    @PostMapping("/load")
    public double calculateOptimalLoad(@RequestBody OptimizationRequest request) {
        List<Item> items = request.getItems();
        List<Truck> trucks = request.getTrucks();
        int[][][] placementMatrix = request.getPlacementMatrix();

        // Calculate total volume used
        double totalVolumeUsed = objectiveFunction.define(items, trucks, placementMatrix);

        // Check constraints
        if (!baseConstraint.checkVolumeConstraint(items, trucks, placementMatrix)) {
            throw new IllegalArgumentException("Volume constraint violated");
        }

        if (!baseConstraint.checkWeightConstraint(items, trucks, placementMatrix)) {
            throw new IllegalArgumentException("Weight constraint violated");
        }

        if (!baseConstraint.checkFragileItems(items, placementMatrix)) {
            throw new IllegalArgumentException("Fragile items placed incorrectly");
        }

        if (!baseConstraint.checkCoolingItems(items, trucks, placementMatrix)) {
            throw new IllegalArgumentException("Cooling items placed incorrectly");
        }

        return totalVolumeUsed;
    }
}
