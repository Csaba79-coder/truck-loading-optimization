package com.csaba79coder.truckloadingoptimization.util;

import com.csaba79coder.truckloadingoptimization.model.Item;
import com.csaba79coder.truckloadingoptimization.model.Truck;

import java.util.List;

public class ValidationUtil {

    // Árucikkek térfogatának validációja
    public static void validateItems(List<Item> items) {
        for (Item item : items) {
            if (item.getVolume() < 0) {
                throw new IllegalArgumentException("Az áru térfogata nem lehet negatív: " + item);
            }
        }
    }

    // Teherautók méreteinek validációja
    public static void validateTrucks(List<Truck> trucks) {
        for (Truck truck : trucks) {
            if (truck.getDimension().getWidth() < 0 ||
                    truck.getDimension().getHeight() < 0 ||
                    truck.getDimension().getLength() < 0) {
                throw new IllegalArgumentException("A teherautó méretei nem lehetnek negatívak: " + truck);
            }
        }
    }

    // Döntési változók (placementMatrix) validációja
    public static void validatePlacementMatrix(int[][][] placementMatrix) {
        for (int[][] matrix : placementMatrix) {
            for (int[] nums : matrix) {
                for (int num : nums) {
                    if (num != 0 && num != 1) {
                        throw new IllegalArgumentException("A placementMatrix csak 0-t vagy 1-et tartalmazhat.");
                    }
                }
            }
        }
    }

    // Elfér-e az áru a teherautóban
    public static boolean checkGoodsInTruck(List<Item> items, List<Truck> trucks, int[][][] placementMatrix) {
        // Ellenőrizze, hogy az összes áru elfér-e a teherautókban
        for (Item item : items) {
            for (Truck truck : trucks) {
                double truckVolume = truck.getDimension().getWidth()
                        * truck.getDimension().getHeight()
                        * truck.getDimension().getLength();
                double itemVolume = item.getVolume();
                if (itemVolume > truckVolume) {
                    return false;
                }
            }
        }
        return true;
    }

    private ValidationUtil() {
    }
}
