package com.vc.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VacationPlannerUserService {
	@RequestMapping("/getuser")
    public String index() {
        return "Welcome to vacation planner!";
    }
}
