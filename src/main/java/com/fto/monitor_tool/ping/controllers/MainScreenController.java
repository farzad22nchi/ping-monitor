package com.fto.monitor_tool.ping.controllers;

import com.fto.monitor_tool.ping.model.Ping;
import com.fto.monitor_tool.ping.service.FileManagerService;
import com.fto.monitor_tool.ping.service.WebPingService;
import com.fto.monitor_tool.ping.util.ControllerUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;


@Controller
public class MainScreenController extends ControllerUtil {
    private static final Logger log = Logger.getLogger(MainScreenController.class.getName());
    private static final String TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

    @FXML
    public TextField duration;
    @FXML
    public TextField interval;
    @FXML
    public TextField webAddress;
    @FXML
    public Button StartStopBtn;
    @FXML
    public TextField avgTimeFrom;
    @FXML
    public TextField avgTimeTo;
    @FXML
    public TextField avgTime;
    @FXML
    public TextField savePath;
    @FXML
    public RadioButton jsonRB;
    @FXML
    public RadioButton csvRB;
    @FXML
    public RadioButton xmlRB;

    ToggleGroup toggleGroup;

    @Autowired
    WebPingService service;
    @Autowired
    FileManagerService fileService;

    @FXML
    public void initialize() {
        toggleGroup = new ToggleGroup();
        jsonRB.setToggleGroup(toggleGroup);
        csvRB.setToggleGroup(toggleGroup);
        xmlRB.setToggleGroup(toggleGroup);
        String begin = "01.01.2000 12:00:00";
        avgTimeFrom.setText(getDateTime(begin)
                .format(dateTimeFormatter).toString());
        avgTimeTo.setText(LocalDateTime
                .now()
                .format(dateTimeFormatter).toString());
    }

    public void startButton(MouseEvent mouseEvent) {
        if(StartStopBtn.getText().equals(ButtonStatus.STOP.label)) {
            service.stopPing();
            StartStopBtn.setText(ButtonStatus.START.label);
            return;
        }
        runPing();
        StartStopBtn.setText(ButtonStatus.STOP.label);
    }
    public void avgButton(MouseEvent mouseEvent){
        LocalDateTime timeFrom = getDateTime(avgTimeFrom.getText());
        LocalDateTime timeTo = getDateTime(avgTimeTo.getText());
        Long averageTime = service.getAverageTime(timeFrom, timeTo);
        avgTime.setText(Long.toString(averageTime));
    }

    public void saveButton(MouseEvent mouseEvent) {
        LocalDateTime timeFrom = getDateTime(avgTimeFrom.getText());
        LocalDateTime timeTo = getDateTime(avgTimeTo.getText());
        final List<Ping> pingList = service.getPings(timeFrom, timeTo);
        saveFile(pingList);
    }

    private void saveFile(List<Ping> pingList) {
        if(jsonRB.isSelected()){
            fileService.saveAsJson(pingList, savePath.getText().trim());
        }
        if(csvRB.isSelected()){
        fileService.saveAsCsv(pingList, savePath.getText().trim());
        }
        if(xmlRB.isSelected()){
            fileService.saveAsXML(pingList, savePath.getText().trim());
        }
    }

    private void runPing() {
        String address = getAddress(webAddress.getText());
        Integer tDuration = getTimeDuration(duration.getText());
        Integer tInterval = getTimeInterval(interval.getText());
        service.startPing(address, tDuration, tInterval);
    }

    private enum ButtonStatus {
        START("Start"),
        STOP("Stop");
        public final String label;
        private ButtonStatus(String label) {
            this.label = label;
        }
    }


}
