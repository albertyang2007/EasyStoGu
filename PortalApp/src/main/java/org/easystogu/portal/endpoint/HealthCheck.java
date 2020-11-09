package org.easystogu.portal.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

  @GetMapping("/portal/health")
  public String mainPage() {
    return new String("The PortalApp is OK.");
  }
}
