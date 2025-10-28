package com.example.cs213project3;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.cs213project3.vehicle.*;
import com.example.cs213project3.rental.*;
import com.example.cs213project3.util.*;

import java.text.DecimalFormat;

public class Controller {
    @FXML private ComboBox<String> campusComboBox;
    @FXML private ComboBox<String> employeeComboBox;
    @FXML private TextField licensePlateField;
    @FXML private DatePicker dateObtainedField;
    @FXML private ToggleGroup vehicleTypeGroup;
    @FXML private static TextArea outputArea;


    public static Fleet fleet = new Fleet();
    public static Reservation bookings = new Reservation();
    public static TripList tripList = new TripList();

    @FXML
    private void initialize() {
        employeeComboBox.getItems().addAll("Patel", "Lim", "Zimnes", "Harper", "Kaur", "Taylor", "Ramesh", "Ceravolo");
        campusComboBox.getItems().addAll("Busch", "Livingston", "Cook", "Newark", "Camden");
    }

    @FXML
    private void addToFleetButton() {
        try {
            String plate = licensePlateField.getText();
            String date = dateObtainedField.getValue().toString();
            RadioButton selectedMake = (RadioButton) vehicleTypeGroup.getSelectedToggle();
            String make = selectedMake.getText();
            String employee = employeeComboBox.getValue();
            String campus = campusComboBox.getValue();

            String[] dataToken = {"A", plate, date, make, employee, campus};

            if (Vehicle.isValidVehicle(dataToken)) {
                Vehicle newVehicle = null;
                switch (plate.substring(plate.length() - 1)){
                    case "X" -> newVehicle = new Truck(dataToken);
                    case "D" -> newVehicle = new Utility(dataToken);
                    case "S" -> newVehicle = new Sedan(dataToken);
                    default -> {
                        outputArea.appendText("\nUnknown vehicle type:" + dataToken[1]);
                        return;
                    }
                }

                if (!fleet.contains(newVehicle)) {
                    fleet.add(newVehicle);
                    String vehicleConfirmation = newVehicle.toString() + " has been added to the fleet.";
                    outputArea.appendText("\n" + vehicleConfirmation);
                }

            } else { return; }
        } catch (Exception e) {
            outputArea.appendText("\nError adding vehicle: " + e.getMessage());
        }
    }

    @FXML
    public static void printNoVehicleInFleet() {
        outputArea.appendText("\nThere is no vehicle in the fleet.");
    }

    @FXML
    public static void printFleetMessages(String messageType, Integer i) {
        switch (messageType) {
            case "Start List" -> outputArea.appendText("\n*List of vehicles in the fleet, ordered by location/make/date obtained.");
            case "End List" -> outputArea.appendText("\n*end of util.");
            case "Fleet Details" -> outputArea.appendText(fleet.get(i).toString());
        }
    }
    
    @FXML
    public static void printInvalidMileageMessage(String errorType, String error, int mileage) {
        switch (errorType) {
            case "Invalid Num Mileage" -> outputArea.appendText(mileage + " - invalid mileage.");
            case "Invalid String Input" -> outputArea.appendText("For input string: " + '"' + error + '"' + " - not a valid mileage.");
        }
    }
    
    @FXML
    public static void printInvalidMakeMessage(String make) {
        String invalidMake = make + " - invalid make.";
        outputArea.appendText(invalidMake);
    }
    
    @FXML
    public static void printInvalidDate(String date){
        String invalidDateMessage = date + " - invalid calendar date.";
        outputArea.appendText(invalidDateMessage);
    }
    
    @FXML    
    public static void printTodayOrFuture(String date) {
        String invalid_command = date + " - is today or a future date.";
        outputArea.appendText(invalid_command);
    }
    
    @FXML
    public static void printBeginDateErrorMessage(String errorType, Date begin) {
        switch (errorType) {
            case "Valid Error" -> outputArea.appendText(begin + " - beginning date is not a valid calendar date.");
            case "Today or Future Error" -> outputArea.appendText(begin + " - beginning date is not today or a future date.");
            case "Beyond 3 Months Error" -> outputArea.appendText(begin + " - beginning date beyond 3 months.");
        }
    }
    
    @FXML
    public static void printEndDateErrorMessage(String errorType, Date begin, Date end) {
        switch (errorType) {
            case "Valid Error" -> outputArea.appendText(begin + " - ending date is not a valid calendar date.");
            case "Equal to or Later Error" -> outputArea.appendText(end + " - ending date must be equal or after the beginning date " + begin);
            case "More than a Week Error" -> outputArea.appendText(begin + " ~ " + end + " - duration more than a week.");
        }
    }
    
    @FXML    
    public static void printInvalidBookingMessage(String errorType, String plate, String employee, Date begin, Date end, String dropoff) {
        switch (errorType) {
            case "Vehicle does not Exist Error" -> outputArea.appendText(plate + " is not in the fleet.");
            case "Vehicle not Available Error" -> outputArea.appendText(plate + " - booking with " + begin + " ~ " + end + " not available.");
            case "Employee not Eligible Error" -> outputArea.appendText(employee + " - not an eligible employee to book.");
            case "Employee Conflict Error" -> outputArea.appendText(employee + " - has an existing booking conflicting with booking date " + begin + " ~ " + end);
            case "Campus Invalid Location" -> outputArea.appendText(dropoff + " - invalid location.");
        }
    }
    
