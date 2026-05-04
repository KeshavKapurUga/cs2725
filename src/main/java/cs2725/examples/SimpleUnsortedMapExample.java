/**
 * Copyright (c) 2025 Sami Menik, PhD. All rights reserved.
 * 
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * This software is provided "as is," without warranty of any kind.
 */
package cs2725.examples;

import cs2725.api.SimpleMap;
import cs2725.impl.SimpleUnsortedMap;
import cs2725.viz.Graph;

public class SimpleUnsortedMapExample {

    public static void main(String[] args) {
        // A map to store the age of people
        SimpleMap<String, Integer> ageMap = new SimpleUnsortedMap<>();

        // Visualization.
        Graph.enable();
        Graph.i().makeLeftToRight();
        Graph.i().setRef("ageMap", ageMap);

        // Adding multiple age records
        ageMap.put("Alice", 25);
        ageMap.put("Bob", 30);
        ageMap.put("Charlie", 35);
        ageMap.put("David", 40);
        ageMap.put("Eve", 28);
        ageMap.put("Frank", 33);
        ageMap.put("Grace", 27);
        ageMap.put("Hank", 45);

        // Retrieving and displaying ages
        System.out.println("Alice's age: " + ageMap.get("Alice"));
        System.out.println("Bob's age: " + ageMap.get("Bob"));
        System.out.println("Eve's age: " + ageMap.get("Eve"));
        System.out.println("Hank's age: " + ageMap.get("Hank"));

        // Checking if an age record exists
        System.out.println("Is Charlie in the map? " + ageMap.containsKey("Charlie"));
        System.out.println("Is Zoe in the map? " + ageMap.containsKey("Zoe"));

        // Updating an existing record
        System.out.println("Updating Bob's age to 31.");
        ageMap.put("Bob", 31);
        System.out.println("Bob's updated age: " + ageMap.get("Bob"));

        // Removing an age entry
        System.out.println("Removed Charlie's age: " + ageMap.remove("Charlie"));

        // Displaying the number of records
        System.out.println("Total records in ageMap: " + ageMap.size());
    }

}
