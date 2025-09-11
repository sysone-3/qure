package app.snapshot.qure.facilities.controller;

import app.snapshot.qure.facilities.dto.FacilitySummaryDto;
import app.snapshot.qure.facilities.service.FacilitySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/facilities")
public class FacilitySummaryApiController {
    @Autowired
    private FacilitySummaryService facilitySummaryService;

    @GetMapping("/{id}/summary")
    public FacilitySummaryDto getSummary(@PathVariable("id") int id) {
        return facilitySummaryService.getSummary(id);
    }
}
