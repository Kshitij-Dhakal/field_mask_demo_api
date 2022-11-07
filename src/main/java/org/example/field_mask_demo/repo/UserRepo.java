package org.example.field_mask_demo.repo;

import lombok.RequiredArgsConstructor;
import org.example.field_mask_demo.commons.StringUtils;
import org.example.pb.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class UserRepo {
    private final JdbcTemplate jdbcTemplate;

    private static User extractData(ResultSet rs, int rowNum) throws SQLException {
        return User.newBuilder()
                .setId(StringUtils.getNotNull(rs.getString(1)))
                .setName(StringUtils.getNotNull(rs.getString(2)))
                .setPicture(StringUtils.getNotNull(rs.getString(3)))
                .setEmail(StringUtils.getNotNull(rs.getString(4)))
                .setPassword(StringUtils.getNotNull(rs.getString(5)))
                .setCreated(rs.getLong(6))
                .setUpdated(rs.getLong(7))
                .build();
    }

    public boolean create(User user) {
        String query = """
                INSERT INTO "user"(
                        id,
                        name,
                        picture,
                        email,
                        password,
                        created,
                        updated
                    )
                VALUES(?, ?, ?, ?, ?, ?, ?)""";
        return jdbcTemplate.update(query,
                user.getId(),
                user.getName(),
                user.getPicture(),
                user.getEmail(),
                user.getPassword(),
                user.getCreated(),
                user.getUpdated()) == 1;
    }

    public boolean update(User user) {
        String query = """
                UPDATE "user"
                SET name = ?,
                    picture = ?,
                    email = ?,
                    password = ?,
                    updated = ?
                WHERE id = ?""";
        return jdbcTemplate.update(query,
                user.getName(),
                user.getPicture(),
                user.getEmail(),
                user.getPassword(),
                user.getUpdated(),
                user.getId()) == 1;
    }

    public User getById(String id) {
        String query = """
                SELECT id,
                    name,
                    picture,
                    email,
                    password,
                    created,
                    updated
                FROM "user"
                WHERE id = ?""";
        return jdbcTemplate.queryForObject(query,
                UserRepo::extractData,
                id);
    }
}
