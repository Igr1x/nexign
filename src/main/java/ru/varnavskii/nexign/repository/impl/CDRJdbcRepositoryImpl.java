package ru.varnavskii.nexign.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.varnavskii.nexign.entity.CDREntity;
import ru.varnavskii.nexign.repository.CDRJdbcRepository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CDRJdbcRepositoryImpl implements CDRJdbcRepository {

    private final static String INSERT_CDR_RECORD_SQL = """
            INSERT INTO cdr (call_type, calling, receiving, start_call, end_call)
            VALUES (?, ?, ?, ?, ?);
            """;

    private final static String FIND_ALL_BY_SUBSCRIBER_ID_AND_MONTH = """
            SELECT * FROM cdr
            WHERE calling = ? OR receiving = ?
                AND EXTRACT(MONTH FROM start_call) = ?;
            """;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<CDREntity> cdrRecords) {
        jdbcTemplate.batchUpdate(INSERT_CDR_RECORD_SQL,
                cdrRecords,
                100,
                (PreparedStatement ps, CDREntity cdr) -> {
                    ps.setString(1, cdr.getCallType().getValue());
                    ps.setLong(2, cdr.getCalling().getId());
                    ps.setLong(3, cdr.getReceiving().getId());
                    ps.setTimestamp(4, Timestamp.valueOf(cdr.getStartCall()));
                    ps.setTimestamp(5, Timestamp.valueOf(cdr.getEndCall()));
                });
    }

    @Override
    public List<CDREntity> findAllBySubscriberIdAndMonth(long subscriberId, Integer month) {
        return jdbcTemplate.query(FIND_ALL_BY_SUBSCRIBER_ID_AND_MONTH,
                new Object[]{subscriberId, subscriberId, month},
                new BeanPropertyRowMapper<>(CDREntity.class));
    }
}
