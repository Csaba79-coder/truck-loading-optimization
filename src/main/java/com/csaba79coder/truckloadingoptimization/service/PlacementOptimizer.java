package com.csaba79coder.truckloadingoptimization.service;

import com.csaba79coder.truckloadingoptimization.model.Item;
import com.csaba79coder.truckloadingoptimization.model.Truck;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class PlacementOptimizer {

    private List<Item> items;
    private List<Truck> trucks;

    public int[][][] performOptimization() {
        // Kiindulási állapot
        // Minden egyes teherautóra kiszámoljuk a max pozíciók számát
        //int[][][] placementMatrix = new int[items.size()][trucks.size()][calculateMaxPositionsPerTruck(trucks.getFirst(), items)];

        // Kiindulási állapot
        // Minden egyes teherautóra kiszámoljuk a max pozíciók számát, teherautónként külön
        int[][][] placementMatrix = new int[items.size()][trucks.size()][0];

        // Inicializálás
        for (int i = 0; i < trucks.size(); i++) {
            Truck truck = trucks.get(i);
            int maxPositions = calculateMaxPositionsPerTruck(truck, items);
            placementMatrix[0][i] = new int[maxPositions];  // A megfelelő pozíciók számának tárolása a teherautóhoz
        }

        // Az áruk súlyának és térfogatának sorrendbe állítása
        List<Item> sortedItems = items.stream()
                .sorted((item1, item2) -> Double.compare(item2.getVolume(), item1.getVolume()))  // Térfogat szerint csökkenő sorrend
                .toList();

        // Iterálunk az árukon, és próbáljuk őket elhelyezni
        for (Item item : sortedItems) {
            boolean placed = false;

            // Próbálkozunk minden teherautóval
            for (Truck truck : trucks) {
                // Ha az áru elfér a teherautóban és megfelel minden szabálynak
                if (canPlaceItem(item, truck, placementMatrix)) {
                    // Elhelyezzük az árut
                    placeItem(item, truck, placementMatrix);
                    placed = true;
                    break; // Ha sikerült elhelyezni, lépünk a következő áruval
                }
            }

            if (!placed) {
                // Ha nem tudtuk elhelyezni az árut, valamit változtatni kell
                // (pl. másik stratégia vagy újrapróbálkozás)
                handlePlacementFailure(item);
            }
        }

        return placementMatrix;
    }

    private int calculateMaxPositionsPerTruck(Truck truck, List<Item> items) {
        // Kiszámoljuk a teherautó térfogatát
        double truckVolume = truck.getDimension().getWidth()
                * truck.getDimension().getHeight()
                * truck.getDimension().getLength();

        // Kiszámoljuk az átlagos áru térfogatát
        double averageItemVolume = calculateAverageItemVolume(items);

        // Meghatározzuk, hány átlagos áru fér el a teherautón
        return (int) (truckVolume / averageItemVolume);
    }

    // Az átlagos áru térfogatának kiszámítása
    private double calculateAverageItemVolume(List<Item> items) {
        double totalVolume = 0;
        for (Item item : items) {
            totalVolume += item.getVolume();
        }
        return totalVolume / items.size();
    }

    private boolean canPlaceItem(Item item, Truck truck, int[][][] placementMatrix) {
        // Ellenőrizzük, hogy az áru elfér-e a teherautóban
        if (truck.getMaxWeight() < item.getWeight()) {
            return false; // Ha a súly meghaladja a teherautó maximális súlyát
        }

        // Ellenőrizzük a térfogatot
        double truckVolume = truck.getDimension().getWidth()
                * truck.getDimension().getHeight()
                * truck.getDimension().getLength();
        double totalVolumeUsed = 0;
        for (int i = 0; i < items.size(); i++) {
            for (int k = 0; k < truckVolume; k++) {
                if (placementMatrix[i][Integer.parseInt(truck.getId())][k] == 1) {
                    totalVolumeUsed += items.get(i).getVolume();
                }
            }
        }

        if (totalVolumeUsed + item.getVolume() > truckVolume) {
            return false; // Ha a térfogat meghaladja a teherautó kapacitását
        }

        // Ellenőrizzük a súly korlátozást is
        double totalWeight = 0;
        for (int i = 0; i < items.size(); i++) {
            for (int k = 0; k < truck.getDimension().getLength(); k++) {
                if (placementMatrix[i][Integer.parseInt(truck.getId())][k] == 1) {
                    totalWeight += items.get(i).getWeight();
                }
            }
        }

        if (totalWeight + item.getWeight() > truck.getMaxWeight()) {
            return false;
        }

        // Törékeny áru: csak a legfelső rétegben
        if (item.isFragile() && !isTopLayer(item, truck, placementMatrix)) {
            return false;
        }

        return true;
    }

    private void placeItem(Item item, Truck truck, int[][][] placementMatrix) {
        // Az áru elhelyezése a megfelelő teherautón és pozíción
        for (int k = 0; k < truck.getDimension().getLength(); k++) {
            if (placementMatrix[Integer.parseInt(item.getId())][Integer.parseInt(truck.getId())][k] == 0) {
                placementMatrix[Integer.parseInt(item.getId())][Integer.parseInt(truck.getId())][k] = 1; // Elhelyezve
                break;
            }
        }
    }

    private boolean isTopLayer(Item item, Truck truck, int[][][] placementMatrix) {
        // Ellenőrzi, hogy az áru a legfelső rétegben van-e
        for (int k = 0; k < truck.getDimension().getLength(); k++) {
            if (placementMatrix[Integer.parseInt(item.getId())][Integer.parseInt(truck.getId())][k] == 1) {
                return k == 0; // Ha az áru az első szinten van, akkor top layer
            }
        }
        return false;
    }

    private void handlePlacementFailure(Item item) {
        // Itt egy alternatív megoldást kellene alkalmazni, ha nem tudunk elhelyezni egy árut
        // Például újrapróbálhatjuk más elhelyezési stratégiával, vagy jelezhetjük a hibát
        System.out.println("Hiba: Az árut nem sikerült elhelyezni: " + item);
    }
}

