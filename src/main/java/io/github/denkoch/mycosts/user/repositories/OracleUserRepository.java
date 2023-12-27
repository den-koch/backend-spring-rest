package io.github.denkoch.mycosts.user.repositories;

import io.github.denkoch.mycosts.exceptions.ResourceNotFoundException;
import io.github.denkoch.mycosts.user.models.User;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Collection;

@Repository
@Primary
@AllArgsConstructor
public class OracleUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_All = "SELECT id, name, email, password FROM users";
    private static final String FIND_BY_ID = "SELECT id, name, email, password FROM users WHERE id=?";
    private static final String DELETE_BY_ID = "DELETE FROM users WHERE id=?";
    private static final String UPDATE = "UPDATE users SET name=?, email=?, password=? WHERE id=?";
    private static final String INSERT = "INSERT INTO users(name, email, password) VALUES (?,?,?)";

    private static final RowMapper ROW_MAPPER = (rs, rowNum) -> new User(rs.getLong(1),
            rs.getString(2), rs.getString(3), rs.getString(4));

    @Override
    public User findById(Long id) {
        try {
            return (User) jdbcTemplate.queryForObject(FIND_BY_ID, ROW_MAPPER, id);
        } catch (IncorrectResultSizeDataAccessException exception) {
            throw new ResourceNotFoundException("User {" + id + "} not found", exception);
        }
    }

    @Override
    public Collection<User> findAll() {
        return jdbcTemplate.query(FIND_All, ROW_MAPPER);
    }

    private User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(INSERT,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR);
        pscf.setGeneratedKeysColumnNames("id");

        PreparedStatementCreator preparedStatementCreator = pscf.newPreparedStatementCreator(
                new Object[]{user.getName(), user.getEmail(), user.getPassword()});

        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new User(id, user.getName(), user.getEmail(), user.getPassword());
    }

    private int update(User user) {
        return jdbcTemplate.update(UPDATE, user.getName(), user.getEmail(), user.getPassword(), user.getId());
    }

    @Override
    public User save(User user) {
        if (update(user) == 1) return user;
        return create(user);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
