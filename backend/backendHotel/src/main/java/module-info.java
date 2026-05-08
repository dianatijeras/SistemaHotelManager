module co.edu.uniquindio.sistemahotelmanager.backendhotel {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.sistemahotelmanager.backendhotel to javafx.fxml;
    exports co.edu.uniquindio.sistemahotelmanager.backendhotel;
    exports co.edu.uniquindio;
    opens co.edu.uniquindio to javafx.fxml;
}