package com.example.cs213project3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.cs213project3.vehicle.*;
import com.example.cs213project3.rental.*;
import com.example.cs213project3.util.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.DecimalFormat;

public class Controller {
    private static Controller instance;
    @FXML private TextArea outputArea = new TextArea();

    //Vehicle Management Tab
    @FXML private ComboBox<String> campusComboBox;
    @FXML private ComboBox<String> printOptionComboBox;
    @FXML private TextField mileageField;
    @FXML private TextField licensePlateField;
    @FXML private DatePicker dateObtainedField;
    @FXML private ToggleGroup vehicleTypeGroup;

    //Vehicle Reservation Tab

    public static Fleet fleet = new Fleet();
    public static Reservation bookings = new Reservation();
    public static TripList tripList = new TripList();

    @FXML
    private void initialize() {
        instance = this;
        campusComboBox.getItems().addAll("Busch", "Livingston", "Cook", "Newark", "Camden");
        printOptionComboBox.getItems().addAll("Print Sorted Fleet", "Print Bookings by City", "Print Bookings by Dept", "Print Completed Trips", "Print Costs");
        outputArea.setWrapText(true);
        outputArea.setPrefRowCount(10);
        outputArea.setScrollTop(Double.MAX_VALUE);
        Controller.getInstance().outputArea.setText("Welcome to the Vehicle Management System");
    }

    // Getter for the controller instance
    public static Controller getInstance() {
        return instance;
    }

    private String formatDate(String date) {
        String[] dataToken = date.split("-");
        return dataToken[1] + "/" + dataToken[2] + "/" + dataToken[0];
    }

