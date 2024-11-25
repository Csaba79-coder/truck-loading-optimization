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

    public static void validatePlacement(List<Item> items, int[][][] placementMatrix) {
        int maxHeight = 3; // Max 3 szint
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            // Ellenőrizzük, hogy az áru maximális magassága 3 lehet
            int itemHeight = getItemHeight(placementMatrix, i);  // Itt szükség lehet egy segédmetódusra a magasság meghatározásához
            if (itemHeight > maxHeight) {
                throw new IllegalArgumentException("Az áru túl magas: " + item);
            }

            // Törékeny áru a legfelső szintre
            if (item.isFragile()) {
                if (!isOnTop(placementMatrix, i)) {
                    throw new IllegalArgumentException("A törékeny áru nem a tetejére került: " + item);
                }

                // Ellenőrizzük, hogy a törékeny áru kisebb súlyú-e, mint az alatta lévő nem törékeny áru
                double weightBelow = getWeightBelow(placementMatrix, i, items);
                if (item.getWeight() >= weightBelow) {
                    throw new IllegalArgumentException("A törékeny áru túl nehéz ahhoz, hogy a tetejére kerüljön: " + item);
                }
            }
        }
    }

    // Segédfüggvények a magasság és súly lekérdezésére (egyszerű példák)
    private static int getItemHeight(int[][][] placementMatrix, int itemIndex) {
        // Visszaadja az áru magasságát (szintjét) a placementMatrix alapján
        for (int height = 0; height < placementMatrix.length; height++) {
            for (int row = 0; row < placementMatrix[height].length; row++) {
                for (int col = 0; col < placementMatrix[height][row].length; col++) {
                    if (placementMatrix[height][row][col] == itemIndex) {
                        return height; // Visszaadja a szintet, ahol az áru található
                    }
                }
            }
        }
        return -1; // Ha nem található meg az áru, hibát jelezhetünk
    }

    private static boolean isOnTop(int[][][] placementMatrix, int itemIndex) {
        // Ellenőrzi, hogy a törékeny áru tényleg a legfelső szinten van-e
        int itemHeight = getItemHeight(placementMatrix, itemIndex);
        return itemHeight == 0; // Ha az áru az első szinten van, akkor az a legfelső szint
    }

    private static double getWeightBelow(int[][][] placementMatrix, int itemIndex, List<Item> items) {
        // Ellenőrzi, hogy az alatta lévő áru súlya kisebb-e a törékeny áruknál
        int itemHeight = getItemHeight(placementMatrix, itemIndex);

        // Ha az áru a legfelső szinten van, nincs alatta más áru
        if (itemHeight == 0) {
            return Double.MAX_VALUE; // Nincs más áru alatta, tehát a törékeny áru bármekkora súlyú lehet
        }

        // Visszaadja az alatta lévő áruk súlyát a következő szinten
        for (int row = 0; row < placementMatrix[itemHeight - 1].length; row++) {
            for (int col = 0; col < placementMatrix[itemHeight - 1][row].length; col++) {
                int belowItemIndex = placementMatrix[itemHeight - 1][row][col];
                if (belowItemIndex != 0) { // Ha van ott áru
                    Item belowItem = items.get(belowItemIndex);
                    return belowItem.getWeight();
                }
            }
        }
        return Double.MAX_VALUE; // Ha nincs alatta áru, akkor nincs súlyra vonatkozó korlátozás
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
