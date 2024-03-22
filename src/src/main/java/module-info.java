module drum.src {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens drum.src to javafx.fxml;
    exports drum.src;
}