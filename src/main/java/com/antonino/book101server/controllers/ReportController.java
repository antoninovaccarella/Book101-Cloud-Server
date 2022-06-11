package com.antonino.book101server.controllers;

import com.antonino.book101server.dto.ReportItem;
import com.antonino.book101server.models.Product;
import com.antonino.book101server.services.OrderService;
import com.antonino.book101server.services.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ReportItem>> getReport() {
        List<ReportItem> result = reportService.getReport();
        return ResponseEntity.ok(result);
    }
}