    @FXML
    private void addToFleet() {
        try {
            String plate = licensePlateField.getText();
            String stringdate = String.valueOf(dateObtainedField.getValue());
            RadioButton selectedMake = (RadioButton) vehicleTypeGroup.getSelectedToggle();
            String mileage = mileageField.getText();
            String campus = campusComboBox.getValue();

            if (plate == null || plate.isEmpty() || stringdate == null || selectedMake == null || mileage == null || mileage.isEmpty() || campus == null || campus.isEmpty()) {
                Controller.getInstance().outputArea.appendText("\nPlease fill out all information.");
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
                    String vehicleConfirmation = newVehicle.toString() + " has been added to the fleet.";
                    Controller.getInstance().outputArea.appendText("\n" + vehicleConfirmation);
                }
            }
        } catch (Exception e) {
            Controller.getInstance().outputArea.appendText("\nError adding vehicle: " + e.getMessage());
        }
    }

    @FXML
    private void loadVehicleButton() {
        Fleet.loadVehicles(fleet, importFile());
    }

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

    @FXML
    private void removeFromFleet() {
        try {
            String plate = licensePlateField.getText();

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

    @FXML
    private void printOption() {
        String printOption = printOptionComboBox.getValue();
        switch (printOption) {
            case "Print Sorted Fleet" -> Sort.printSortedFleet();
            case "Print Bookings by City" -> Sort.printBookingsByCity();
            case "Print Bookings by Dept" -> Sort.printBookingsByDept();
            case "Print Completed Trips" -> Sort.printCompletedTrips();
            case "Print Costs" -> Sort.printCost();
        }
    }




    @FXML
    public static void printNoVehicleInFleet() {
        Controller.getInstance().outputArea.appendText("\nThere is no vehicle in the fleet.");
    }

    @FXML
    public static void printFleetMessages(String messageType, Integer i) {
        switch (messageType) {
            case "Start List" -> Controller.getInstance().outputArea.appendText("\n*List of vehicles in the fleet, ordered by location/make/date obtained.");
            case "End List" -> Controller.getInstance().outputArea.appendText("\n*end of util.");
            case "Fleet Details" -> Controller.getInstance().outputArea.appendText("\n" + fleet.get(i).toString());
        }
    }

    @FXML
    public static void printInvalidMileageMessage(String errorType, String error, int mileage) {
        switch (errorType) {
            case "Invalid Num Mileage" -> Controller.getInstance().outputArea.appendText("\n" + mileage + " - invalid mileage.");
            case "Invalid String Input" -> Controller.getInstance().outputArea.appendText("\nFor input string: " + '"' + error + '"' + " - not a valid mileage.");
        }
    }

    @FXML
    public static void printInvalidMakeMessage(String make) {
        String invalidMake = make + " - invalid make.";
        Controller.getInstance().outputArea.appendText("\n" + invalidMake);
    }

    @FXML
    public static void printInvalidDate(String date){
        String invalidDateMessage = date + " - invalid calendar date.";
        Controller.getInstance().outputArea.appendText("\n" + invalidDateMessage);
    }

    @FXML
    public static void printTodayOrFuture(String date) {
        String invalid_command = date + " - is today or a future date.";
        Controller.getInstance().outputArea.appendText("\n" + invalid_command);
    }

    @FXML
    public static void printBeginDateErrorMessage(String errorType, Date begin) {
        switch (errorType) {
            case "Valid Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " - beginning date is not a valid calendar date.");
            case "Today or Future Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " - beginning date is not today or a future date.");
            case "Beyond 3 Months Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " - beginning date beyond 3 months.");
        }
    }

    @FXML
    public static void printEndDateErrorMessage(String errorType, Date begin, Date end) {
        switch (errorType) {
            case "Valid Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " - ending date is not a valid calendar date.");
            case "Equal to or Later Error" -> Controller.getInstance().outputArea.appendText("\n" + end + " - ending date must be equal or after the beginning date " + begin);
            case "More than a Week Error" -> Controller.getInstance().outputArea.appendText("\n" + begin + " ~ " + end + " - duration more than a week.");
        }
    }

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

    @FXML
    public static void printInvalidPlateMessage(String errorType, String plate) {
        switch (errorType) {
            case "6 Character Error" -> Controller.getInstance().outputArea.appendText("\n" + plate + " - license plate number must be exactly 6 characters.");
            case "Not Valid Vehicle Type Error" -> Controller.getInstance().outputArea.appendText("\n" + plate + " - last character is not a valid vehicle type.");
            case "First 5 Numbers Error" -> Controller.getInstance().outputArea.appendText("\n" + plate + " - first 5 characters must be numbers.");
        }
    }

    @FXML
    public static void printLoadVehicleMessage(String messageType, String error, int numLoaded) {
        switch (messageType) {
            case "Unknown Vehicle Type" -> Controller.getInstance().outputArea.appendText("\nUnknown vehicle type: " + error);
            case "Vehicles Loaded Message" -> Controller.getInstance().outputArea.appendText("\n" + numLoaded + " vehicles loaded.");
            case "Text file not found" -> Controller.getInstance().outputArea.appendText("\nText file not found: " + error);
        }
    }

    @FXML
    public static void printValidCancelBookingMessage(Date begin, Date end, String plate) {
        String validCancelBookingMessage = plate + ":" + begin + " ~ " + end + " has been canceled.";
        Controller.getInstance().outputArea.appendText("\n" + validCancelBookingMessage);
    }

    @FXML
    public static void printInvalidCancelBookingMessage(Date begin, Date end, String plate) {
        String invalidCancelBookingMessage = plate + ":" +begin + " ~ " + end + " - cannot find the booking.";
        Controller.getInstance().outputArea.appendText("\n" + invalidCancelBookingMessage);
    }

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

    @FXML
    public static void printTripsMessage(String messageType, Trip[] trips, Integer i, String minNode) {
        switch (messageType) {
            case "Empty List" -> Controller.getInstance().outputArea.appendText("\nThere is no completed trips.");
            case "Start List" -> Controller.getInstance().outputArea.appendText("\n*List of completed trips ordered by license plate and ending date.");
            case "End List" -> Controller.getInstance().outputArea.appendText("\n*end of util.\n");
            case "Trip Details" -> Controller.getInstance().outputArea.appendText("\n" + minNode);
        }
    }

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

    @FXML
    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) return string;
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    // Booking & Reservation Management Tab
    @FXML TextField beginDateField;
    @FXML TextField endDateField;
    @FXML TextField employeeField;
    @FXML TextField reservationPlateField;
    @FXML ComboBox<String> dropoffCampusComboBox;

    @FXML
    private void bookVehicle() {
        try {
            String beginDate = beginDateField.getText().trim();
            String endDate = endDateField.getText().trim();
            String employee = employeeField.getText().trim();
            String plate = reservationPlateField.getText().trim();
            String dropoffCampus = dropoffCampusComboBox.getValue();

            if (beginDate.isEmpty() || endDate.isEmpty() || employee.isEmpty() || plate.isEmpty() || dropoffCampus == null) {
                outputArea.appendText("\nPlease fill out all information.");
                return;
            }

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
            } else {
                return;
            }
        } catch (Exception e) {
            outputArea.appendText("\n Error booking vehicle: " + e.getMessage());
        }
    }
}
