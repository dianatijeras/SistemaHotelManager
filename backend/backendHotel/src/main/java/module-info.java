module co.edu.uniquindio.sistemahotelmanager.backendhotel {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.sistemahotelmanager.sistemahotelmanager.backendhotel to javafx.fxml;
    exports co.edu.uniquindio.sistemahotelmanager.sistemahotelmanager.backendhotel;
    exports co.edu.uniquindio.sistemahotelmanager.sistemahotelmanager;
    opens co.edu.uniquindio.sistemahotelmanager.sistemahotelmanager to javafx.fxml;
}