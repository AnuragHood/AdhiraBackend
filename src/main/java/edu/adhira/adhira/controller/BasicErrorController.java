package edu.adhira.adhira.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class BasicErrorController implements ErrorController {

	private final static String PATH = "/error";

	@RequestMapping("/error")
	@ResponseBody
	public String handleError(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		
		return 
				"<html><body style =background-color:#9cafec><div style ='text-align:center; top:40%; position: relative'><p><b style =color:red>Something went wrong...may be you can try with below options?</b></p><br /><a href =http://localhost:4200><b>Login</b><br /></a><a href =http://localhost:4200><b>Register</b></a><h2 style =color:red>Error Occured</h2><div>Status code:"+statusCode+" <b></b></div>"
						+ "<body></html>";
				
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
