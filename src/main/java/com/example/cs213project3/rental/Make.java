package com.example.cs213project3.rental;

import com.example.cs213project3.Controller;

/**
 * Enum make that contains all the different makes of the vehicles
 * @author Lana Huang
 */
public enum Make {
    CHEVY ("CHEVY"),
    FORD ("FORD"),
    HONDA ("HONDA"),
    TOYOTA ("TOYOTA");

    private String make;

    /**
     * Gives the string make.
     * @param make The make string.
     */
    Make(String make) {
        this.make = make;
    }

    /**
     * Checks if the given string is a valid make.
     * Accounts for various different cases.
     * @param make The string make that needs to be validated.
     * @return true if the string is a valid make; false otherwise.
     */
    public static boolean isValidMake(String make) {
        String upperMake = make.toUpperCase();
        switch (upperMake) {
            case "HONDA", "CHEVY", "TOYOTA", "FORD" -> {
                return true;
            }
            default -> {
                Controller.printInvalidMakeMessage(make);
                return false;
            }
        }
    }
}


