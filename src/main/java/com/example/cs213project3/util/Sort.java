package com.example.cs213project3.util;

import com.example.cs213project3.Controller;
import com.example.cs213project3.rental.*;
import com.example.cs213project3.vehicle.Vehicle;

/**
 * The Sort util class sorts lists based on different keys.
 * It sorts the Fleet, Reservation, and TripList as well as the Cost Report for the Trips
 * @author Lana Huang, Sharon Chen
 */
public class Sort {
    private Sort() {}

    /**
     * PC Command: Prints the cost report, ordered by department, including the charge and surcharge for each trip, and the department total for all charges.
     */
    public static void printCost(){
        if (Controller.tripList.getLast() == null) {
            Controller.printCostMessage("Empty List", null, null, null, null);
            return;
        }

        Trip[] trips = getAllTrips();
        orderTripsByDept(trips, trips.length);

        Controller.printCostMessage("Start List", null, null, null, null);
        String currentDept = "";
        double deptTotal = 0.00;

        for (int i = 0; i < trips.length; i++) {
            String tripDept = trips[i].getBooking().getEmployee().getDepartment().toString();
            if (!tripDept.equals(currentDept)) {
                if (!currentDept.isEmpty()){
                    Controller.printCostMessage("Department Total", null, null, deptTotal, null);
                    deptTotal = 0.00;
                }
                currentDept = tripDept;
                Controller.printCostMessage("Department Details", null, null, null, currentDept);
            }

            if (trips[i].hasSurcharge()){
                double tripTotal = trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed()) + trips[i].getBooking().getVehicle().surcharge(trips[i].mileageUsed(), trips[i].hasSurcharge());
                deptTotal += tripTotal;
                Controller.printCostMessage("Surcharged Trip Details", trips, i,null, null);
                Controller.printCostMessage("Surcharged Cost Details", trips, i, tripTotal, null);
            }
            else {
                deptTotal += trips[i].getBooking().getVehicle().charge(trips[i].mileageUsed());
                Controller.printCostMessage("Not Surcharged Trip Details", trips, i, null, null);
                Controller.printCostMessage("Not Surcharged Cost Details", trips, i, null, null);
            }
        }
        Controller.printCostMessage("Department Total", null, null, deptTotal, null);
        Controller.printCostMessage("End List", null, null, null, null);
    }

    /**
     * Print all completed trips in the circular linked list, tripList, ordered by license plate and end date.
     * This method creates a visited boolean array that keeps track of Nodes already visited.
     * It then repeatedly finds the unvisited node with the earliest date, prints the trip information.
     * Then it marks the node as visited and continues this process until all are printed.
     */
    public static void printCompletedTrips() {
        if (Controller.tripList.getLast() == null) {
            Controller.printTripsMessage("Empty List", null, null, null);
            return;
        }
        Controller.printTripsMessage("Start List", null, null, null);

        int length = TripList.getLength();
        Node ptr;

        boolean[] visited = new boolean[length];

        for (int i = 0; i < length; i++) {
            Node minNode = null;
            int minIndex = -1;

            ptr = Controller.tripList.getLast().getNext();
            for (int j = 0; j < length; j++) {
                if (!visited[j]) {
                    if (minNode == null || (ptr.getTrip().getBooking().getVehicle().getPlate().compareTo(minNode.getTrip().getBooking().getVehicle().getPlate()) < 0)) {
                        minNode = ptr;
                        minIndex = j;
                    } else if (ptr.getTrip().getBooking().getVehicle().getPlate().compareTo(minNode.getTrip().getBooking().getVehicle().getPlate()) < 0) {
                        if (ptr.getTrip().getBooking().getEnd().compareTo(minNode.getTrip().getBooking().getEnd()) <= 0) {
                            minNode = ptr;
                            minIndex = j;
                        }
                    }
                }
                ptr = ptr.getNext();
            }
            Controller.printTripsMessage("Trip Details", null, null, Sort.tripString(minNode));
            visited[minIndex] = true;
        }
        Controller.printTripsMessage("End List", null, null, null);
    }

    /**
     * Takes the Node and returns a String in the correct format for printing TripList.
     * @param minNode the Node to get the correct information for printing.
     * @return the String in the correct format.
     */
    private static String tripString(Node minNode) {
        if (minNode.getTrip().hasSurcharge()){
            return (minNode.getTrip().getBooking().getVehicle().getPlate() + " " + minNode.getTrip().getBooking().getBegin() + " ~ " + minNode.getTrip().getBooking().getEnd() + " mileage(old): " + minNode.getTrip().getBeginMileage() + " mileage(new): " + minNode.getTrip().getEndMileage() + " mileage(used): " + minNode.getTrip().mileageUsed() + " [dropped off: " + minNode.getTrip().getBooking().getCampusDropoff().name() + "**]");
        }
        else {
            return (minNode.getTrip().getBooking().getVehicle().getPlate() + " " + minNode.getTrip().getBooking().getBegin() + " ~ " + minNode.getTrip().getBooking().getEnd() + " mileage(old): " + minNode.getTrip().getBeginMileage() + " mileage(new): " + minNode.getTrip().getEndMileage() + " mileage(used): " + minNode.getTrip().mileageUsed() + " [dropped off: " + minNode.getTrip().getBooking().getCampusDropoff().name() + "]");
        }
    }

    /**
     * Puts the list of trips in order by department
     * @param trips the list of trips that will be organized
     * @param length the length of the list of trips
     */
    private static void orderTripsByDept(Trip[] trips, int length) {
        for (int i = 0; i < (length - 1); i++) {
            for (int j = 0; j < (length - i - 1); j++) {
                String dept1 = trips[j].getBooking().getEmployee().getDepartment().toString();
                String dept2 = trips[j + 1].getBooking().getEmployee().getDepartment().toString();
                //sort departments
                if (dept1.compareTo(dept2) > 0) {
                    Trip temp = trips[j];
                    trips[j] = trips[j+1];
                    trips[j+1] = temp;
                }
            }
        }
    }

    /**
     * Puts all the trips from the linked list into an array
     * @return array of trips
     */
    private static Trip[] getAllTrips() {
        int length = 1;
        Node ptr = Controller.tripList.getLast().getNext();
        while (ptr != Controller.tripList.getLast()) {
            length++;
            ptr = ptr.getNext();
        }

        Trip[] trips = new Trip[length];
        ptr = Controller.tripList.getLast().getNext();
        for (int i = 0; i < length; i++){
            trips[i] = ptr.getTrip();
            ptr = ptr.getNext();
        }

        return trips;
    }

    /**
     * PR Command: Prints all reservations ordered by campus city location, then license plate number, and then by beginning date.
     */
    public static void printBookingsByCity() {
        if (Controller.bookings.isEmpty()) {
            Controller.printBookingsMessage("Empty List", null, null);
            return;
        }

        for (int i = 0; i < (Controller.bookings.size() - 1); i++) {
            for (int j = 0; j < (Controller.bookings.size() - i - 1); j++) {
                String city1 = Controller.bookings.get(j).getVehicle().getCampus().getCity();
                String city2 = Controller.bookings.get(j + 1).getVehicle().getCampus().getCity();

                int compareCampus = city1.compareTo(city2); //Compare by campus first
                if (compareCampus > 0) {
                    swapBookings(j, j + 1);
                }

                else if (city1.compareTo(city2) == 0) {
                    String plate1 = Controller.bookings.get(j).getVehicle().getPlate();
                    String plate2 = Controller.bookings.get(j + 1).getVehicle().getPlate();

                    if (plate1.compareTo(plate2) > 0) {
                        swapBookings(j, (j + 1));
                    }
                    else if (plate1.compareTo(plate2) == 0) {
                        Date begin1 = Controller.bookings.get(j).getBegin();
                        Date begin2 = Controller.bookings.get(j + 1).getBegin();

                        if (begin1.compareTo(begin2) > 0) {
                            swapBookings(j, (j + 1));
                        }
                    }
                }
            }
        }
        Controller.printBookingsMessage("Start City List", null, null);
        for (int i = 0; i < Controller.bookings.size(); i++) {
            Controller.printBookingsMessage("City Booking Details", Controller.bookings.get(i).toString(), null);
        }
        Controller.printBookingsMessage("End List", null, null);
    } //ordered by city, then plate, and then beginning date

    /**
     * PD Command: Prints all reservations ordered by department and then by employee.
     */
    public static void printBookingsByDept() {
        if (Controller.bookings.isEmpty()) {
            Controller.printBookingsMessage("Empty List", null, null);
            return;
        }

        for (int i = 0; i < (Controller.bookings.size() - 1); i++) {
            for (int j = 0; j < (Controller.bookings.size() - i - 1); j++) {
                String dept1 = Controller.bookings.get(j).getEmployee().getDepartment().toString();
                String dept2 = Controller.bookings.get(j + 1).getEmployee().getDepartment().toString();
                //sort departments
                if (dept1.compareTo(dept2) > 0) {
                    Booking temp = Controller.bookings.get(j);
                    Controller.bookings.set(j, Controller.bookings.get(j + 1));
                    Controller.bookings.set(j + 1, temp);
                } else if (dept1.compareTo(dept2) == 0) {
                    String emp1 = Controller.bookings.get(j).getEmployee().name();
                    String emp2 = Controller.bookings.get(j + 1).getEmployee().name();
                    //sort employees in department
                    if (emp1.compareTo(emp2) > 0) {
                        Booking temp = Controller.bookings.get(j);
                        Controller.bookings.set(j, Controller.bookings.get(j + 1));
                        Controller.bookings.set(j + 1, temp);
                    }
                }
            }
        }
        Controller.printBookingsMessage("Start Dept List", null, null);
        String currentDept = "";
        for (int i = 0; i < Controller.bookings.size(); i++) {
            String bookingDept = Controller.bookings.get(i).getEmployee().getDepartment().toString();
            if (!bookingDept.equals(currentDept)) {
                currentDept = bookingDept;
                Controller.printBookingsMessage("Department Details", null, currentDept);
            }
            Controller.printBookingsMessage("Dept Booking Details", Controller.bookings.get(i).toString(), null);
        }
        Controller.printBookingsMessage("End List", null, null);
    } //ordered by department then by employee

    /**
     * Swap 2 bookings in the bookings list given the index of booking 1 and booking 2.
     * @param i The index of the first booking to be swapped.
     * @param j The index of the second booking to be swapped.
     */
    private static void swapBookings(int i, int j) {
        Booking temp = Controller.bookings.get(i);
        Controller.bookings.set(i, Controller.bookings.get(j));
        Controller.bookings.set(j, temp);
    }

    /**
     * Prints the fleet by the make then by the date obtained
     * Uses selection sort methods to loop through the fleet to find the minimum index of the minimum element.
     */
    public static void printSortedFleet() {
        if (Controller.fleet.isEmpty()) {
            Controller.printNoVehicleInFleet();

        } else {
            Controller.printFleetMessages("Start List", null);
            for (int i = 0; i < Controller.fleet.size() - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < Controller.fleet.size(); j++) {
                    int compareCampus = Controller.fleet.get(j).getCampus().getCity().compareTo(Controller.fleet.get(minIndex).getCampus().getCity()); //Compare by campus first
                    if (compareCampus < 0) {
                        minIndex = j;
                    } else if (compareCampus == 0) {
                        int compareMake = Controller.fleet.get(j).getMake().compareTo(Controller.fleet.get(minIndex).getMake()); //If campus is equal then compare by car make
                        if (compareMake < 0) {
                            minIndex = j;
                        } else if (compareMake == 0) {
                            int compareDate = Controller.fleet.get(j).getDate().compareTo(Controller.fleet.get(minIndex).getDate()); //If car make is equal then compare by date
                            if (compareDate < 0) {
                                minIndex = j;
                            }
                        }
                    }
                }
                if (minIndex != i) {
                    Vehicle temp = Controller.fleet.get(i);
                    Controller.fleet.set(i, Controller.fleet.get(minIndex));
                    Controller.fleet.set(minIndex, temp);
                }
            }
            for (int i = 0; i < Controller.fleet.size(); i++) {
                Controller.printFleetMessages("Fleet Details", i);
            }
            Controller.printFleetMessages("End List", null);
        }
    }
}
