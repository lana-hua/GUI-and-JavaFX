package com.example.cs213project3.rental;

/**
 * Enum of the Campus that the Vehicles go to.
 * Each campus has the name and the String format for the city.
 * @author Sharon Chen
 */
public enum Campus {
    Busch ("New Brunswick"),
    Livingston ("New Brunswick"),
    Cook ("New Brunswick"),
    Newark ("Newark"),
    Camden ("Camden");

    private final String city;

    /**
     * Returns the city that the campus is a part of.
     * @return the city string
     */
    public String getCity(){
        return city;
    }

    /**
     * Gives the city that the campus is a part of.
     * @param city The city string that the campus is a part of.
     */
    Campus(String city) {this.city = city;}

    /**
     * Checks if the value of a string is a valid Employee
     * The method provides capitalization to check and
     * @param campus The string campus that the method is checking
     * @return true if the string is a valid employee enum; return false otherwise.
     */
    public static boolean isValidCampus(String campus) {
        try {
            String capitalizedName = campus.substring(0, 1).toUpperCase() + campus.toLowerCase().substring(1);
            switch (capitalizedName) {
                case "Busch", "Livingston", "Cook", "Newark", "Camden" -> {
                    return true;
                }
                default -> {
                    return false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return a string representation of Campus class with the city and the campus
     * @return the formatted String with the city and campus name
     */
    @Override
    public String toString() {
        return name() +  ":" + city;
    }
}
