package com.example.cs213project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import com.example.cs213project3.vehicle.*;
import com.example.cs213project3.rental.*;
import com.example.cs213project3.util.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



import java.io.File;
import java.text.DecimalFormat;

/**
 * The main controller class for the Vehicle Management System GUI.
 * This class manages all user interface components in the View FXML file.
 * It handles user interactions, updates the output display, and coordinates communication between the view and the application logic.
 * @author Lana Huang, Sharon Chen
 */
public class Controller {
    private static Controller instance;

    @FXML
    private TextArea outputArea = new TextArea();

    //Vehicle Management Tab
    @FXML
    private ComboBox<String> campusComboBox;
    @FXML
    private TextField mileageField, licensePlateField;
    @FXML
    private DatePicker dateObtainedField;
    @FXML
    private ToggleGroup vehicleTypeGroup;

    // Booking & Reservation Management Tab
    @FXML DatePicker beginDateBooking;
    @FXML DatePicker endDateBooking;
    @FXML ComboBox<String> employeeBooking;
    @FXML ComboBox<String> dropoffCampusBookingComboBox;
    @FXML public ComboBox<String> vehicleBookingVehicle;

    //Vehicle Return Tab
    @FXML private DatePicker vehicleReturnEndDate;
    @FXML public ComboBox<String> vehicleReturnVehicle;
    @FXML private TextField vehicleReturnMileage;

    //Vehicle Print Tab
    @FXML
    private ComboBox<String> printOptionComboBox;

    public static Fleet fleet = new Fleet();
    public static Reservation bookings = new Reservation();
    public static TripList tripList = new TripList();

    /**
     * This method sets the initial values for the GUI objects.
     * Fills the ComboBox, campusComboBox and dropoffCampusBookingComboBox with Strings of the campuses
     * Fills the ComboBox, employeeBooking with Strings of the employee names
     * Fill the ComboBox, printOptionComboBox with Strings of the different reports that can be printed
     * Sets the outputArea settings to wrap text and sets the text to the initial greeting
     */
    @FXML
    private void initialize() {
        instance = this;
        campusComboBox.getItems().addAll("Busch", "Livingston", "Cook", "Newark", "Camden");
        dropoffCampusBookingComboBox.getItems().addAll("Busch", "Livingston", "Cook", "Newark", "Camden");
        employeeBooking.getItems().addAll("Patel", "Lim", "Zimnes", "Harper", "Kaur", "Taylor", "Ramesh", "Ceravolo");
        printOptionComboBox.getItems().addAll("Print Sorted Fleet", "Print Bookings by City", "Print Bookings by Dept", "Print Completed Trips", "Print Costs");
        outputArea.setWrapText(true);
        Controller.getInstance().outputArea.setText("Welcome to the Vehicle Management System");
    }

    /**
     * This method is the Getter for the controller instance
     * @return instance of the Controller
     */
    public static Controller getInstance() {
        return instance;
    }

    /**
     * This is a helper method that formats the date from the GUI DatePicker
     * @param date the given string input
     * @return the date in format
     */
    private String formatDate(String date) {
        String[] dataToken = date.split("-");
        return dataToken[1] + "/" + dataToken[2] + "/" + dataToken[0];
    }

