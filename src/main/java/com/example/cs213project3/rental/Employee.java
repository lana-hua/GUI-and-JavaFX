package com.example.cs213project3.rental;

/**
 * Enum of each Employee using their last name, each Employee has their department attached
 * @author Lana Huang
 */
public enum Employee {
    Patel (Department.COMPUTER_SCIENCE),
    Lim (Department.ELECTRICAL_ENGINEERING),
    Zimnes (Department.COMPUTER_SCIENCE),
    Harper (Department.ELECTRICAL_ENGINEERING),
    Kaur (Department.INFORMATION_TECHNOLOGY_AND_INFORMATICS),
    Taylor (Department.MATHEMATICS),
    Ramesh (Department.MATHEMATICS),
    Ceravolo (Department.BUSINESS_ANALYTICS_AND_INFORMATION_TECHNOLOGY);


    private Department dept;

    /**
     * Returns the department that the employee is a part of.
     * @return the department enum
     */
    public Department getDepartment(){
        return dept;
    }

    /**
     * Gives the department that the employee is a part of.
     * @param department The department enum that the employee is a part of.
     */
    Employee(Department department) {
        this.dept = department;
    }

    /**
     * Checks if the value of a string is a valid Employee
     * The method provides capitalization to check and
     * @param employee The string employee that the method is checking
     * @return true if the string is a valid employee enum; return false otherwise.
     */
    public static boolean isValidEmployee(String employee) {
        String capitalizedName = employee.substring(0, 1).toUpperCase() + employee.toLowerCase().substring(1);
        switch (capitalizedName) {
            case "Patel", "Lim", "Zimnes", "Harper", "Kaur", "Taylor", "Ramesh", "Ceravolo" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    /**
     * Overrides the toString method to return both the name and the department
     * @return Returns the name plus the department
     */
    @Override
    public String toString() {
        return name() + ": " + dept.toString();
    }

}
