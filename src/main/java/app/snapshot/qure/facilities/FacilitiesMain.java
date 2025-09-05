package app.snapshot.qure.facilities;

import java.util.List;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;

import app.snapshot.qure.facilities.dto.FacilitiesDto;
import app.snapshot.qure.facilities.service.IFacilitiesService;

public class FacilitiesMain {

    public static void main(String[] args) {
        AbstractApplicationContext context =
                new GenericXmlApplicationContext("classpath:config/jdbc/application-config.xml");

        try {
            IFacilitiesService facilitiesService = context.getBean("facilitiesService", IFacilitiesService.class);

            // 1) 신규 시설 등록
            System.out.println("=== 신규 시설 등록 ===");
            FacilitiesDto f = new FacilitiesDto();
            f.setFacilityId(500);                 // PK (중복되지 않게!)
            f.setName("본관 1층 로비");
            f.setDomain("청결");                  // 예: 청결 / 순찰 / 소방 ...
            f.setFloor("1F");
            f.setZone("Lobby-A");
            f.setAddress("서울시 어딘가 123");
            f.setGpsLat(37.5665);
            f.setGpsLng(126.9780);
            f.setStatus("정상");                  // 예: 정상/주의/긴급/점검필요
            f.setMemo("입구 매트 교체 예정");
            f.setManagersId(101);                 // 관리자 ID 없으면 0 또는 null로
            f.setInspectorId(0);                  // 점검자 미할당이면 0

            try {
                int inserted = facilitiesService.insertFacilities(f);
                System.out.println("insert 결과: " + inserted);
            } catch (DataAccessException ex) {
                System.out.println("INSERT 실패: " + ex.getMessage());
            }

            // 2) 목록 조회 (요구 컬럼만 출력)
            System.out.println("\n=== 시설 목록 ===");
            List<FacilitiesDto> list = facilitiesService.getFacilitiesList();
            for (FacilitiesDto it : list) {
                String updated = (it.getUpdatedAt() == null) ? "—" : it.getUpdatedAt().toString();
                System.out.printf("ID=%d, NAME=%s, DOMAIN=%s, UPDATED_AT=%s, STATUS=%s%n",
                        it.getFacilityId(),
                        it.getName(),
                        it.getDomain(),
                        updated,
                        it.getStatus());
            }

            // 3) 삭제 데모
            System.out.println("\n=== 500 시설 삭제 ===");
            int cnt = facilitiesService.deleteFacilities(500, null);
            System.out.println(cnt + "개의 데이터를 삭제");

        } finally {
            context.close();
        }
    }
}