    /**
     * Event Handler for the add to fleet button.
     * When the button "Add to Fleet" is clicked, get the following information:
     * Plate number from the TextField licensePlateField, date obtained from the DatePicker dateObtainedField, make from the RadioButton ToggleGroup vehicleTypeGroup, mileage from the TextField mileageField, campus from the TextField campusComboBox.
     * Creates new Vehicle from the information provided and adds it to the Fleet.
     * @param event the event object fired by the button click. This object encapsulates
     *              the information about the event triggered by the user.
     */
    @FXML
    private void addToFleet(ActionEvent event) {
        try {
            String plate = licensePlateField.getText();
            RadioButton selectedMake = (RadioButton) vehicleTypeGroup.getSelectedToggle();
            String mileage = mileageField.getText();
            String campus = campusComboBox.getValue();

            if (plate == null || plate.isEmpty() || dateObtainedField.getValue() == null || selectedMake == null || mileage == null || mileage.isEmpty() || campus == null || campus.isEmpty()) {
                Controller.getInstance().outputArea.appendText("\nMissing data tokens for adding a vehicle.");
                return;
            }

            String date = formatDate(dateObtainedField.getValue().toString());
            String make = selectedMake.getText();
            String[] dataToken = {"A", plate, date, make, mileage, campus};

            if (Vehicle.isValidVehicle(dataToken)) {
                Vehicle newVehicle = null;
                switch (plate.substring(plate.length() - 1)){
                    case "X" -> newVehicle = new Truck(dataToken);
                    case "D" -> newVehicle = new Utility(dataToken);
                    case "S" -> newVehicle = new Sedan(dataToken);
                    default -> {
                        Controller.getInstance().outputArea.appendText("\nUnknown vehicle type:" + dataToken[1]);
                        return;
                    }
                }
                if (!fleet.contains(newVehicle)) {
                    fleet.add(newVehicle);
                    vehicleReturnVehicle.getItems().addAll(newVehicle.getPlate());
                    vehicleBookingVehicle.getItems().addAll(newVehicle.getPlate());
                    String vehicleConfirmation = newVehicle.toString() + " has been added to the fleet.";
                    Controller.getInstance().outputArea.appendText("\n" + vehicleConfirmation);
                } else {
                    Controller.getInstance().outputArea.appendText("\nVehicle " + plate + " is already in fleet.");
                }
            }
        } catch (Exception e) {
            Controller.getInstance().outputArea.appendText("\nError adding vehicle: " + e.getMessage());
        }
    }

    /**
     * Event Handler for the Load from Text File button.
     * When the button "Choose Text File" is clicked, it will call on the loadVehicle method in Fleet.
     * It uses the helper method importFile() to allow the user to choose the vehicle file and then use the path for the loadVehicle method.
     */
    @FXML
    private void loadVehicleButton(ActionEvent event) {
        Fleet.loadVehicles(fleet, importFile());
    }

