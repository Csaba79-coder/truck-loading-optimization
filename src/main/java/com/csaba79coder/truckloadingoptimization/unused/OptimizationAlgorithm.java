package com.csaba79coder.truckloadingoptimization.unused;

import com.csaba79coder.truckloadingoptimization.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Áruk rendezése prioritások alapján: A törékeny áruk kerüljenek előre, mivel ezek csak a felső rétegbe helyezhetők.
 * Hűtött áruk kezelése: A hűtést igénylő áruk csak a hűtött térben kerülhetnek elhelyezésre.
 * Tengelysúlyok kezelése: Az áruk egyenletes elosztása a tengelyek között megakadályozza a túlterhelést.
 * Iteráció teherautók között: Az algoritmus több teherautót is figyelembe vesz az áruk optimális elosztásához.
 */
@Service
public class OptimizationAlgorithm {

    public OptimizationResult optimize(List<Item> items, List<Truck> trucks) {
        // 1. Inicializálás
        OptimizationResult result = new OptimizationResult();
        List<TruckAllocation> truckAllocations = new ArrayList<>();

        // 2. Árucikkek rendezése prioritás szerint
        items.sort((a, b) -> Boolean.compare(b.isFragile(), a.isFragile()));

        // 3. Teherautók iterálása és áruk elosztása
        for (Truck truck : trucks) {
            TruckAllocation allocation = new TruckAllocation();
            allocation.setTruck(truck);
            List<Allocation> allocations = new ArrayList<>();

            double usedVolume = 0;
            double usedWeight = 0;
            double[] axleWeights = new double[truck.getMaxAxleWeights().length];

            for (Item item : items) {
                // Ellenőrizze, hogy az áru elfér-e a teherautóban
                if (canFit(item, truck, usedVolume, usedWeight, axleWeights)) {
                    Allocation itemAllocation = allocateItemToTruck(item, truck, axleWeights);
                    allocations.add(itemAllocation);

                    // Frissítse a kihasznált kapacitásokat
                    usedVolume += item.getVolume();
                    usedWeight += item.getWeight();
                }
            }

            allocation.setAllocations(allocations);
            allocation.setTotalVolumeUsed(usedVolume);
            allocation.setTotalWeightUsed(usedWeight);
            truckAllocations.add(allocation);
        }

        // 4. Eredmények összegzése
        result.setTruckAllocations(truckAllocations);
        result.setSuccessful(!truckAllocations.isEmpty());
        result.setTotalVolumeUsed(truckAllocations.stream()
                .mapToDouble(TruckAllocation::getTotalVolumeUsed)
                .sum());
        result.setTotalWeightUsed(truckAllocations.stream()
                .mapToDouble(TruckAllocation::getTotalWeightUsed)
                .sum());

        return result;
    }

    private boolean canFit(Item item, Truck truck, double usedVolume, double usedWeight, double[] axleWeights) {
        // Térfogat ellenőrzés
        if (usedVolume + item.getVolume() > truck.getDimension().calculateVolume()) {
            return false;
        }

        // Súly ellenőrzés
        if (usedWeight + item.getWeight() > truck.getMaxWeight()) {
            return false;
        }

        // Tengelysúly ellenőrzés
        for (int i = 0; i < axleWeights.length; i++) {
            if (axleWeights[i] + item.getWeight() / axleWeights.length > truck.getMaxAxleWeights()[i]) {
                return false;
            }
        }

        // Hűtött áruk elhelyezése
        if (item.isRefrigerated() && truck.getRefrigeratedAreaDimension() == null) {
            return false;
        }

        return true;
    }

    private Allocation allocateItemToTruck(Item item, Truck truck, double[] axleWeights) {
        Allocation allocation = new Allocation();
        allocation.setItem(item);
        allocation.setTruck(truck);

        // Árucikk pozíció kiszámítása
        ItemPositionInTruck position = new ItemPositionInTruck();
        position.setLayer(0); // Feltételezzük, hogy az első rétegbe kerül
        position.setRow(0);
        position.setColumn(0);
        allocation.setItemPositionInTruck(position);

        // Tengelysúly frissítése
        for (int i = 0; i < axleWeights.length; i++) {
            axleWeights[i] += item.getWeight() / axleWeights.length;
        }

        return allocation;
    }
}

