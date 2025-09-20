package com.subham.app2;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App11Application {
	
	private static final Logger logger = LoggerFactory.getLogger(App11Application.class);

	public static void main(String[] args) {
		SpringApplication.run(App11Application.class, args);
	}
	
	@GetMapping("/hbt")
    public String healthCheck() {
		logger.info("Health check triggered!!!");
        return "OK";
    }

    @PostMapping("/send")
    public JSONObject sendData(@RequestBody String body) {
        logger.info("Post call triggered!!!!");
        JSONObject jsonObject = new JSONObject(body);
        return jsonObject;
    }

}
