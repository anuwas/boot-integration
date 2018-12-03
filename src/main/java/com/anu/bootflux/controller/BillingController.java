package com.anu.bootflux.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/billing/api/v1")
public interface BillingController {
	
	@RequestMapping(value = "/testapi", produces = {MediaType.TEXT_HTML_VALUE}, method = RequestMethod.GET)
	public String testApi();

}
