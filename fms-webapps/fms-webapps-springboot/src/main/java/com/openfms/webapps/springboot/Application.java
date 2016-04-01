package com.openfms.webapps.springboot;

import java.awt.Desktop;
import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan(basePackages = "com")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		try {
			System.setProperty("java.awt.headless","true");
			Desktop.getDesktop().browse(new URI("http://127.0.0.1:8080"));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ProcessBuilder pb = new ProcessBuilder("open","http://127.0.0.1:8080");
			pb.start();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
