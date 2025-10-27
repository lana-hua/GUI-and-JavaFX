package com.example.cs213project3.rental;

import com.example.cs213project3.util.List;

/**
 *  The Reservation class manages a collection of bookings with a List.
 *  It allows the user to search, resize, add, remove, and match bookings in the list.
 *  It also allows the user to check for conflicts and print for bookings.
 *  @author Sharon Chen
 */
public class Reservation extends List<Booking> {

    /**
     * Constructs an empty array of bookings for Reservation.
     */
    public Reservation() {
        super();
    }

    /**
     * Checks if a vehicle with the given plate number is currently booked.
     * @param plate the license plate number to check
     * @return true if the vehicle is booked, false otherwise
     */
    public static boolean isVehicleBooked(String plate) {
        for (int i = 0; i < Frontend.bookings.size(); i++) {
            if (Frontend.bookings.get(i).getVehicle().getPlate().equals(plate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for a booking given the ending date and license plate.
     * @param end the ending date of the booking to find
     * @param plate the license plate number of the vehicle
     * @return the booking if found; return null otherwise
     */
    public static Booking findBookingForReturnVehicle(Date end, String plate){
        for (int i = 0; i < Frontend.bookings.size(); i++) {
            if ((Frontend.bookings.get(i).getEnd().equals(end) && (Frontend.bookings.get(i).getVehicle().getPlate().equals(plate)))) {
                return Frontend.bookings.get(i);
            }
        }
        return null;
    }

    /**
     * Searches for a booking given the beginning date, end date, and license plate.
     * @param begin the beginning date of the booking to find
     * @param end the ending date of the booking to find
     * @param plate the license plate number of the vehicle
     * @return the booking if found, null otherwise
     */
    public static Booking findBookingForCancelBooking(Date begin, Date end, String plate){
        for (int i = 0; i < Frontend.bookings.size(); i++) {
            if ((Frontend.bookings.get(i).getBegin().equals(begin)) && (Frontend.bookings.get(i).getEnd().equals(end) && (Frontend.bookings.get(i).getVehicle().getPlate().equals(plate)))) {
                return Frontend.bookings.get(i);
            }
        }
        return null;
    }

    /**
     * Checks if there is a vehicle conflict for the given dates and license plate.
     * @param begin the start date to check for conflicts
     * @param end the end date to check for conflicts
     * @param plate the license plate number to check
     * @return true if there is a vehicle conflict; return false otherwise
     */
    public static boolean isVehicleConflict(Date begin, Date end, String plate) {
        for (int i = 0; i < Frontend.bookings.size(); i++){
            Booking existingBooking = Frontend.bookings.get(i);

            if (existingBooking.getVehicle().getPlate().equals(plate)) {
                Date existingBegin = existingBooking.getBegin();
                Date existingEnd = existingBooking.getEnd();

                boolean overlaps = (begin.compareTo(existingEnd) <= 0) && (end.compareTo(existingBegin) >= 0);

                if (overlaps) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if there is an employee conflict for the given dates and employee.
     * @param begin the start date to check for conflicts
     * @param end the end date to check for conflicts
     * @param employee the employee name to check
     * @return the conflicting booking's begin date if conflict exists; return null otherwise
     */
    public static Booking isEmployeeConflict(Date begin, Date end, String employee) {
        for (int i = 0; i < Frontend.bookings.size(); i++){
            Booking existingBooking = Frontend.bookings.get(i);

            if (existingBooking.getEmployee().name().equalsIgnoreCase(employee)) {
                Date existingBegin = existingBooking.getBegin();
                Date existingEnd = existingBooking.getEnd();

                boolean overlaps = (begin.compareTo(existingEnd) <= 0) && (end.compareTo(existingBegin) >= 0);

                if (overlaps) {
                    return existingBooking;
                }
            }
        }
        return null;
    }

    /**
     * Checks if the given return date is the earliest end date among all bookings.
     * @param returnDate the return date to check
     * @return true if the return date is the earliest end date; return false otherwise
     */
    public static boolean isReturnEarliestEnd (Date returnDate){
        if (Frontend.bookings.isEmpty()) {
            return true;
        }
        Date earliest = Frontend.bookings.get(0).getEnd();

        for (int i = 1; i < Frontend.bookings.size(); i++) {
            Date endDate = Frontend.bookings.get(i).getEnd();
            if (endDate.compareTo(earliest) < 0) {
                earliest = endDate;
            }
        }

        return returnDate.compareTo(earliest) == 0;
    }

}