    @FXML
    public static void printInvalidPlateMessage(String errorType, String plate) {
        switch (errorType) {
            case "6 Character Error" -> outputArea.appendText(plate + " - license plate number must be exactly 6 characters.");
            case "Not Valid Vehicle Type Error" -> outputArea.appendText(plate + " - last character is not a valid vehicle type.");
            case "First 5 Numbers Error" -> outputArea.appendText(plate + " - first 5 characters must be numbers.");
        }
    }
    
    @FXML
    public static void printLoadVehicleMessage(String messageType, String error, int numLoaded) {
        switch (messageType) {
            case "Unknown Vehicle Type" -> outputArea.appendText("Unknown vehicle type: " + error);
            case "Vehicles Loaded Message" -> outputArea.appendText(numLoaded + " vehicles loaded.");
            case "Text file not found" -> outputArea.appendText("Text file not found: " + error);
        }
    }
    
    @FXML
    public static void printValidCancelBookingMessage(Date begin, Date end, String plate) {
        String validCancelBookingMessage = plate + ":" + begin + " ~ " + end + " has been canceled.";
        outputArea.appendText(validCancelBookingMessage);
    }
    
    @FXML
    public static void printInvalidCancelBookingMessage(Date begin, Date end, String plate) {
        String invalidCancelBookingMessage = plate + ":" +begin + " ~ " + end + " - cannot find the booking.";
        outputArea.appendText(invalidCancelBookingMessage);
    }
    
    @FXML
    public static void printCostMessage(String messageType, Trip[] trips, Integer i, Double cost, String currentDept) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        switch (messageType) {
            case "Empty List" -> outputArea.appendText("There is no archived trips for the cost report.");
            case "Start List" -> outputArea.appendText("*List of charges ordered by department.");
            case "End List" -> outputArea.appendText("*end of util.\n");
            case "Department Details" -> outputArea.appendText("--" + currentDept + "--");
            case "Department Total" -> outputArea.appendText("  <*>Department total: $ " + df.format(cost));
            case "Surcharged Trip Details" -> outputArea.appendText("\t" + (trips[i].getBooking().getVehicle().getPlate() + " " + trips[i].getBooking().getBegin() + " ~ " + trips[i].getBooking().getEnd() + " mileage(old): " + trips[i].getBeginMileage() + " mileage(new): " + trips[i].getEndMileage() + " mileage(used): " + trips[i].mileageUsed() + " [dropped off: " + trips[i].getBooking().getCampusDropoff().name() + "**]"));
            case "Surcharged Cost Details" -> outputArea.appendText("\t\t[charge: $" + df.format(trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed())) + "] [surcharge: $" + df.format(trips[i].getBooking().getVehicle().surcharge(trips[i].mileageUsed(), trips[i].hasSurcharge())) + "] [total charge: $ " + df.format(cost) + "]");
            case "Not Surcharged Trip Details" -> outputArea.appendText("\t" + (trips[i].getBooking().getVehicle().getPlate() + " " + trips[i].getBooking().getBegin() + " ~ " + trips[i].getBooking().getEnd() + " mileage(old): " + trips[i].getBeginMileage() + " mileage(new): " + trips[i].getEndMileage() + " mileage(used): " + trips[i].mileageUsed() + " [dropped off: " + trips[i].getBooking().getCampusDropoff().name() + "]"));
            case "Not Surcharged Cost Details" -> outputArea.appendText("\t\t[charge: $" + df.format(trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed())) + "] [surcharge: no] [total charge: $ " + df.format(trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed())) + "]");
        }
    }
    
    @FXML
    public static void printTripsMessage(String messageType, Trip[] trips, Integer i, String minNode) {
        switch (messageType) {
            case "Empty List" -> outputArea.appendText("There is no completed trips.");
            case "Start List" -> outputArea.appendText("*List of completed trips ordered by license plate and ending date.");
            case "End List" -> outputArea.appendText("*end of util.\n");
            case "Trip Details" -> outputArea.appendText(minNode);
        }
    }
    
    @FXML
    public static void printBookingsMessage(String messageType, String bookingDetails, String currentDept){
        switch (messageType) {
            case "Empty List" -> outputArea.appendText("There is no booking record.");
            case "Start City List" -> outputArea.appendText("*List of reservations ordered by location/license plate/beginning date.");
            case "Start Dept List" -> outputArea.appendText("*List of reservations ordered by department and employee.");
            case "End List" -> outputArea.appendText("*end of util.\n");
            case "City Booking Details" -> outputArea.appendText(bookingDetails);
            case "Dept Booking Details" -> outputArea.appendText("\t" + bookingDetails);
            case "Department Details" -> outputArea.appendText("--" + currentDept + "--");
        }
    }
    
    @FXML
    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) return string;
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
    
    
    
}
