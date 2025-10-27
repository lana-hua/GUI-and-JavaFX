package com.example.cs213project3.vehicle;

import com.example.cs213project3.rental.Date;
import com.example.cs213project3.rental.Frontend;
import com.example.cs213project3.rental.Make;
import com.example.cs213project3.rental.Campus;

/**
 Vehicle class that contains information about the vehicle.
 It contains the string license plate, the Date it was obtained, the make of the car, and the mileage on the odometer.
 @author Lana Huang
 */
public abstract class Vehicle implements Comparable<Vehicle> {
    protected String plate; //license plate number
    protected Date obtained; //Date class described in the next page
    protected Make make; //Make is an enum class
    protected int mileage; //current reading on the odometer
    protected Campus campus; //Campus is an enum class defining the campuses.

    public abstract double charge(int mileageUsed); //charge per mile used
    public abstract double surcharge(int mileageUsed, boolean surcharge);

    /**
     * Sets a Vehicles campus given a campus.
     * @param campus Campus that the Vehicle's campus should be set to.
     */
    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    /**
     * Gets the mileage from an instance of Vehicle.
     * @return mileage
     */
    public int getMileage() {
        return mileage;
    }

    /**
     * Sets the mileage from an instance of Vehicle with the given mileage.
     * @param mileage The mileage that will be set to.
     */
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    /**
     * Gets the plate from an instance of Vehicle.
     * @return plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * Gets the make from an instance of Vehicle.
     * @return make
     */
    public Make getMake() {
        return make;
    }

    /**
     * Gets date from an instance of Vehicle.
     * @return date
     */
    public Date getDate() {
        return obtained;
    }

    /**
     * Gets campus from an instance of Vehicle.
     * @return campus
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * Constructs Vehicle given the plate, date obtained, make, and mileage.
     * @param plate String license plate number.
     * @param obtained Date obtained.
     * @param make Make of the vehicle.
     * @param mileage Mileage of the vehicle.
     * @param campus Campus that the Vehicle is from.
     */
    public Vehicle(String plate, Date obtained, Make make, int mileage, Campus campus) {
        this.plate = plate;
        this.obtained = obtained;
        this.make = make;
        this.mileage = mileage;
        this.campus = campus;
    }

    /**
     * Constructs Vehicle given a String array dataToken.
     * Checks if each of the dataTokens are valid.
     * @param dataToken DataToken that contains the plate, date, make, and mileage
     */
    public Vehicle(String[] dataToken) {
        if (Vehicle.isValidVehicle(dataToken)){
            this.plate = dataToken[1];
            this.obtained = new Date(dataToken[2]);
            this.make = Make.valueOf(dataToken[3].toUpperCase());
            this.mileage = Integer.parseInt(dataToken[4]);
            this.campus = Campus.valueOf(Frontend.capitalize(dataToken[5]));
        }
    }

    /**
     * Gets the Vehicle type based on the last letter of the license plate.
     * @param plate The given plate of the Vehicle that is being checked
     * @return X returns the type truck, D returns the type utility, S returns the type sedan.
     */
    public String getType(String plate) {
        switch (plate.substring(plate.length() - 1)){
            case "X" -> { return "truck"; }
            case "D" -> { return "utility"; }
            case "S" -> { return "sedan"; }
            default -> {
                return null;
            }
        }
    }

    /**
     * Checks if each of the dataToken can make a valid vehicle.
     * @param dataToken String array dataToken with plate, date, make, and mileage.
     * @return true if all string array elements are valid vehicle parts; false otherwise.
     */
    public static boolean isValidVehicle(String[] dataToken) {
        Date obtained = new Date(dataToken[2]);

        if (!obtained.isCalendarDateValid(dataToken[2])) {
            return false;
        } else if (!Make.isValidMake(dataToken[3])) {
            return false;
        } else if (!Vehicle.isValidMileage(dataToken[4])) {
            return false;
        } else if (!isValidPlate(dataToken[1])) {
            return false;
        } else if (!Campus.isValidCampus(dataToken[5])) {
            Frontend.printInvalidBookingMessage("Campus Invalid Location", null, null, null, null, dataToken[5]);
            return false;
        } else { return true; }
    }

    /**
     * Checks if the plate string is a valid plate.
     * Checks the character amount, if the first 5 characters are digit and if the last char is equal to S, D, or X.
     * @param plate The string plate that is checked.
     * @return true if it's a valid plate; false otherwise.
     */
    public static boolean isValidPlate(String plate) {
        plate = plate.trim();
        if (plate.length() != 6){
            Frontend.printInvalidPlateMessage("6 Character Error", plate);
            return false;
        }

        for (int i = 0; i < plate.length()-1; i++) {
            if(!Character.isDigit(plate.charAt(i))){
                Frontend.printInvalidPlateMessage("First 5 Numbers Error", plate);
                return false;
            }
        }
        String end = plate.substring(plate.length() - 1);

        if (!end.equals("S") && !end.equals("D") && !end.equals("X")){
            Frontend.printInvalidPlateMessage("Not Valid Vehicle Type Error", plate);
            return false;
        }
        return true;
    }

    /**
     * Checks if the mileage is greater than 0.
     * @param mileage The mileage to be checked.
     * @return true if the mileage is greater than 0; false otherwise.
     */
    public static boolean isValidMileage(String mileage) {
        try {
            int int_mileage = Integer.parseInt(mileage);

            if (int_mileage > 0) {
                return true;
            }
            else {
                Frontend.printInvalidMileageMessage("Invalid Num Mileage", null, int_mileage);
                return false;
            }
        } catch (NumberFormatException e) {
            Frontend.printInvalidMileageMessage("Invalid String Input", mileage, 0);
            return false;
        }
    }

    /**
     * Override equals method that checks if the vehicles are equal.
     * Checks through the getClass method and through the vehicle plate.
     * @param object  The reference object with which to compare.
     * @return true if vehicle is the same; false if they are different.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Vehicle other = (Vehicle) object;
        return this.plate.equals(other.plate);
    }

    /**
     * Override the toString method to return the plate, make, date obtained, and the mileage.
     * @return a string of all the vehicle traits together.
     */
    @Override
    public String toString() {
        return plate + "[" + make + ":" + getType(plate) + "] " + obtained + " [mileage:" + mileage + "] [" + campus + "]";
    }

    /**
     * Override compareTo method that compares the plate of two vehicles.
     * @param vehicle the reference vehicle with which to compare to.
     * @return 0 if they are the same; -1 or 1 if they are different.
     */
    @Override
    public int compareTo(Vehicle vehicle) {
        return plate.compareTo(vehicle.plate);
    }

//    /**
//     * Testbed for 3 test cases of Testing Specifications for compareTo method.
//     * @param args
//     */
//    public static void main(String[] args) {
//        //1 output
//        Vehicle vehicle1 = new Vehicle("80671S", null, Make.CHEVY, 10293);
//        Vehicle vehicle2 = new Vehicle("71707X", null, Make.CHEVY, 10293);
//        System.out.println(vehicle1.compareTo(vehicle2));
//
//        //0 output
//        Vehicle vehicle3 = new Vehicle("58718D", null, Make.CHEVY, 10293);
//        Vehicle vehicle4 = new Vehicle("58718D", null, Make.CHEVY, 10293);
//        System.out.println(vehicle3.compareTo(vehicle4));
//
//        //-1 output
//        Vehicle vehicle5 = new Vehicle("58718D", null, Make.CHEVY, 10293);
//        Vehicle vehicle6 = new Vehicle("65402A", null, Make.CHEVY, 10293);
//        System.out.println(vehicle5.compareTo(vehicle6));
//
//
//    }
}
