package io.github.denkoch.mycosts.category.repositories;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.exceptions.ResourceAlreadyExistsException;
import io.github.denkoch.mycosts.exceptions.ResourceNotFoundException;
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
public class OracleCategoryRepository implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_BY_USER_ID = "SELECT id, label, user_id FROM categories WHERE user_id=?";
    private static final String FIND_BY_USER_ID_AND_BY_ID = "SELECT id, label, user_id FROM categories WHERE user_id=? AND id=?";
    private static final String COUNT_BY_USER_ID_AND_LABEL = "SELECT COUNT(*) FROM categories WHERE user_id=? AND label=?";
    private static final String INSERT = "INSERT INTO categories(label, user_id) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM categories WHERE user_id=? AND id=?";
    private static final String DELETE_ALL_FOR_USER = "DELETE FROM categories WHERE user_id=?";

    private static final RowMapper ROW_MAPPER = (rs, rowNum) -> new Category(rs.getLong(1),
            rs.getString(2), rs.getLong(3));


    @Override
    public Category findById(Long userId, Long id) {
        try {
            return (Category) jdbcTemplate.queryForObject(FIND_BY_USER_ID_AND_BY_ID, ROW_MAPPER, userId, id);
        } catch (IncorrectResultSizeDataAccessException exception) {
            throw new ResourceNotFoundException("Category {" + id + "} not found", exception);
        }
    }

    public Integer findByLabel(Long userId, String label) {
        return jdbcTemplate.queryForObject(COUNT_BY_USER_ID_AND_LABEL, Integer.class, userId, label);
    }


    @Override
    public Collection<Category> findAll(Long userId) {
        return jdbcTemplate.query(FIND_ALL_BY_USER_ID, ROW_MAPPER, userId);
    }

    private Category create(Category category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(INSERT,
                Types.VARCHAR, Types.NUMERIC);
        pscf.setGeneratedKeysColumnNames("id");

        PreparedStatementCreator preparedStatementCreator = pscf.newPreparedStatementCreator(
                new Object[]{category.getLabel(), category.getUserId()});

        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Category(id, category.getLabel(), category.getUserId());
    }

    @Override
    public Category save(Category category) {
        if (findByLabel(category.getUserId(), category.getLabel()) != 0)
            throw new ResourceAlreadyExistsException("Category already exists");
        return create(category);
    }

    @Override
    public void deleteById(Long userId, Long id) {
        jdbcTemplate.update(DELETE, userId, id);
    }

    public void deleteByUserId(Long userId) {
        jdbcTemplate.update(DELETE_ALL_FOR_USER, userId);
    }

}
