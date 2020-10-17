package org.easystogu.app.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class HealthCheck {
	private static Logger logger = LoggerFactory.getLogger(HealthCheck.class);
	private static WebClient webClient = WebClient.create("http://localhost:8080");
	
	@GetMapping("/health")
	public Mono<String> mainPage() {
		try {
			logger.info("The SchedulerApp test start");
			webClient.get()
			        .uri("/test", 3L)
			        .accept(MediaType.TEXT_HTML)
			        .exchange()
			        .flatMap( rtn -> {
			          logger.info("status code is :{}", rtn.statusCode());
			          return rtn.bodyToMono(String.class);
			        }).subscribe( v -> logger.info("response is: {}", v));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Mono.just("The SchedulerApp is OK.");
	}
	
	@GetMapping("/test")
	public Mono<String> testPage() {
		logger.info("testPage");
		return Mono.just("returing testPage");
	}
}
