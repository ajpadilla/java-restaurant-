package com.restaurant.gateway.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/management")
public class ManagementController {

    @GetMapping
    //@PreAuthorize("hasAuthority('manager:read')")
    public String get() {
        return "GET: management controller";
    }

    @PostMapping
    public String post() {
        return "POST: management controller";
    }

    @PutMapping
    public String put() {
        return "PUT: management controller";
    }

    @DeleteMapping
    public String delete() {
        return "DELETE: management controller";
    }

}
