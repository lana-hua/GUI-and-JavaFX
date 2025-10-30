module com.example.cs213project3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    opens com.example.cs213project3.vehicle to javafx.fxml;
    exports com.example.cs213project3.vehicle;
    exports com.example.cs213project3.rental;
    exports com.example.cs213project3.util;
}