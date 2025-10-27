package com.example.cs213project3.vehicle;

import com.example.cs213project3.rental.Campus;
import com.example.cs213project3.rental.Date;
import com.example.cs213project3.rental.Make;

/**
 *  The Utility class extends from the Vehicle class.
 *  It has its own charge per mile, surcharge per mile and max surcharge.
 *  @author Lana Huang
 */
public class Utility extends Vehicle{
    private final double chargePerMile = 1.99;
    private final double surchargePerMile = .30;
    private final double surchargeMax = 35.99;

    /**
     * Gets the surchargeMax
     * @return the surchargeMax
     */
    public double getSurchargeMax() {
        return surchargeMax;
    }

    /**
     * Constructs Utility given the plate, date obtained, make, and mileage.
     * @param plate String license plate number.
     * @param obtained Date obtained.
     * @param make Make of the Utility.
     * @param mileage Mileage of the Utility.
     * @param campus Campus that the Utility is from.
     */
    public Utility(String plate, Date obtained, Make make, int mileage, Campus campus) {
        super(plate, obtained, make, mileage, campus);
    }

    /**
     * Constructs Utility given a String array dataToken.
     * Checks if each of the dataTokens are valid.
     * @param dataToken DataToken that contains the plate, date, make, mileage, and campus
     */
    public Utility(String[] dataToken) {
        super(dataToken);
    }

    /**
     * Overrides the charge method from Vehicle to get the correct charge based on Utility chargerPerMile.
     * @param mileageUsed the given mileageUsed that is used to calculate the charge.
     * @return a double that's the cost of the charge.
     */
    @Override
    public double charge(int mileageUsed) {
        return mileageUsed * chargePerMile;
    }

    /**
     * Overrides the surcharge method from Vehicle to get the correct surcharge based on Utility surchargerPerMile.
     * It first checks if there is a surcharge before calculating it.
     * If the surcharge exceeds the surchargeMax, then the cost of the surcharge is the surchargeMax.
     * @param mileageUsed the given mileageUsed that is used to calculate the surcharge.
     * @return a double that's the cost of the surcharge; otherwise 0.0 if no surcharge.
     */
    @Override
    public double surcharge(int mileageUsed, boolean surcharge) {
        if (surcharge) {
            double cost = mileageUsed * surchargePerMile;

            if (cost > surchargeMax) {
                return surchargeMax;
            }
            return cost;
        } else { return 0.0; }
    }
}
