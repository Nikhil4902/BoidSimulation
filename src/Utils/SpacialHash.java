package Utils;

import ParameterSettings.Parameters;
import SimulationObjects.SimulationObject;

import java.util.ArrayList;

public class SpacialHash {
    @SuppressWarnings("unchecked")
    public static ArrayList<Integer>[] spacialHash = initializeHash();

    public static void setSpacialHash(ArrayList<SimulationObject> simulationObjects) {
        for (int i = 0; i < spacialHash.length; i++) {
            SpacialHash.spacialHash[i] = new ArrayList<>();
        }
        for (int i = 0; i < simulationObjects.size(); i++) {
            SpacialHash.spacialHash[simulationObjects.get(i).getHashIndex()].add(i);
        }
    }

    public static ArrayList<Integer> getObjectsInCell(int hashIdx) {
        if (SpacialHash.spacialHash[hashIdx] == null) {
            return new ArrayList<>();
        }
        return SpacialHash.spacialHash[hashIdx];
    }

    public static void updateHashTable(int objectIdx, int oldHashIdx, int newHashIdx) {
        if (oldHashIdx != newHashIdx) {
            spacialHash[oldHashIdx].remove(Integer.valueOf(objectIdx));
            spacialHash[newHashIdx].add(objectIdx);
        }
    }

    public static void addToHashTable(int objectIdx, int hashIdx) {
        spacialHash[hashIdx].add(objectIdx);
    }

    private static ArrayList<Integer>[] initializeHash() {
        int totalBoxes = Parameters.numVerticalBoxes * Parameters.numHorizontalBoxes;
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] ans = new ArrayList[totalBoxes];
        for (int i = 0; i < totalBoxes; i++) {
            ans[i] = new ArrayList<>();
        }
        return ans;
    }
}
