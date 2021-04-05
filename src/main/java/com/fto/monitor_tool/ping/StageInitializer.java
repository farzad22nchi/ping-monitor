package com.fto.monitor_tool.ping;

import com.fto.monitor_tool.ping.PingUiApplication.StageReadyEvent;
import com.fto.monitor_tool.ping.controllers.MainScreenController;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {
    private static final Logger logger = Logger.getLogger(StageInitializer.class.getName());
    @Autowired
    MainScreenController controller;
    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        stage.setTitle("Monitor-Tool");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_stage.fxml"));
        fxmlLoader.setController(controller);
        Region root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            logger.log(Level.WARNING, "fxml file did not find");
        }
        stage.setScene(new Scene( root,600,400));
        stage.show();
    }
}
