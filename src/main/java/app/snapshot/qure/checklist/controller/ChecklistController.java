package app.snapshot.qure.checklist.controller;
// 작성자: 최이서

import app.snapshot.qure.checklist.service.IChecklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChecklistController {

    @Autowired
    IChecklistService checklistService;
}
