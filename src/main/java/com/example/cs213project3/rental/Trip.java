package com.example.cs213project3.rental;

/**
 The Trip class manages a completed trip's booking details.
 It allows the user to track the trip information, make comparisons, and convert to strings.
 @author Sharon Chen
 */
public class Trip {
    private Booking booking;
    private int beginMileage;
    private int endMileage;
    private boolean surcharge;

    /**
     * Constructs a trip object with booking, vehicle starting mileage, and the vehicle ending mileage
     * @param booking the booking associated with trip
     * @param beginMileage the vehicle's starting mileage at the start of the trip
     * @param endMileage the vehicle's ending mileage at the end of the trip
    */
    public Trip(Booking booking, int beginMileage, int endMileage){
        this.booking = booking;
        this.beginMileage = beginMileage;
        this.endMileage = endMileage;
        this.surcharge = !booking.getCampusPickup().name().equals(booking.getCampusDropoff().name());
    }

    /**
     * Returns the booking associated with the trip
     * @return the booking object for this trip
    */
    public Booking getBooking(){
        return booking;
    }

    /**
     * Returns the vehicle's starting mileage with the trip
     * @return the vehicle's starting mileage for this trip
     */
    public int getBeginMileage() {
        return beginMileage;
    }

    /**
     * Returns the vehicle's ending mileage with the trip
     * @return the vehicle's ending mileage for this trip
     */
    public int getEndMileage(){
        return endMileage;
    }

    /**
     * Checks if the vehicle has surcharge for the trip
     * @return true if there is a surcharge; return false otherwise
     */
    public boolean hasSurcharge(){
        return surcharge;
    }

    /**
     * Returns the mileage used by using the Trip variables
     * @return the mileage used
     */
    public int mileageUsed() {
        return endMileage - beginMileage;
    }

    /**
     * Compares if the trip object is the same as the object given
     * @param comparison the object to compare with the trip
     * @return true if the objects are equal; return false otherwise
     */
    @Override
    public boolean equals(Object comparison) {
        if (this == comparison) {
            return true;
        }
        if ((comparison == null) || (this.getClass() != comparison.getClass())) {
            return false;
        }

        Trip compareTrip = (Trip) comparison;
        return ((this.booking == compareTrip.booking) && (this.beginMileage == compareTrip.beginMileage) && (this.endMileage == compareTrip.endMileage));
    }

    /**
     * Creates a string to represent the trip details
     * @return a formatted string containing trip details including the vehicle license plate, booking dates, starting mileage, and ending mileage
     */
    @Override
    public String toString(){
        int mileageUsed = endMileage - beginMileage;
      
        boolean checkSurcharge = hasSurcharge();
        if (checkSurcharge){
            return (booking.getVehicle().getPlate() + " " + booking.getBegin() + " ~ " + booking.getEnd() + " mileage(old): " + beginMileage + " mileage(new): " + endMileage + " mileage(used): " + mileageUsed + " [dropped off: " + booking.getCampusDropoff().name() + "**]" + " [picked up: " + booking.getCampusPickup().name() + "]");
        }
        else {
            return (booking.getVehicle().getPlate() + " " + booking.getBegin() + " ~ " + booking.getEnd() + " mileage(old): " + beginMileage + " mileage(new): " + endMileage + " mileage(used): " + mileageUsed + " [dropped off: " + booking.getCampusDropoff().name() + "]" + " [picked up: " + booking.getCampusPickup().name() + "]");
        }
    }
}