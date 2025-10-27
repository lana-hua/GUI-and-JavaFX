package com.example.cs213project3.rental;

import java.util.Calendar;

/**
 The Date class manages a calendar date object containing day, month, and year.
 It allows the user check the date validity and compare for date.
 @author Sharon Chen
*/
public class Date implements Comparable<Date> {
    private int day;
    private int month;
    private int year;

    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;

    public static final int MIN_DAYS = 1;
    public static final int MAX_DAYS_SHORT = 30;
    public static final int MAX_DAYS_LONG = 31;
    public static final int MIN_MONTH = 1;
    public static final int MAX_MONTH = 12;
    public static final int MIN_YEAR = 0;

    public static final int FEBRUARY = 2;
    public static final int APRIL = 4;
    public static final int JUNE = 6;
    public static final int SEPTEMBER = 9;
    public static final int NOVEMBER = 11;

    public static final int FEBRUARY_NONLEAP_DAYS = 28;
    public static final int FEBRUARY_LEAP_DAYS = 29;

    /**
     * Constructs a Date object with the specified month, day, and year.
     * @param month the month of the date
     * @param day the day of the date
     * @param year the year of the date
     */
    public Date(int month, int day, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Constructs a Date object from a string in "MM/DD/YYYY" format.
     * @param dateInput the date string to parse
     */
    public Date(String dateInput) {
        String[] dateSections = dateInput.split("/");

        if ((dateSections.length == 3)) {
            this.month = Integer.parseInt(dateSections[0]);
            this.day = Integer.parseInt(dateSections[1]);
            this.year = Integer.parseInt(dateSections[2]);

        }
        else {
            Frontend.printInvalidDate(dateInput);
        }
    }

    /**
     * Returns the day of the date.
     * @return the day of the date
     */
    public int getDay(){
        return day;
    }

    /**
     * Returns the month of the date.
     * @return the month of the date
     */
    public int getMonth(){
        return month;
    }

    /**
     * Returns the year of the date.
     * @return the year of the date
     */
    public int getYear(){
        return year;
    }

    /**
     * Determines if the date falls in a leap year.
     * @return true if the year is a leap year; return false otherwise
     */
    public boolean isLeap() {
        if (year % QUADRENNIAL == 0){
            if (year % CENTENNIAL== 0){
                if (year % QUATERCENTENNIAL == 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return true;
            }
        }
        else{
            return false;
        }
    }

    /**
     * Checks if the date is today or in the future.
     * @return true if the date is today or future; return false if in the past
     */
    public boolean isTodayOrFuture(){
        Calendar today = Calendar.getInstance();
        Date todaysDate = new Date((today.get(Calendar.MONTH)+1), (today.get(Calendar.DAY_OF_MONTH)), (today.get(Calendar.YEAR)));

        //compare date w/ today
        if (this.compareTo(todaysDate) >= 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Validates if the date is appropriate for booking purposes.
     * @param type the type of date being validated (begin or end)
     * @param date the date to validate
     * @return true if the date is valid for booking; return false otherwise
     */
    public boolean isBookingDateValid(String type, Date date) {
        if (!date.isValid()) {
            if (type == "begin") {
                Frontend.printBeginDateErrorMessage("Valid Error", date);
                return false;
            } else if (type == "end") {
                Frontend.printEndDateErrorMessage("Valid Error", date, date);
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the date represents a valid calendar date.
     * @return true if the date is valid; return false otherwise
     */
    public boolean isValid() {
        if ((day < MIN_DAYS) || (day > MAX_DAYS_LONG) || (month < MIN_MONTH) || (month > MAX_MONTH) || (year < MIN_YEAR)) {
            return false;
        }

        //setting maxDays in each month
        int maxDays;
        if (month == FEBRUARY) {
            if (isLeap()) {
                maxDays = FEBRUARY_LEAP_DAYS;
            }
            else {
                maxDays = FEBRUARY_NONLEAP_DAYS;
            }
        }
        else if ((month == APRIL) || (month == JUNE) || (month == SEPTEMBER) || (month == NOVEMBER)) {
            maxDays = MAX_DAYS_SHORT;
        }
        else {
            maxDays = MAX_DAYS_LONG;
        }

        return (day <= maxDays);
    }

    /**
     * Checks if the date is within 3 months from today.
     * @return true if within 3 months; return false otherwise
     */
    public boolean isWithin3Months(){
        Calendar today = Calendar.getInstance();
        Date todaysDate = new Date((today.get(Calendar.MONTH)+1), (today.get(Calendar.DAY_OF_MONTH)), (today.get(Calendar.YEAR)));

        Calendar threeMonthsLater = (Calendar) today.clone();
        threeMonthsLater.add(Calendar.MONTH, 3);
        Date threeMonthsLaterDate = new Date((threeMonthsLater.get(Calendar.MONTH)+1), (threeMonthsLater.get(Calendar.DAY_OF_MONTH)), (threeMonthsLater.get(Calendar.YEAR)));

        if ((this.compareTo(todaysDate) >= 0) && (this.compareTo(threeMonthsLaterDate) <= 0)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks if the duration between begin and end dates is within 7 days.
     * @param begin the starting date
     * @param end the ending date
     * @return true if duration is within 7 days; return false otherwise
     */
    public boolean isWithin7Days(Date begin, Date end){
        Calendar bookingStart = Calendar.getInstance();
        bookingStart.set(Calendar.DAY_OF_MONTH, begin.getDay());
        bookingStart.set(Calendar.MONTH, begin.getMonth() - 1);
        bookingStart.set(Calendar.YEAR, begin.getYear());

        Calendar bookingEnd = Calendar.getInstance();
        bookingEnd.set(Calendar.DAY_OF_MONTH, end.getDay());
        bookingEnd.set(Calendar.MONTH, end.getMonth() - 1);
        bookingEnd.set(Calendar.YEAR, end.getYear());

        long durationMsec = bookingEnd.getTimeInMillis() - bookingStart.getTimeInMillis();
        long durationDays = durationMsec / (1000 * 60 * 60 * 24);

        if ((durationDays >= 0) && (durationDays < 7)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Validates if the calendar date string represents a valid date.
     * @param date the date string to validate
     * @return true if valid; return false otherwise
     */
    public boolean isCalendarDateValid(String date) {
        if (!isValid()){
            Frontend.printInvalidDate(date);
            return false;
        } else if ((isTodayOrFuture())) {
            Frontend.printTodayOrFuture(date);
            return false;
        }
        return true;
    }

    /**
     * Compares this Date object with another object for equality.
     * @param comparison the object to compare with this date
     * @return true if the dates are equal; return false otherwise
     */
    @Override
    public boolean equals(Object comparison) {
        if (this == comparison) {
            return true;
        }
        if ((comparison == null) || (this.getClass() != comparison.getClass())) {
            return false;
        }

        Date compareDate = (Date) comparison;
        return ((this.day == compareDate.day) && (this.month == compareDate.month) && (this.year == compareDate.year));
    }

    /**
     * Returns a string representation of the date in "MM/DD/YYYY" format.
     * @return the formatted date string
     */
    @Override
    public String toString(){
        return (month + "/" + day + "/" + year);
    }

    /**
     * Compares this date with another date.
     * @param comparison the date to compare with
     * @return positive if later, zero if equal, negative if this date is earlier
     */
    @Override
    public int compareTo(Date comparison){
        if (this.year != comparison.year){
            return Integer.compare(this.year, comparison.year);
        }
        if (this.month != comparison.month){
            return Integer.compare(this.month, comparison.month);
        }
        return Integer.compare(this.day, comparison.day);
    }

    /**
     Testing 6 cases in testbed main for isValid() method.
     */
    public static void main(String[] args) {
        Date test1 = new Date("11/34/2025");
        System.out.println(test1.isValid());

        Date test2 = new Date("19/16/2025");
        System.out.println(test2.isValid());

        Date test3 = new Date("06/07/-1");
        System.out.println(test3.isValid());

        Date test4 = new Date("02/29/2028");
        System.out.println(test4.isValid());

        Date test5 = new Date("02/29/2026");
        System.out.println(test5.isValid());

        Date test6 = new Date("10/30/2025");
        System.out.println(test6.isValid());
    }
}
