package org.easystogu.test;

import javax.ejb.Stateless;
import javax.jws.WebService;

@Stateless
@WebService
public class HelloImpl {
    public String sayHello() {
        return "Welcome to Jboss webservice.";
    }
}
