package com.example.cs213project3.rental;

import com.example.cs213project3.Controller;
import com.example.cs213project3.util.List;
import com.example.cs213project3.vehicle.Sedan;
import com.example.cs213project3.vehicle.Truck;
import com.example.cs213project3.vehicle.Utility;
import com.example.cs213project3.vehicle.Vehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Fleet class that manages a collection of Vehicles.
 *  It allows the user to search, resize, add, remove, and match vehicles in the list.
 *  It also allows the user to print the fleet by make and date.
 * @author Lana Huang
 */
public class Fleet extends List<Vehicle> {

    /**
     * Constructs a fleet with the initial capacity size, CAPACITY, and a size of 0.
     */
    public Fleet() {
        super();
    }

    /**
     * Gets the vehicle given the license plate number from the fleet.
     * @param plate The plate number to be found in the fleet.
     * @return vehicle The vehicle found in the fleet returns null if it cannot find it.
     */
    public static Vehicle getVehicle(String plate) {
        for (int i = 0; i < Controller.fleet.size(); i++) {
            if (Controller.fleet.get(i).getPlate().equals(plate)) {
                return Controller.fleet.get(i);
            }
        }
        return null;
    }

    /**
     * Loads vehicles into the Fleet via text file.
     * File needs to be placed in the top-level project folder.
     * Should not add the same vehicle if loaded twice.
     */
    public static Integer loadVehicles(List<Vehicle> fleet, String path) {
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(new File(file.toURI()));
            int numVehiclesLoaded = 0;

            while (scanner.hasNextLine()) {
                String[] dataToken = ("A " + scanner.nextLine().trim()).split("\\s+");

                if (Vehicle.isValidVehicle(dataToken)) {
                    Vehicle newVehicle = null;
                    switch (dataToken[1].substring(dataToken[1].length() - 1)){
                        case "X" -> newVehicle = new Truck(dataToken);
                        case "D" -> newVehicle = new Utility(dataToken);
                        case "S" -> newVehicle = new Sedan(dataToken);
                        default -> {
                            Controller.printLoadVehicleMessage("Unknown Vehicle Type", dataToken[1], 0);
                            return 0;
                        }
                    }
                    if (!fleet.contains(newVehicle)) {
                        fleet.add(newVehicle);
                        numVehiclesLoaded++;
                    }
                } else { return 0; }
            }
            Controller.printLoadVehicleMessage("Vehicles Loaded Message", null, numVehiclesLoaded);
            scanner.close();
            return numVehiclesLoaded;
        }
        catch (FileNotFoundException exception) {
            Controller.printLoadVehicleMessage("Text file not found", exception.getMessage(), 0);

        }
        return null;
    }

}
