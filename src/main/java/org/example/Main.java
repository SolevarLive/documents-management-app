package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.repository.InMemoryDocumentRepository;
import org.example.service.DocumentServiceImpl;
import org.example.ui.controllers.MainController;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        var service = new DocumentServiceImpl(new InMemoryDocumentRepository());

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);


        MainController controller = fxmlLoader.getController();
        controller.setService(service);

        stage.setTitle("Управление документами");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
