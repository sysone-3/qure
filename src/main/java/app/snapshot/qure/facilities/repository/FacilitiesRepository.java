package app.snapshot.qure.facilities.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import app.snapshot.qure.facilities.dto.FacilitiesDto;

@Repository
public class FacilitiesRepository implements IFacilitiesRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final RowMapper<FacilitiesDto> MAPPER = (rs, rowNum) -> {
        FacilitiesDto f = new FacilitiesDto();
        f.setFacilityId(rs.getInt("FACILITY_ID"));
        f.setDomain(rs.getString("DOMAIN"));
        f.setName(rs.getString("NAME"));
        f.setFloor(rs.getString("FLOOR"));
        f.setZone(rs.getString("ZONE"));
        f.setAddress(rs.getString("ADDRESS"));
        f.setGpsLat(rs.getObject("GPS_LAT") == null ? 0.0 : rs.getDouble("GPS_LAT"));
        f.setGpsLng(rs.getObject("GPS_LNG") == null ? 0.0 : rs.getDouble("GPS_LNG"));
        f.setStatus(rs.getString("STATUS"));
        f.setMemo(rs.getString("MEMO"));
        f.setCreatedAt(rs.getTimestamp("CREATED_AT"));
        f.setUpdatedAt(rs.getTimestamp("UPDATED_AT"));
        f.setManagersId(rs.getObject("MANAGERS_ID") == null ? 0 : rs.getInt("MANAGERS_ID"));
        f.setInspectorId(rs.getObject("INSPECTOR_ID") == null ? 0 : rs.getInt("INSPECTOR_ID"));
        return f;
    };

    @Override
    public List<FacilitiesDto> getFacilitiesList() {
        String sql = """
            SELECT *
            FROM FACILITIES
            ORDER BY FACILITY_ID DESC
            """;
        return jdbcTemplate.query(sql, MAPPER);
    }

    @Override
    public List<FacilitiesDto> searchFacilities(String q) {
        String like = "%" + q.toLowerCase() + "%";
        String sql = """
            SELECT *
              FROM FACILITIES
             WHERE LOWER(NAME) LIKE ?
                OR LOWER(DOMAIN) LIKE ?
             ORDER BY FACILITY_ID DESC
            """;
        return jdbcTemplate.query(sql, MAPPER, like, like);
    }

    @Override
    public FacilitiesDto getFacilitiesInfo(int facilityId) {
        String sql = "SELECT * FROM FACILITIES WHERE FACILITY_ID = ?";
        return jdbcTemplate.queryForObject(sql, MAPPER, facilityId);
    }

    @Override
    public int insertFacilities(FacilitiesDto f) {
        String sql = """
            INSERT INTO FACILITIES(
                FACILITY_ID, DOMAIN, NAME, FLOOR, ZONE, ADDRESS,
                GPS_LAT, GPS_LNG, STATUS, MEMO, CREATED_AT, UPDATED_AT,
                MANAGERS_ID, INSPECTOR_ID
            ) VALUES(
                ?, ?, ?, ?, ?, ?,
                ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
                ?, ?
            )
            """;
        return jdbcTemplate.update(sql,
                f.getFacilityId(), f.getDomain(), f.getName(), f.getFloor(), f.getZone(), f.getAddress(),
                f.getGpsLat(), f.getGpsLng(), f.getStatus(), f.getMemo(),
                f.getManagersId(), f.getInspectorId()
        );
    }

    @Override
    public int updateFacilities(FacilitiesDto f) {
        String sql = """
            UPDATE FACILITIES
               SET DOMAIN = ?,
                   NAME = ?,
                   FLOOR = ?,
                   ZONE = ?,
                   ADDRESS = ?,
                   GPS_LAT = ?,
                   GPS_LNG = ?,
                   STATUS = ?,
                   MEMO = ?,
                   UPDATED_AT = CURRENT_TIMESTAMP,
                   MANAGERS_ID = ?,
                   INSPECTOR_ID = ?
             WHERE FACILITY_ID = ?
            """;
        return jdbcTemplate.update(sql,
                f.getDomain(), f.getName(), f.getFloor(), f.getZone(), f.getAddress(),
                f.getGpsLat(), f.getGpsLng(), f.getStatus(), f.getMemo(),
                f.getManagersId(), f.getInspectorId(),
                f.getFacilityId()
        );
    }

    @Override
    public int deleteFacilities(int facilityId, String unusedEmail) {
        String sql = "DELETE FROM FACILITIES WHERE FACILITY_ID = ?";
        return jdbcTemplate.update(sql, facilityId);
    }
}
