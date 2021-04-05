package com.fto.monitor_tool.ping;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PingApplication {

	public static void main(String[] args) {
		Application.launch(PingUiApplication.class, args);
	}

}
