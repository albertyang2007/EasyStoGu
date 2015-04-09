package org.easystogu.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeEndPoint {
    @RequestMapping(value = "/")
    @ResponseBody()
    public String index() {
        return "Hello EasyStoGu";
    }
}
