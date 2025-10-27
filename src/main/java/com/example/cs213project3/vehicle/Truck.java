package com.example.cs213project3.vehicle;

import com.example.cs213project3.rental.Campus;
import com.example.cs213project3.rental.Date;
import com.example.cs213project3.rental.Make;

/**
 *  The Truck class extends from the Vehicle class.
 *  It has its own charge per mile, surcharge per mile and max surcharge.
 *  @author Lana Huang
 */
public class Truck extends Vehicle{
    private final double perMile = 2.99;
    private final double flatFee = 39.99;

    /**
     * Gets the flat fee for trucks.
     * @return the flat fee.
     */
    public double getFlatFee() {
        return flatFee;
    }

    /**
     * Constructs Truck given the plate, date obtained, make, and mileage.
     * @param plate String license plate number.
     * @param obtained Date obtained.
     * @param make Make of the Truck.
     * @param mileage Mileage of the Truck.
     * @param campus Campus that the Truck is from.
     */
    public Truck(String plate, Date obtained, Make make, int mileage, Campus campus) {
        super(plate, obtained, make, mileage, campus);
    }

    /**
     * Constructs Truck given a String array dataToken.
     * Checks if each of the dataTokens are valid.
     * @param dataToken DataToken that contains the plate, date, make, mileage, and campus
     */
    public Truck(String[] dataToken) {
        super(dataToken);
    }

    /**
     * Overrides the charge method from Vehicle to get the correct charge based on Truck chargerPerMile.
     * @param mileageUsed the given mileageUsed that is used to calculate the charge.
     * @return a double that's the cost of the charge.
     */
    @Override
    public double charge(int mileageUsed) {
        return mileageUsed * perMile;
    }

    /**
     * Overrides the surcharge method from Vehicle to get the correct surcharge based on Truck flat fee.
     * If there's a surcharge it will equal the flat fee.
     * @param mileageUsed the given mileageUsed.
     * @return a double equal to the flat fee; otherwise 0.0 if no surcharge
     */
    @Override
    public double surcharge(int mileageUsed, boolean surcharge) {
        if (surcharge) {
            return flatFee;
        } else { return 0.0;}
    }
}
