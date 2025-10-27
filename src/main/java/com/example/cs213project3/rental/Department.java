package com.example.cs213project3.rental;

/**
 * Enum of the Departments that the Employees are from.
 * Each department has the name and the String format for the department.
 * @author Lana Huang
 */
public enum Department {
    COMPUTER_SCIENCE("Computer Science"),
    ELECTRICAL_ENGINEERING("Electrical Engineering"),
    INFORMATION_TECHNOLOGY_AND_INFORMATICS("Information Technology and Informatics"),
    MATHEMATICS("Mathematics"),
    BUSINESS_ANALYTICS_AND_INFORMATION_TECHNOLOGY("Business Analytics and Information Technology");

    private final String fullName;

    /**
     * Gives the string format of the department name.
     * @param fullName The string format of the department name.
     */
    Department(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Override toString that returns the string format of the department name.
     * @return fullName The string format fo the department name.
     */
    @Override
    public String toString() {
        return fullName;
    }
}
