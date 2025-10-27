package com.example.cs213project3.rental;

import com.example.cs213project3.vehicle.Sedan;
import com.example.cs213project3.vehicle.Truck;
import com.example.cs213project3.vehicle.Utility;
import com.example.cs213project3.vehicle.Vehicle;
import com.example.cs213project3.util.Sort;
import java.text.DecimalFormat;

import java.util.Scanner;

/**
 * The Frontend class serves as the main interface for the Vehicle Management System.
 * It handles user input, command processing, and coordinates between different system components.
 * @authors Lana Huang, Sharon Chen
 */
public class Frontend {
  public static Fleet fleet = new Fleet();
  public static Reservation bookings = new Reservation();
  public static TripList tripList = new TripList();

    /**
     * Starts the Vehicle Management System and processes user commands.
     */
    public static void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Vehicle Management System is running.\n");

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            if (input.equals("Q")) {
                quit();
                break;
            }

            String[] dataToken = input.split("\\s+");
            String command = dataToken[0];

            checkCommand(command, dataToken);
        }
        scanner.close();
    }

    /**
     * Processes the given command and delegates to the appropriate method.
     * @param command the command to execute
     * @param dataToken the array of command parameters
     */
    public static void checkCommand(String command, String[] dataToken) {
        switch (command) {
            case "A" -> addVehicle(dataToken);
            case "D" -> removeVehicle(dataToken);
            case "B" -> bookVehicle(dataToken);
            case "C" -> cancelBooking(dataToken);
            case "L" -> Fleet.loadVehicles(fleet);
            case "R" -> returnVehicle(dataToken);
            case "PF" -> Sort.printSortedFleet();//needs to be ordered by make then date
            case "PR" -> Sort.printBookingsByCity(); //needs to be ordered by city, then plate, then beginning date
            case "PD" -> Sort.printBookingsByDept();//needs to be ordered by department then by employee
            case "PT" -> Sort.printCompletedTrips();//needs to be ordered by ending date
            case "PC" -> Sort.printCost(); //needs to be ordered by department
            default -> {
                String invalid_command = command + " - invalid command!";
                System.out.println(invalid_command);
            }
        }
    }

    /**
     * Adds a new vehicle to the fleet if the vehicle data is valid.
     * @param dataToken the array containing vehicle data
     */
    public static void addVehicle(String[] dataToken) {
        try {
            if (Vehicle.isValidVehicle(dataToken)) {
                Vehicle newVehicle = null;

                switch (dataToken[1].substring(dataToken[1].length() - 1)){
                    case "X" -> newVehicle = new Truck(dataToken);
                    case "D" -> newVehicle = new Utility(dataToken);
                    case "S" -> newVehicle = new Sedan(dataToken);
                    default -> {
                        System.out.println("Unknown vehicle type: " + dataToken[1]);
                        return;
                    }
                }
                if (!fleet.contains(newVehicle)) {
                    fleet.add(newVehicle);
                    String vehicleConfirmation = newVehicle.toString() + " has been added to the fleet.";
                    System.out.println(vehicleConfirmation);
                }
            } else { return; }
        } catch (Exception e) {
            if (dataToken.length != 6) {
                System.out.println("Missing data tokens for adding a vehicle.");
                return;
            }
        }
    }

    /**
     * Prints an error message for invalid mileage input.
     * @param mileage the invalid mileage value
     */
    public static void printInvalidMileageMessage(String errorType, String error, int mileage) {
        switch (errorType) {
            case "Invalid Num Mileage" -> System.out.println(mileage + " - invalid mileage.");
            case "Invalid String Input" -> System.out.println("For input string: " + '"' + error + '"' + " - not a valid mileage.");
        }
    }

    /**
     * Prints an error message for invalid vehicle make.
     * @param make the invalid make value
     */
    public static void printInvalidMakeMessage(String make) {
        String invalidMake = make + " - invalid make.";
        System.out.println(invalidMake);
    }

    /**
     * Removes a vehicle from the fleet if it has no existing bookings.
     * @param dataToken the array containing vehicle plate data
     */
    public static void removeVehicle(String[] dataToken) {
        try {
            String plate = dataToken[1];

            if (Fleet.getVehicle(plate) != null) {
                Vehicle temp = Fleet.getVehicle(plate);

                if (!Reservation.isVehicleBooked(plate)) {
                    fleet.remove(temp);
                    System.out.println(plate + " has been removed from the fleet.");
                } else {
                    System.out.println(plate + " - has existing bookings; cannot be removed.");
                }

            } else {
                System.out.println(plate + " is not in the fleet.");
            }
        } catch (Exception e) {
            if (dataToken.length != 2) {
                System.out.println("Missing data tokens for removing a vehicle.");
                return;
            }

        }

    }

    /**
     * Prints a message when there are no vehicles in the fleet.
     */
    public static void printNoVehicleInFleet() {
        System.out.println("There is no vehicle in the fleet.");
    }

    /**
     * Prints an error message for invalid date input.
     * @param date the invalid date string
     */
    public static void printInvalidDate(String date){
        String invalidDateMessage = date + " - invalid calendar date.";
        System.out.println(invalidDateMessage);
    }

    /**
     * Prints a message when a date is today or in the future.
     * @param date the date that is today or future
     */
    public static void printTodayOrFuture(String date) {
        String invalid_command = date + " - is today or a future date.";
        System.out.println(invalid_command);
    }

    /**
     * Books a vehicle if the booking request passes all validation checks.
     * @param dataToken the array containing booking data
     */
    public static void bookVehicle(String[] dataToken) {
        try {
            if (Booking.isValidBookingDate(dataToken) && Booking.isValidBooking(dataToken)) {
                Date begin = new Date(dataToken[1]);
                Date end = new Date(dataToken[2]);
                String plate = dataToken[3];
                Vehicle vehicle = Fleet.getVehicle(plate);
                Employee employeeName = Employee.valueOf(dataToken[4].substring(0, 1).toUpperCase() + dataToken[4].toLowerCase().substring(1));
                Campus dropoff = Campus.valueOf(dataToken[5].substring(0,1).toUpperCase() + dataToken[5].toLowerCase().substring(1));

                Booking newBooking = new Booking(begin, end, vehicle, employeeName, dropoff);
                bookings.add(newBooking);

                System.out.println(newBooking.toString() + " booked.");
            } else {return;}
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints error messages related to begin date validation.
     * @param errorType the type of begin date error
     * @param begin the beginning date that caused the error
     */
    public static void printBeginDateErrorMessage(String errorType, Date begin) {
        switch (errorType) {
            case "Valid Error" -> System.out.println(begin + " - beginning date is not a valid calendar date.");
            case "Today or Future Error" -> System.out.println(begin + " - beginning date is not today or a future date.");
            case "Beyond 3 Months Error" -> System.out.println(begin + " - beginning date beyond 3 months.");
        }
    }

    /**
     * Prints error messages related to end date validation.
     * @param errorType the type of end date error
     * @param begin the beginning date
     * @param end the ending date that caused the error
     */
    public static void printEndDateErrorMessage(String errorType, Date begin, Date end) {
        switch (errorType) {
            case "Valid Error" -> System.out.println(begin + " - ending date is not a valid calendar date.");
            case "Equal to or Later Error" -> System.out.println(end + " - ending date must be equal or after the beginning date " + begin);
            case "More than a Week Error" -> System.out.println(begin + " ~ " + end + " - duration more than a week.");
        }
    }

    /**
     * Prints error messages related to booking validation.
     * @param errorType the type of booking error
     * @param plate the vehicle plate number
     * @param employee the employee name
     * @param begin the beginning date of the booking
     * @param end the ending date of the booking
     */
    public static void printInvalidBookingMessage(String errorType, String plate, String employee, Date begin, Date end, String dropoff) {
        switch (errorType) {
            case "Vehicle does not Exist Error" -> System.out.println(plate + " is not in the fleet.");
            case "Vehicle not Available Error" -> System.out.println(plate + " - booking with " + begin + " ~ " + end + " not available.");
            case "Employee not Eligible Error" -> System.out.println(employee + " - not an eligible employee to book.");
            case "Employee Conflict Error" -> System.out.println(employee + " - has an existing booking conflicting with booking date " + begin + " ~ " + end);
            case "Campus Invalid Location" -> System.out.println(dropoff + " - invalid location.");
        }
    }

    /**
     * Prints error messages if the license plate is invalid
     * @param errorType the type of plate error
     * @param plate the vehicle plate number
     */
    public static void printInvalidPlateMessage(String errorType, String plate) {
        switch (errorType) {
            case "6 Character Error" -> System.out.println(plate + " - license plate number must be exactly 6 characters.");
            case "Not Valid Vehicle Type Error" -> System.out.println(plate + " - last character is not a valid vehicle type.");
            case "First 5 Numbers Error" -> System.out.println(plate + " - first 5 characters must be numbers.");
        }
    }

    /**
     * Prints messages related to loading vehicles
     * @param messageType the type of message to print
     * @param error the error details if applicable
     * @param numLoaded the number of vehicles loaded if applicable
     */
    public static void printLoadVehicleMessage(String messageType, String error, int numLoaded) {
        switch (messageType) {
            case "Unknown Vehicle Type" -> System.out.println("Unknown vehicle type: " + error);
            case "Vehicles Loaded Message" -> System.out.println(numLoaded + " vehicles loaded.");
            case "Text file not found" -> System.out.println("Text file not found: " + error);
        }
    }

    /**
     * Prints a confirmation message when a booking is successfully canceled.
     * @param begin the beginning date of the canceled booking
     * @param end the ending date of the canceled booking
     * @param plate the vehicle plate number
     */
    public static void printValidCancelBookingMessage(Date begin, Date end, String plate) {
        String validCancelBookingMessage = plate + ":" + begin + " ~ " + end + " has been canceled.";
        System.out.println(validCancelBookingMessage);
    }

    /**
     * Prints an error message when a booking to cancel cannot be found.
     * @param begin the beginning date of the booking
     * @param end the ending date of the booking
     * @param plate the vehicle plate number
     */
    public static void printInvalidCancelBookingMessage(Date begin, Date end, String plate) {
        String invalidCancelBookingMessage = plate + ":" +begin + " ~ " + end + " - cannot find the booking.";
        System.out.println(invalidCancelBookingMessage);
    }

    /**
     * Cancels a booking if it exists and passes validation checks.
     * @param dataToken the array containing cancel booking data
     */
    public static void cancelBooking(String[] dataToken) {
        Date begin = new Date(dataToken[1]);
        Date end = new Date(dataToken[2]);
        String plate = dataToken[3];

        if (!begin.isValid() || !begin.isTodayOrFuture() || !end.isValid() || !end.isTodayOrFuture()){
            printInvalidDate(plate);
            return;
        }
        if (Reservation.findBookingForCancelBooking(begin, end, plate) == null){
            printInvalidCancelBookingMessage(begin, end, plate);
            return;
        }
        else if (Reservation.findBookingForCancelBooking(begin, end, plate) != null){
            bookings.remove(Reservation.findBookingForCancelBooking(begin, end, plate));
            printValidCancelBookingMessage(begin, end, plate);
        }
    }

    /**
     * Processes a vehicle return and creates a trip record if all validations pass.
     * @param dataToken the array containing return vehicle data
     */
    public static void returnVehicle(String[] dataToken) {
        Date returnDate = new Date(dataToken[1]);
        String plate = dataToken[2];

        if (Reservation.findBookingForReturnVehicle(returnDate, plate) == null) {
            String cannotFindBookingMessage = plate + " booked with ending date " + returnDate + " - cannot find the booking.";
            System.out.println(cannotFindBookingMessage);
            return;

        } else if (!Reservation.isReturnEarliestEnd(returnDate)) {
            String notEarliestEndDateMessage = plate + " booked with end date " + returnDate + " - returning not in order of ending date.";
            System.out.println(notEarliestEndDateMessage);
            return;

        } else if (!Vehicle.isValidMileage(dataToken[3])) {
            return;

        } else if (Reservation.findBookingForReturnVehicle(returnDate, plate).getVehicle().getMileage() >= Integer.parseInt(dataToken[3])) {
            String invalidMileageMessage = "Invalid mileage - current mileage: " + Reservation.findBookingForReturnVehicle(returnDate, plate).getVehicle().getMileage() + " entered mileage: " + Integer.parseInt(dataToken[3]);
            System.out.println(invalidMileageMessage);
            return;

        } else {
            int mileage = Integer.parseInt(dataToken[3]);
            Booking booking = Reservation.findBookingForReturnVehicle(returnDate, plate);

            Trip newTrip = new Trip(booking, booking.getVehicle().getMileage(), mileage);
            Node node = new Node(newTrip);
            tripList.add(node);

            System.out.println("Trip completed: " + newTrip.toString());

            booking.getVehicle().setMileage(mileage);
            booking.getVehicle().setCampus(booking.getCampusDropoff());
            bookings.remove(booking);
        }

    }

    /**
     * Prints messages related to cost details
     * @param messageType the type of message to print
     * @param trips the list of trips
     * @param i the index of the trip
     * @param cost the cost associated with the messageType
     * @param currentDept the department being listed
     */
    public static void printCostMessage(String messageType, Trip[] trips, Integer i, Double cost, String currentDept) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        switch (messageType) {
            case "Empty List" -> System.out.println("There is no archived trips for the cost report.");
            case "Start List" -> System.out.println("*List of charges ordered by department.");
            case "End List" -> System.out.println("*end of util.\n");
            case "Department Details" -> System.out.println("--" + currentDept + "--");
            case "Department Total" -> System.out.println("  <*>Department total: $ " + df.format(cost));
            case "Surcharged Trip Details" -> System.out.println("\t" + (trips[i].getBooking().getVehicle().getPlate() + " " + trips[i].getBooking().getBegin() + " ~ " + trips[i].getBooking().getEnd() + " mileage(old): " + trips[i].getBeginMileage() + " mileage(new): " + trips[i].getEndMileage() + " mileage(used): " + trips[i].mileageUsed() + " [dropped off: " + trips[i].getBooking().getCampusDropoff().name() + "**]"));
            case "Surcharged Cost Details" -> System.out.println("\t\t[charge: $" + df.format(trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed())) + "] [surcharge: $" + df.format(trips[i].getBooking().getVehicle().surcharge(trips[i].mileageUsed(), trips[i].hasSurcharge())) + "] [total charge: $ " + df.format(cost) + "]");
            case "Not Surcharged Trip Details" -> System.out.println("\t" + (trips[i].getBooking().getVehicle().getPlate() + " " + trips[i].getBooking().getBegin() + " ~ " + trips[i].getBooking().getEnd() + " mileage(old): " + trips[i].getBeginMileage() + " mileage(new): " + trips[i].getEndMileage() + " mileage(used): " + trips[i].mileageUsed() + " [dropped off: " + trips[i].getBooking().getCampusDropoff().name() + "]"));
            case "Not Surcharged Cost Details" -> System.out.println("\t\t[charge: $" + df.format(trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed())) + "] [surcharge: no] [total charge: $ " + df.format(trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed())) + "]");
        }
    }

    /**
     * Prints messages related to trips
     * @param messageType the type of message to print
     * @param trips the list of trips
     * @param i the index of the trip
     */
    public static void printTripsMessage(String messageType, Trip[] trips, Integer i, String minNode) {
        switch (messageType) {
            case "Empty List" -> System.out.println("There is no completed trips.");
            case "Start List" -> System.out.println("*List of completed trips ordered by license plate and ending date.");
            case "End List" -> System.out.println("*end of util.\n");
            case "Trip Details" -> System.out.println(minNode);
        }
    }

    /**
     * Prints messages related to bookings
     * @param messageType the type of message to print
     * @param bookingDetails the booking information that needs to be printed
     * @param currentDept  the department being listed
     */
    public static void printBookingsMessage(String messageType, String bookingDetails, String currentDept){
        switch (messageType) {
            case "Empty List" -> System.out.println("There is no booking record.");
            case "Start City List" -> System.out.println("*List of reservations ordered by location/license plate/beginning date.");
            case "Start Dept List" -> System.out.println("*List of reservations ordered by department and employee.");
            case "End List" -> System.out.println("*end of util.\n");
            case "City Booking Details" -> System.out.println(bookingDetails);
            case "Dept Booking Details" -> System.out.println("\t" + bookingDetails);
            case "Department Details" -> System.out.println("--" + currentDept + "--");
        }
    }

    /**
     * Prints messages related to the fleet
     * @param messageType the type of message to print
     * @param i the index of the fleet
     */
    public static void printFleetMessages(String messageType, Integer i) {
        switch (messageType) {
            case "Start List" -> System.out.println("*List of vehicles in the fleet, ordered by location/make/date obtained.");
            case "End List" -> System.out.println("*end of util.\n");
            case "Fleet Details" -> System.out.println(Frontend.fleet.get(i));
        }
    }

    /**
     * Converts a String into the format where the first character is capitalized and the rest after is lowercase.
     * @param string the string that will have its capitalization format changed
     * @return a formatted string with capitalization of the first character and every character after is lowercase
     */
    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) return string;
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    /**
     * Terminates the Vehicle Management System.
     */
    public static void quit() {
        System.out.println("\nVehicle Management System is terminated.");
        System.exit(0);
    }

}
