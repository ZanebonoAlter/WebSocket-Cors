package com.zanebono.chart;

import com.zanebono.chart.WebSocket.WebSocketChat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ServletComponentScan
public class ChartApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ChartApplication.class, args);
        WebSocketChat.setApplicationContext(applicationContext);
    }
}
