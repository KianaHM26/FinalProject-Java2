module com.champsoft.final_project_kianaandarushi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.champsoft.final_project_kianaandarushi to javafx.fxml;
    exports com.champsoft.final_project_kianaandarushi;
}