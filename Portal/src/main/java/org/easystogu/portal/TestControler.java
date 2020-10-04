package org.easystogu.portal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "/portal/")
public class TestControler {
	@PostMapping("/holle")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response holle(final String body, @Context HttpServletRequest request) {
		System.out.println(request.getHeader("Content-Type"));
		JSONObject json = JSONObject.fromObject(body);
		System.out.println(json.get("subscriberId"));
		return Response.ok().build();
	}
}