    /**
     * Helper method that allows users to choose a file from their machine to load vehicles.
     * It returns the path in a String
     */
    @FXML
    private String importFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Source File for the Import");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File sourceFile = chooser.showOpenDialog(stage); //get the reference of the source file
        if (sourceFile != null) {
            return sourceFile.getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * Event Handler for the Remove from Fleet File button to remove vehicle from fleet.
     * When the button "Remove from Fleet" is clicked, it will get the plate number from the TextField licensePlateField.
     * Removes a vehicle from the fleet if it has no existing bookings.
     */
    @FXML
    private void removeFromFleet(ActionEvent event) {
        try {
            String plate = licensePlateField.getText();
            if (plate == null || plate.isEmpty()) {
                Controller.getInstance().outputArea.appendText("\nMissing data tokens for removing a vehicle.");
                return;
            }

            if (Fleet.getVehicle(plate) != null) {
                Vehicle temp = Fleet.getVehicle(plate);

                if (!Reservation.isVehicleBooked(plate)) {
                    fleet.remove(temp);
                    Controller.getInstance().outputArea.appendText("\n" + plate + " has been removed from the fleet.");
                } else {
                    Controller.getInstance().outputArea.appendText("\n" + plate + " - has existing bookings; cannot be removed.");
                }

            } else {
                Controller.getInstance().outputArea.appendText("\n" + plate + " is not in the fleet.");
            }
        } catch (Exception e) {
            if (licensePlateField.getText().length() != 2) {
                Controller.getInstance().outputArea.appendText("\nMissing data tokens for removing a vehicle.");
            }
        }
    }

    /**
     * Event Handler for the Book Vehicle button to book a vehicle if the booking request passes all validation checks.
     * When the button "Book It" is clicked, get the following information:
     * Plate number from the TextField licensePlateField, beginDate obtained from the DatePicker beginDateBooking, endDate obtained from the DatePicker endDateBooking, employee from the ComboBox employeeBooking, campus from the ComboBox dropoffCampusBookingComboBox.
     */
    @FXML
    private void bookVehicle(ActionEvent event) {
        try {
            String employee = employeeBooking.getValue();
            String plate = vehicleBookingVehicle.getValue();
            String dropoffCampus = dropoffCampusBookingComboBox.getValue();

            if (beginDateBooking.getValue() == null || endDateBooking.getValue() == null || employee == null || employee.isEmpty() || plate == null|| plate.isEmpty() || dropoffCampus == null) {
                outputArea.appendText("\nMissing data tokens for booking vehicle.");
                return;
            }

            String beginDate = formatDate(beginDateBooking.getValue().toString());
            String endDate = formatDate(endDateBooking.getValue().toString());

            String[] dataToken = {"B", beginDate, endDate, plate, employee, dropoffCampus};

            if (Booking.isValidBookingDate(dataToken) && Booking.isValidBooking(dataToken)) {
                Date begin = new Date(dataToken[1]);
                Date end = new Date(dataToken[2]);
                Vehicle vehicle = Fleet.getVehicle(plate);
                Employee employeeName = Employee.valueOf(dataToken[4].substring(0, 1).toUpperCase() + dataToken[4].toLowerCase().substring(1));
                Campus dropoff = Campus.valueOf(dataToken[5].substring(0, 1).toUpperCase() + dataToken[5].toLowerCase().substring(1));

                Booking newBooking = new Booking(begin, end, vehicle, employeeName, dropoff);
                bookings.add(newBooking);
                String bookingConfirmation = newBooking.toString() + " booked.";
                Controller.getInstance().outputArea.appendText("\n" + bookingConfirmation);
            }
        } catch (Exception e) {
            outputArea.appendText("\n Error booking vehicle: " + e.getMessage());
        }
    }

    /**
     * Event Handler for the Cancel Booking button to cancel a booking if it exists and passes validation checks.
     * When the button "Cancel It" is clicked, it will get the following information:
     * plate number from the ComboBox Vehicle Plate,
     * dates from the DatePicker beginDateBooking and endDateBooking.
     */
    @FXML
    private void cancelBooking() {
        String plate = vehicleBookingVehicle.getValue();

        if (beginDateBooking.getValue() == null || endDateBooking.getValue() == null || plate == null || plate.isEmpty()) {
            outputArea.appendText("\nMissing data tokens to cancel booking.");
            return;
        }

        Date begin = new Date(formatDate(beginDateBooking.getValue().toString()));
        Date end = new Date(formatDate(endDateBooking.getValue().toString()));

        if (!begin.isValid() || !begin.isTodayOrFuture() || !end.isValid() || !end.isTodayOrFuture()){
            printInvalidDate(plate);
            return;
        }
        if (Reservation.findBookingForCancelBooking(begin, end, plate) == null){
            printInvalidCancelBookingMessage(begin, end, plate);
            return;
        }
        else if (Reservation.findBookingForCancelBooking(begin, end, plate) != null) {
            bookings.remove(Reservation.findBookingForCancelBooking(begin, end, plate));
            printValidCancelBookingMessage(begin, end, plate);
        }
    }

    /**
     * Event Handler for the Return Vehicle button to return a vehicle and complete a trip.
     * It will create a trip record if all validations pass.
     * When the button "Complete this Trip" is clicked, it will get the following information:
     * Plate number from the ComboBox of Vehicle plates from the fleet, mileage from the TextField from vehicleReturnMileage, ending date from the DatePicker returnDate.
     */
    @FXML
    private void returnVehicle(ActionEvent event) {
        String plate = vehicleReturnVehicle.getValue();
        String stringMileage = vehicleReturnMileage.getText();

        if (plate == null || plate.isEmpty() || vehicleReturnEndDate.getValue() == null || stringMileage == null || stringMileage.isEmpty()) {
            Controller.getInstance().outputArea.appendText("\nPlease fill out all information.");
            return;
        }
        Date returnDate = new Date(formatDate(vehicleReturnEndDate.getValue().toString()));

        if (Reservation.findBookingForReturnVehicle(returnDate, plate) == null) {
            String cannotFindBookingMessage = plate + " booked with ending date " + returnDate + " - cannot find the booking.";
            Controller.getInstance().outputArea.appendText("\n" + cannotFindBookingMessage);

        } else if (!Reservation.isReturnEarliestEnd(returnDate)) {
            String notEarliestEndDateMessage = plate + " booked with end date " + returnDate + " - returning not in order of ending date.";
            Controller.getInstance().outputArea.appendText("\n" + notEarliestEndDateMessage);

        } else if (Reservation.findBookingForReturnVehicle(returnDate, plate).getVehicle().getMileage() >= Integer.parseInt(stringMileage)) {
            String invalidMileageMessage = "Invalid mileage - current mileage: " + Reservation.findBookingForReturnVehicle(returnDate, plate).getVehicle().getMileage() + " entered mileage: " + Integer.parseInt(stringMileage);
            Controller.getInstance().outputArea.appendText("\n" + invalidMileageMessage);

        } else {
            int mileage = Integer.parseInt(stringMileage);
            Booking booking = Reservation.findBookingForReturnVehicle(returnDate, plate);

            Trip newTrip = new Trip(booking, booking.getVehicle().getMileage(), mileage);
            Node node = new Node(newTrip);
            tripList.add(node);

            Controller.getInstance().outputArea.appendText("\nTrip completed: " + newTrip.toString());

            booking.getVehicle().setMileage(mileage);
            booking.getVehicle().setCampus(booking.getCampusDropoff());
            bookings.remove(booking);
        }
    }

    /**
     * Event Handler for the Print button to print a report depending on the ComboBox Print option.
     * The ComboBox printOption has five options: Print Sorted Fleet, Print Bookings by City, Print Bookings by Dept, Print Completed Trips, Print Cost Report.
     * Each option will call the Sort method that corresponds.
     */
    @FXML
    private void printOption(ActionEvent event) {
        String printOption = printOptionComboBox.getValue();
        switch (printOption) {
            case "Print Sorted Fleet" -> Sort.printSortedFleet();
            case "Print Bookings by City" -> Sort.printBookingsByCity();
            case "Print Bookings by Dept" -> Sort.printBookingsByDept();
            case "Print Completed Trips" -> Sort.printCompletedTrips();
            case "Print Costs" -> Sort.printCost();
        }
    }

    /**
     * Prints a message when there are no vehicles in the fleet to the outputArea.
     */
    @FXML
    public static void printNoVehicleInFleet() {
        Controller.getInstance().outputArea.appendText("\nThere is no vehicle in the fleet.");
    }

    /**
     * Prints messages related to the fleet to the outputArea.
     * @param messageType the type of message to print.
     * @param i the index of the fleet.
     */
    @FXML
    public static void printFleetMessages(String messageType, Integer i) {
        switch (messageType) {
            case "Start List" -> Controller.getInstance().outputArea.appendText("\n*List of vehicles in the fleet, ordered by location/make/date obtained.");
            case "End List" -> Controller.getInstance().outputArea.appendText("\n*end of util.");
            case "Fleet Details" -> Controller.getInstance().outputArea.appendText("\n" + fleet.get(i).toString());
        }
    }

    /**
     * Prints an error message for invalid mileage input to the outputArea.
     * @param errorType errorType the type of booking error.
     * @param error error message.
     * @param mileage the invalid mileage value.
     */
    @FXML
    public static void printInvalidMileageMessage(String errorType, String error, int mileage) {
        switch (errorType) {
            case "Invalid Num Mileage" -> Controller.getInstance().outputArea.appendText("\n" + mileage + " - invalid mileage.");
            case "Invalid String Input" -> Controller.getInstance().outputArea.appendText("\nFor input string: " + '"' + error + '"' + " - not a valid mileage.");
        }
    }

    /**
     * Prints an error message for invalid vehicle make to the outputArea.
     * @param make the invalid make value
     */
    @FXML
    public static void printInvalidMakeMessage(String make) {
        String invalidMake = make + " - invalid make.";
        Controller.getInstance().outputArea.appendText("\n" + invalidMake);
    }

    /**
     * Prints an error message for invalid date input to the outputArea.
     * @param date the invalid date string
     */
    @FXML
    public static void printInvalidDate(String date){
        String invalidDateMessage = date + " - invalid calendar date.";
        Controller.getInstance().outputArea.appendText("\n" + invalidDateMessage);
    }

    /**
     * Prints a message when a date is today or in the future to the outputArea.
     * @param date the date that is today or future
     */
    @FXML
    public static void printTodayOrFuture(String date) {
        String invalid_command = date + " - is today or a future date.";
        Controller.getInstance().outputArea.appendText("\n" + invalid_command);
    }

    /**
     * Prints error messages related to begin date validation to the outputArea.
     * @param errorType the type of begin date error
     * @param begin the beginning date that caused the error
     */
    @FXML
    public static void printBeginDateErrorMessage(String errorType, Date begin) {
        switch (errorType) {
            case "Valid Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " - beginning date is not a valid calendar date.");
            case "Today or Future Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " - beginning date is not today or a future date.");
            case "Beyond 3 Months Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " - beginning date beyond 3 months.");
        }
    }

    /**
     * Prints error messages related to end date validation to the outputArea.
     * @param errorType the type of end date error
     * @param begin the beginning date
     * @param end the ending date that caused the error
     */
    @FXML
    public static void printEndDateErrorMessage(String errorType, Date begin, Date end) {
        switch (errorType) {
            case "Valid Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " - ending date is not a valid calendar date.");
            case "Equal to or Later Error" -> Controller.getInstance().outputArea.appendText("\n" + end + " - ending date must be equal or after the beginning date " + begin);
            case "More than a Week Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " ~ " + end + " - duration more than a week.");
        }
    }

    /**
     * Prints error messages related to booking validation to the outputArea.
     * @param errorType the type of booking error
     * @param plate the vehicle plate number
     * @param employee the employee name
     * @param begin the beginning date of the booking
     * @param end the ending date of the booking
     * @param dropoff the campus that the vehicle is dropped off on
     */
    @FXML
    public static void printInvalidBookingMessage(String errorType, String plate, String employee, Date begin, Date end, String dropoff) {
        switch (errorType) {
            case "Vehicle does not Exist Error" -> Controller.getInstance().outputArea.appendText("\n" + plate + " is not in the fleet.");
            case "Vehicle not Available Error" -> Controller.getInstance().outputArea.appendText("\n" + plate + " - booking with " + begin + " ~ " + end + " not available.");
            case "Employee not Eligible Error" -> Controller.getInstance().outputArea.appendText("\n" + employee + " - not an eligible employee to book.");
            case "Employee Conflict Error" -> Controller.getInstance().outputArea.appendText("\n" + employee + " - has an existing booking conflicting with booking date " + begin + " ~ " + end);
            case "Campus Invalid Location" -> Controller.getInstance().outputArea.appendText("\n" + dropoff + " - invalid location.");
        }
    }

    /**
     * Prints error messages if the license plate is invalid to the outputArea.
     * @param errorType the type of plate error
     * @param plate the vehicle plate number
     */
    @FXML
    public static void printInvalidPlateMessage(String errorType, String plate) {
        switch (errorType) {
            case "6 Character Error" -> Controller.getInstance().outputArea.appendText("\n" + plate + " - license plate number must be exactly 6 characters.");
            case "Not Valid Vehicle Type Error" -> Controller.getInstance().outputArea.appendText("\n" + plate + " - last character is not a valid vehicle type.");
            case "First 5 Numbers Error" -> Controller.getInstance().outputArea.appendText("\n" + plate + " - first 5 characters must be numbers.");
        }
    }

    /**
     * Prints messages related to loading vehicles to the outputArea.
     * @param messageType the type of message to print
     * @param error the error details if applicable
     * @param numLoaded the number of vehicles loaded if applicable
     */
    @FXML
    public static void printLoadVehicleMessage(String messageType, String error, int numLoaded) {
        switch (messageType) {
            case "Unknown Vehicle Type" -> Controller.getInstance().outputArea.appendText("\nUnknown vehicle type: " + error);
            case "Vehicles Loaded Message" -> Controller.getInstance().outputArea.appendText("\n" + numLoaded + " vehicles loaded.");
            case "Text file not found" -> Controller.getInstance().outputArea.appendText("\nText file not found: " + error);
        }
    }

    /**
     * Prints a confirmation message when a booking is successfully canceled.
     * @param begin the beginning date of the canceled booking
     * @param end the ending date of the canceled booking
     * @param plate the vehicle plate number
     */
    @FXML
    public static void printValidCancelBookingMessage(Date begin, Date end, String plate) {
        String validCancelBookingMessage = plate + ":" + begin + " ~ " + end + " has been canceled.";
        Controller.getInstance().outputArea.appendText("\n" + validCancelBookingMessage);
    }

    /**
     * Prints an error message when a booking to cancel cannot be found to the outputArea.
     * @param begin the beginning date of the booking
     * @param end the ending date of the booking
     * @param plate the vehicle plate number
     */
    @FXML
    public static void printInvalidCancelBookingMessage(Date begin, Date end, String plate) {
        String invalidCancelBookingMessage = plate + ":" +begin + " ~ " + end + " - cannot find the booking.";
        Controller.getInstance().outputArea.appendText("\n" + invalidCancelBookingMessage);
    }

    /**
     * Prints messages related to cost details to the outputArea.
     * @param messageType the type of message to print
     * @param trips the list of trips
     * @param i the index of the trip
     * @param cost the cost associated with the messageType
     * @param currentDept the department being listed
     */
    @FXML
    public static void printCostMessage(String messageType, Trip[] trips, Integer i, Double cost, String currentDept) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        switch (messageType) {
            case "Empty List" -> Controller.getInstance().outputArea.appendText("\nThere is no archived trips for the cost report.");
            case "Start List" -> Controller.getInstance().outputArea.appendText("\n*List of charges ordered by department.");
            case "End List" -> Controller.getInstance().outputArea.appendText("\n*end of util.\n");
            case "Department Details" -> Controller.getInstance().outputArea.appendText("\n--" + currentDept + "--");
            case "Department Total" -> Controller.getInstance().outputArea.appendText("\n  <*>Department total: $ " + df.format(cost));
            case "Surcharged Trip Details" -> Controller.getInstance().outputArea.appendText("\n\t" + (trips[i].getBooking().getVehicle().getPlate() + " " + trips[i].getBooking().getBegin() + " ~ " + trips[i].getBooking().getEnd() + " mileage(old): " + trips[i].getBeginMileage() + " mileage(new): " + trips[i].getEndMileage() + " mileage(used): " + trips[i].mileageUsed() + " [dropped off: " + trips[i].getBooking().getCampusDropoff().name() + "**]"));
            case "Surcharged Cost Details" -> Controller.getInstance().outputArea.appendText("\n\t\t[charge: $" + df.format(trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed())) + "] [surcharge: $" + df.format(trips[i].getBooking().getVehicle().surcharge(trips[i].mileageUsed(), trips[i].hasSurcharge())) + "] [total charge: $ " + df.format(cost) + "]");
            case "Not Surcharged Trip Details" -> Controller.getInstance().outputArea.appendText("\n\t" + (trips[i].getBooking().getVehicle().getPlate() + " " + trips[i].getBooking().getBegin() + " ~ " + trips[i].getBooking().getEnd() + " mileage(old): " + trips[i].getBeginMileage() + " mileage(new): " + trips[i].getEndMileage() + " mileage(used): " + trips[i].mileageUsed() + " [dropped off: " + trips[i].getBooking().getCampusDropoff().name() + "]"));
            case "Not Surcharged Cost Details" -> Controller.getInstance().outputArea.appendText("\n\t\t[charge: $" + df.format(trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed())) + "] [surcharge: no] [total charge: $ " + df.format(trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed())) + "]");
        }
    }

    /**
     * Prints messages related to printing the completed trips to the outputArea.
     * @param messageType the type of message to print
     * @param trips the list of trips
     * @param i the index of the trip
     * @param minNode the minNode where the trip information is stored
     */
    @FXML
    public static void printTripsMessage(String messageType, Trip[] trips, Integer i, String minNode) {
        switch (messageType) {
            case "Empty List" -> Controller.getInstance().outputArea.appendText("\nThere is no completed trips.");
            case "Start List" -> Controller.getInstance().outputArea.appendText("\n*List of completed trips ordered by license plate and ending date.");
            case "End List" -> Controller.getInstance().outputArea.appendText("\n*end of util.\n");
            case "Trip Details" -> Controller.getInstance().outputArea.appendText("\n" + minNode);
        }
    }

    /**
     * Prints messages related to bookings to the outputArea.
     * @param messageType the type of message to print
     * @param bookingDetails the booking information that needs to be printed
     * @param currentDept  the department being listed
     */
    @FXML
    public static void printBookingsMessage(String messageType, String bookingDetails, String currentDept){
        switch (messageType) {
            case "Empty List" -> Controller.getInstance().outputArea.appendText("\nThere is no booking record.");
            case "Start City List" -> Controller.getInstance().outputArea.appendText("\n*List of reservations ordered by location/license plate/beginning date.");
            case "Start Dept List" -> Controller.getInstance().outputArea.appendText("\n*List of reservations ordered by department and employee.");
            case "End List" -> Controller.getInstance().outputArea.appendText("\n*end of util.\n");
            case "City Booking Details" -> Controller.getInstance().outputArea.appendText("\n" + bookingDetails);
            case "Dept Booking Details" -> Controller.getInstance().outputArea.appendText("\n\t" + bookingDetails);
            case "Department Details" -> Controller.getInstance().outputArea.appendText("\n--" + currentDept + "--");
        }
    }

    /**
     * Converts a String into the format where the first character is capitalized and the rest after is lowercase.
     * @param string the string that will have its capitalization format changed
     * @return a formatted string with capitalization of the first character and every character after is lowercase
     */
    @FXML
    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) return string;
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
