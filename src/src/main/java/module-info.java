module drum.src {
    requires javafx.controls;
    requires javafx.fxml;


    opens drum.src to javafx.fxml;
    exports drum.src;
}