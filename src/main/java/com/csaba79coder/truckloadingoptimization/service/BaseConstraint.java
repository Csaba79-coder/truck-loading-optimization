package com.csaba79coder.truckloadingoptimization.service;

import com.csaba79coder.truckloadingoptimization.model.Item;
import com.csaba79coder.truckloadingoptimization.model.Truck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseConstraint {

    // Raktér méret korlátozása (maximum térfogat)
    public boolean checkVolumeConstraint(List<Item> items, List<Truck> trucks, int[][][] placementMatrix) {
        for (int j = 0; j < trucks.size(); j++) { // minden teherautóra
            double totalVolume = 0;
            for (int i = 0; i < items.size(); i++) { // minden áru
                for (int k = 0; k < trucks.get(j).getDimension().getLength(); k++) { // minden pozíció
                    if (placementMatrix[i][j][k] == 1) { // ha az áru ezen a pozíción van
                        totalVolume += items.get(i).getVolume(); // az áru térfogata
                    }
                }
            }
            if (totalVolume > trucks.get(j).getMaxWeight()) {
                return false; // Ha a térfogat túllépi a maximális értéket
            }
        }
        return true; // Ha minden teherautóra megfelelő térfogatú áruk kerültek
    }

    // Súlykorlátozás (teherautó tengelyére eső súly)
    public boolean checkWeightConstraint(List<Item> items, List<Truck> trucks, int[][][] placementMatrix) {
        for (int j = 0; j < trucks.size(); j++) { // minden teherautó
            double totalWeight = 0;
            for (int i = 0; i < items.size(); i++) { // minden áru
                for (int k = 0; k < trucks.get(j).getDimension().getLength(); k++) { // minden pozíció
                    if (placementMatrix[i][j][k] == 1) { // ha az áru ezen a pozíción van
                        totalWeight += items.get(i).getWeight(); // az áru súlya
                    }
                }
            }
            for (int k = 0; k < trucks.get(j).getMaxAxleWeights().length; k++) {
                if (totalWeight > trucks.get(j).getMaxAxleWeights()[k]) { // ha a tengely súlya túllépi a maximális értéket
                    return false;
                }
            }
        }
        return true; // Ha minden teherautóra megfelelő súlyú áruk kerültek
    }

    // Törékeny áruk (csak a legfelső rétegben)
    public boolean checkFragileItems(List<Item> items, int[][][] placementMatrix) {
        for (int i = 0; i < items.size(); i++) { // minden áru
            if (items.get(i).isFragile()) { // ha törékeny
                for (int j = 0; j < placementMatrix[i].length; j++) { // minden teherautó
                    for (int k = 0; k < placementMatrix[i][j].length; k++) { // minden pozíció
                        if (placementMatrix[i][j][k] == 1 && k > 0) { // ha nem a legfelső réteg
                            return false; // Törékeny áruk nem lehetnek nem a legfelső rétegben
                        }
                    }
                }
            }
        }
        return true; // Ha minden törékeny áru helyesen van elhelyezve
    }

    // Hűtést igénylő áruk
    public boolean checkCoolingItems(List<Item> items, List<Truck> trucks, int[][][] placementMatrix) {
        for (int i = 0; i < items.size(); i++) { // minden áru
            if (items.get(i).isRefrigerated()) { // ha hűtést igényel
                for (int j = 0; j < trucks.size(); j++) { // minden teherautó
                    for (int k = 0; k < trucks.get(j).getDimension().getLength(); k++) { // minden pozíció
                        if (placementMatrix[i][j][k] == 1) { // ha az áru ezen a pozíción van
                            if (!trucks.get(j).isHasRefrigeratedArea()) { // ha a teherautó nem rendelkezik hűtéssel
                                return false; // Hűtést igénylő áru nem lehet nem hűtött teherautón
                            }
                        }
                    }
                }
            }
        }
        return true; // Ha minden hűtést igénylő áru helyesen van elhelyezve
    }
}
