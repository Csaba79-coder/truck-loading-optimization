package com.csaba79coder.truckloadingoptimization.bootstrap;

import com.csaba79coder.truckloadingoptimization.model.*;
import com.csaba79coder.truckloadingoptimization.service.OptimizationAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoadProjectWithTestData implements ApplicationRunner {

    private final OptimizationAlgorithm optimizationAlgorithm;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Példa teherautók és áruk
        Truck truck1 = new Truck("Truck1", 10000, new Dimension(10, 5, 3), new double[]{3000, 2500}, false, null);
        // A teherautó alap térfogata: 12 m x 5 m x 3 m = 180 m³
        // A hűtött rész: 3 m x 5 m x 2 m = 30 m³
        Truck truck2 = new Truck("Truck2", 12000, new Dimension(12, 5, 3), new double[]{4000, 3500}, true, new Dimension(3, 5, 2));

        Item item1 = new Item("Item1", 2, 2, 1, 1000, false, false);
        Item item2 = new Item("Item2", 3, 2, 1, 2000, true, false);  // Törékeny
        Item item3 = new Item("Item3", 2, 3, 1, 1500, false, true);  // Hűtött

        List<Truck> trucks = Arrays.asList(truck1, truck2);
        List<Item> items = Arrays.asList(item1, item2, item3);

        // Használja az optimalizálási algoritmust
        OptimizationResult result = optimizationAlgorithm.optimize(items, trucks);

        // Kiírja az eredményeket
        System.out.println("Optimalizált teherautó hozzárendelés:");
        for (TruckAllocation allocation : result.getTruckAllocations()) {
            System.out.println("Teherautó: " + allocation.getTruck().getId());
            for (Allocation alloc : allocation.getAllocations()) {
                System.out.println(" - Áru: " + alloc.getItem().getId() + ", Pozíció: " + alloc.getItemPositionInTruck());
            }
        }
        System.out.println("Összes használt térfogat: " + result.getTotalVolumeUsed());
        System.out.println("Összes használt súly: " + result.getTotalWeightUsed());
    }
}
