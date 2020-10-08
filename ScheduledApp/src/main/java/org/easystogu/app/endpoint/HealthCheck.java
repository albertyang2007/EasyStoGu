package org.easystogu.app.endpoint;

import org.easystogu.scheduler.DailyScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class HealthCheck {
	@Autowired
	DailyScheduler DailyScheduler;
	@GetMapping("/health")
	public Mono<String> mainPage() {
		return Mono.just("The SchedulerApp is OK.");
	}
}
