package org.easystogu.kafuka.app.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthCheck {
	@GetMapping("/health")
	public Mono<String> mainPage() {
		return Mono.just("The KafukaBroker is OK.");
	}
}
