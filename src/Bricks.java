import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
This is a program whose task is to create buildings from blocks.
Blocks for construction are in the box (they are preceded by "0").
Based on them, we will try to build instructions that begin with the number "1".
 */

public class Bricks {

    //is used to count the occurrences of a given element in a list
    private static int countOccurrenc(List<String> list, String element) {
        int count = 0;
        for (String item : list) {
            if (item.equals(element)) {
                count++;
            }
        }
        return count;
    }

    //is used to remove the specified number of occurrences of a given element from the list
    private static void removeOccurrenc(List<String> list, String element, int occurrenc) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(element)) {
                list.remove(i);
                occurrenc--;
                i--;
                if (occurrenc <= 0) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> combinedList = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        do {
            String input = scanner.nextLine();

            input = input.toUpperCase();
            if (input.length() != 6 || !Character.isDigit(input.charAt(0)) || input.charAt(1) != ':' || !input.substring(2).matches("[A-O]{4}")) {
                continue;
            }
            combinedList.add(input);
        } while (!scanner.nextLine().equals(""));

        scanner.close();


        ArrayList<String> box = new ArrayList<>();
        ArrayList<String> instruction = new ArrayList<>();

        for (String item : combinedList) {
            if (item.startsWith("0:")) {
                box.add(item.substring(2));
            } else {
                instruction.add(item);
            }
        }

        Map<Integer, List<String>> phase1 = new HashMap<>();
        Map<Integer, List<String>> phase2 = new HashMap<>();

        for (String instructions : instruction) {
            //inclusion of the use of ":" in the code submitted by the user
            String[] parts = instructions.split(":");
            if (parts.length < 2) {
                continue;
                // Incorrect format
            }
            try {
                int serialNumber = Integer.parseInt(parts[0]);
                String blockCode = parts[1];

                /*taking into account the instructions of the Bolk hedgehog,
                which takes precedence over all the rest.
                It is phase1
                 */

                if (serialNumber % 3 == 0) {
                    List<String> list = phase1.getOrDefault(serialNumber, new ArrayList<>());
                    list.add(blockCode);
                    phase1.put(serialNumber, list);
                } else {

                    // all the rest of the instructions. It is phase2

                    List<String> list = phase2.getOrDefault(serialNumber, new ArrayList<>());
                    list.add(blockCode);
                    phase2.put(serialNumber, list);
                }
            } catch (NumberFormatException e) {
                continue;
            }
        }

        /*
        A set of functions to check to what extent it was successful,
        or why not, to build the structures entered by the user
         */
        List<String> instructionsImplementedPhase1 = new ArrayList<>();
        List<String> instructionsImplementedPhase2 = new ArrayList<>();
        int totalBricksUsedPhase1 = 0;
        int totalBricksUsedPhase2 = 0;
        int missingBricksCount = 0;
        int notImplementedCount = 0;

        // phase1
        // iteration through map keys
        for (int number : phase1.keySet()) {
            List<String> list = phase1.get(number);
            boolean implemented = true;

            for (String blockCode : list) {
                int blockCountInInstruction = countOccurrenc(list, blockCode);
                /*
                Calculates the number of occurrences of blockCode in the list of lists using the countOccurrenc method.
                The result is stored in the variable blockCountInInstruction.
                 */
                int blockCountInBox = countOccurrenc(box, blockCode);
                /*
                Calculates the number of occurrences of blockCode in the box list using the countOccurrenc method.
                The result is stored in the variable blockCountInBox
                 */

                if (blockCountInInstruction > blockCountInBox) {
                    implemented = false;
                    missingBricksCount += (blockCountInInstruction - blockCountInBox);
                    /*
                    The difference between the number of occurrences in the instruction
                    and the number of occurrences in the box is added to the missingBricksCount variable.
                     */
                    break;
                }
            }
            if(!implemented){
                //If implemented is not true then the number of buildings that failed to build increments
                notImplementedCount++;
            }

            if (implemented) {
                for (String blockCode : list) {
                    removeOccurrenc(box, blockCode, 1);
                    /*
                    Calls removeOccurrenc with which one blockCode element is removed from the box list.
                    This function removes the number of occurrences of a given element.
                     */
                    totalBricksUsedPhase1++;
                    //Iterates through totalBricksUsedPhase1 and increases the number of blocks used in phase one
                }
                instructionsImplementedPhase1.add(Integer.toString(number));
                /*This function adds the instruction number (number) as part of the instructionsImplementedPhase1 character list.
                It stores the numbers of instructions that were implemented in phase one
                 */
            }

        }

        // phase2
        for (int number : phase2.keySet()) {
            List<String> list = phase2.get(number);
            boolean implemented = true;

            for (String blockCode : list) {
                int blockCountInInstruction = countOccurrenc(list, blockCode);
                int blockCountInBox = countOccurrenc(box, blockCode);

                if (blockCountInInstruction > blockCountInBox) {
                    implemented = false;
                    missingBricksCount += (blockCountInInstruction - blockCountInBox);
                    break;
                }
            }
            if(!implemented){
                notImplementedCount++;
            }

            if (implemented) {
                for (String blockCode : list) {
                    removeOccurrenc(box, blockCode, 1);
                    totalBricksUsedPhase2++;
                }
                instructionsImplementedPhase2.add(Integer.toString(number));
            }
        }

        int bricksRemainingInBox = box.size();
        int buildingsCompleted = instructionsImplementedPhase1.size() + instructionsImplementedPhase2.size();

        System.out.println(totalBricksUsedPhase1);
        System.out.println(totalBricksUsedPhase2);
        System.out.println(bricksRemainingInBox);
        System.out.println(missingBricksCount);
        System.out.println(buildingsCompleted);
        System.out.println(notImplementedCount);
    }
}
