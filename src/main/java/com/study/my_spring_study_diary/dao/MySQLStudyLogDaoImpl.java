package com.study.my_spring_study_diary.dao;


import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MySQL based StudyLog DAO implementation
 *
 * JdbcTemplate usage:
 * - JDBC helper class provided by Spring
 * - Automatically manages Connection, Statement, etc.
 * - Converts SQL exceptions to Spring's DataAccessException
 * - Reduces boilerplate code
 */
@Repository
public class MySQLStudyLogDaoImpl implements StudyLogDao {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor injection
     * JdbcTemplate is automatically registered as Bean by Spring
     */
    public MySQLStudyLogDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ========== CREATE ==========

    @Override
    public StudyLog save(StudyLog studyLog) {
        String sql = """
            INSERT INTO diary_entries (title, content, category, understanding, study_time, study_date)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        // KeyHolder: Object to receive auto-generated ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, studyLog.getTitle());
            ps.setString(2, studyLog.getContent());
            ps.setString(3, studyLog.getCategory() != null ? studyLog.getCategory().name() : null);
            ps.setString(4, studyLog.getUnderstanding() != null ? studyLog.getUnderstanding().name() : null);

            if (studyLog.getStudyTime() != null) {
                ps.setInt(5, studyLog.getStudyTime());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            if (studyLog.getStudyDate() != null) {
                ps.setDate(6, Date.valueOf(studyLog.getStudyDate()));
            } else {
                ps.setNull(6, Types.DATE);
            }
            return ps;
        }, keyHolder);

        // Set the generated ID to StudyLog object
        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            studyLog.setId(generatedId.longValue());
        }

        return studyLog;
    }

    // ========== READ ==========

    @Override
    public Optional<StudyLog> findById(Long id) {
        String sql = "SELECT * FROM diary_entries WHERE id = ?";

        try {
            StudyLog studyLog = jdbcTemplate.queryForObject(sql, studyLogRowMapper, id);
            return Optional.ofNullable(studyLog);
        } catch (Exception e) {
            // Return Optional.empty() when no data exists as exception occurs
            return Optional.empty();
        }
    }

    @Override
    public List<StudyLog> findAll() {
        String sql = "SELECT * FROM diary_entries ORDER BY study_date DESC, id DESC";
        return jdbcTemplate.query(sql, studyLogRowMapper);
    }

    @Override
    public List<StudyLog> findByCategory(String category) {
        String sql = "SELECT * FROM diary_entries WHERE category = ? ORDER BY study_date DESC, id DESC";
        return jdbcTemplate.query(sql, studyLogRowMapper, category);
    }

    @Override
    public List<StudyLog> findByStudyDate(LocalDate date) {
        String sql = "SELECT * FROM diary_entries WHERE study_date = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, studyLogRowMapper, Date.valueOf(date));
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM diary_entries WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM diary_entries";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    // ========== UPDATE ==========

    @Override
    public StudyLog update(StudyLog studyLog) {
        String sql = """
            UPDATE diary_entries
            SET title = ?, content = ?, category = ?, understanding = ?,
                study_time = ?, study_date = ?
            WHERE id = ?
            """;

        int updated = jdbcTemplate.update(sql,
                studyLog.getTitle(),
                studyLog.getContent(),
                studyLog.getCategory().name(),
                studyLog.getUnderstanding().name(),
                studyLog.getStudyTime(),
                Date.valueOf(studyLog.getStudyDate()),
                studyLog.getId());

        if (updated == 0) {
            throw new RuntimeException("Study log not found. ID: " + studyLog.getId());
        }

        return studyLog;
    }

    // ========== DELETE ==========

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM diary_entries WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        return deleted > 0;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM diary_entries";
        jdbcTemplate.update(sql);
    }

    // ========== PRIVATE METHODS ==========

    /**
     * RowMapper: Converts each row of ResultSet to StudyLog object
     * Can be simply implemented with lambda expression
     */
    private final RowMapper<StudyLog> studyLogRowMapper = (rs, rowNum) -> {
        StudyLog studyLog = new StudyLog();
        studyLog.setId(rs.getLong("id"));
        studyLog.setTitle(rs.getString("title"));
        studyLog.setContent(rs.getString("content"));
        studyLog.setCategory(Category.valueOf(rs.getString("category")));
        studyLog.setUnderstanding(Understanding.valueOf(rs.getString("understanding")));
        studyLog.setStudyTime(rs.getInt("study_time"));
        
        // study_date가 null일 수 있으므로 null-safe 처리
        Date studyDate = rs.getDate("study_date");
        if (studyDate != null) {
            studyLog.setStudyDate(studyDate.toLocalDate());
        }
        
        return studyLog;
    };
}
