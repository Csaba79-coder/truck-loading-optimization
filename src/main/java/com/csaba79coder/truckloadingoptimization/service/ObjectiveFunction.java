package com.csaba79coder.truckloadingoptimization.service;

import com.csaba79coder.truckloadingoptimization.model.Item;
import com.csaba79coder.truckloadingoptimization.model.Truck;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.csaba79coder.truckloadingoptimization.util.ValidationUtil.*;

@Service
public class ObjectiveFunction {

    public double define(List<Item> items, List<Truck> trucks, int[][][] placementMatrix) {
        // Validációk
        validateItems(items);
        validateTrucks(trucks);
        validatePlacementMatrix(placementMatrix);

        double totalVolumeUsed = 0;
        double[] totalWeightPerTruck = new double[trucks.size()]; // Teherautónkénti összsúly

        // N = az áruk száma
        int N = items.size();
        // M = teherautók száma
        int M = trucks.size();
        // L = pozíciók száma (egyszerűsítve a teherautó térfogata alapján)

        // Iteráljunk végig minden áruk, teherautó és pozíció kombinációján
        for (int i = 0; i < N; i++) { // Minden árun
            for (int j = 0; j < M; j++) { // Minden teherautóra
                // Számoljuk ki a teherautó térfogatát
                double truckVolume = trucks.get(j).getDimension().getWidth()
                        * trucks.get(j).getDimension().getHeight()
                        * trucks.get(j).getDimension().getLength();

                // Számoljuk a teherautó súlyát a döntési változók alapján
                for (int k = 0; k < truckVolume; k++) { // Minden pozícióra a teherautóban
                    if (placementMatrix[i][j][k] == 1) { // Ha az áru ezen a pozíción van
                        totalVolumeUsed += items.get(i).getVolume(); // Add hozzá az áru térfogatát

                        // Hozzáadjuk az áru súlyát a teherautó összsúlyához
                        totalWeightPerTruck[j] += items.get(i).getWeight();
                    }
                }
            }
        }

        // Ellenőrizhetjük, hogy a teherautók súlya nem haladja-e meg a maximális teherbírást
        for (int j = 0; j < M; j++) {
            double maxWeight = trucks.get(j).getMaxWeight();
            if (totalWeightPerTruck[j] > maxWeight) {
                throw new IllegalStateException("A " + (j + 1) + ". teherautó súlyhatárt meghaladja!");
            }
        }

        return totalVolumeUsed;
    }
}
