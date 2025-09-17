package app.snapshot.qure.facilities.service;

import app.snapshot.qure.facilities.dto.FacilitySummaryDto;
import app.snapshot.qure.facilities.repository.IFacilitySummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 작성자: 김민서
@Service
public class FacilitySummaryService {
    @Autowired
    private IFacilitySummaryRepository repo;

    public FacilitySummaryDto getSummary(Long facilityId) {
        return repo.findSummaryByFacilityId(facilityId);
    }
}
