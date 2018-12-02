package com.anu.billing.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingControllerImpl implements BillingController{

	@Override
	public String testApi() {
		return "successfully running";
	}

}
