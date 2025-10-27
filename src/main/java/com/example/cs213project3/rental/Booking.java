package com.example.cs213project3.rental;

import com.example.cs213project3.vehicle.Vehicle;

/**
 The Booking class manages vehicle reservations made by an employee for specific dates
 It allows the user to obtain information including dates, vehicle details, employee details, and campus details while also checking validity of the dates.
 @author Sharon Chen
 */
public class Booking {
    private Date begin;
    private Date end;
    private Vehicle vehicle;
    private Employee employee;
    private Campus dropoff;

    /**
     * Constructs a Booking object with the specified start date, end date, vehicle, and employee.
     * @param begin the start date of the booking period
     * @param end the end date of the booking period
     * @param vehicle the vehicle assigned to this booking
     * @param employee the employee who made this booking
     * @param dropoff the campus the individual is leaving to in this booking
     */
    public Booking(Date begin, Date end, Vehicle vehicle, Employee employee, Campus dropoff){
        this.begin = begin;
        this.end = end;
        this.vehicle = vehicle;
        this.employee = employee;
        this.dropoff = dropoff;
    }

    /**
     * Returns the start date of this booking period.
     * @return the begin date of the booking
     */
    public Date getBegin(){
        return begin;
    }

    /**
     * Returns the end date of this booking period.
     * @return the end date of the booking
     */
    public Date getEnd(){
        return end;
    }

    /**
     * Returns the vehicle assigned to this booking.
     * @return the vehicle for this booking
     */
    public Vehicle getVehicle(){
        return vehicle;
    }

    /**
     * Returns the employee who made this booking.
     * @return the employee associated with this booking
     */
    public Employee getEmployee(){
        return employee;
    }

    /**
     * Returns the campus the individual is being picked up on in this booking.
     * @return the campus pickup associated with this booking
     */
    public Campus getCampusPickup() { return vehicle.getCampus();}

    /**
     * Returns the campus the individual is being dropped off on in this booking.
     * @return the campus dropoff associated with this booking
     */
    public Campus getCampusDropoff() { return dropoff;}

    /**
     * Validates whether a booking request meets all the rules and constraints.
     * @param dataToken the array containing booking data tokens
     * @return true if all validation criteria are met; return false otherwise
     */
    public static boolean isValidBooking(String[] dataToken) {
        Date begin = new Date(dataToken[1]);
        Date end = new Date(dataToken[2]);
        String plate = dataToken[3];
        String employee = dataToken[4];
        String dropoff = dataToken[5];


        if (!Frontend.fleet.contains(Fleet.getVehicle(plate))) {
            Frontend.printInvalidBookingMessage("Vehicle does not Exist Error", plate, null, null, null, null);
            return false;
        } else if (Reservation.isVehicleConflict(begin, end, plate)) {
            Frontend.printInvalidBookingMessage("Vehicle not Available Error", plate, null, begin, end, null);
            return false;
        } else if (!Employee.isValidEmployee(employee)) {
            Frontend.printInvalidBookingMessage("Employee not Eligible Error", null, employee, null,null, null);
            return false;
        } else if (Reservation.isEmployeeConflict(begin, end, employee) != null) {
            Frontend.printInvalidBookingMessage("Employee Conflict Error", null, employee, begin, end, null);
            return false;
        } else if (!Campus.isValidCampus(dropoff)) {
            Frontend.printInvalidBookingMessage("Campus Invalid Location", null, null, null, null, dropoff);
            return false;
        }
        return true;
    }

    /**
     * Validates the date parameters for a booking request to ensure they meet scheduling requirements.
     * @param dataToken the array containing booking data tokens with date information
     * @return true if all date validation rules are satisfied; return false otherwise
     */
    public static boolean isValidBookingDate(String[] dataToken) {
        Date begin = new Date(dataToken[1]);
        Date end = new Date(dataToken[2]);

        if (!begin.isBookingDateValid("begin", begin)) {
            return false;
        } else if (!begin.isTodayOrFuture()) {
            Frontend.printBeginDateErrorMessage("Today or Future Error", begin);
            return false;
        } else if (!begin.isWithin3Months()) {
            Frontend.printBeginDateErrorMessage("Beyond 3 Months Error", begin);
            return false;
        } else if (!end.isBookingDateValid("end", end)) {
            return false;
        } else if (end.compareTo(begin) < 0) {
            Frontend.printEndDateErrorMessage("Equal to or Later Error", begin, end);
            return false;
        } else if (!end.isWithin7Days(begin, end)) {
            Frontend.printEndDateErrorMessage("More than a Week Error", begin, end);
            return false;
        }
        return true;
    }

    /**
     * Compares if the Booking object is the same as the object given
     * @param comparison the object to compare with the booking
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

        Booking compareBooking = (Booking) comparison;
        return ((this.begin == compareBooking.begin) && (this.end == compareBooking.end) && (this.vehicle == compareBooking.vehicle) && (this.employee == compareBooking.employee));
    }

    /**
     * Creates a string to represent the booking details
     * @return a formatted string containing booking details including the vehicle license plate, booking dates, and employee details
     */
    @Override
    public String toString(){
        return (vehicle.getPlate() + ":" + vehicle.getMake() + " [" + vehicle.getCampus() + "]" + " " + begin + " ~ " + end + " [drop off:" + dropoff.name() + "] [" + employee.name().toUpperCase() + "]");
    }

}