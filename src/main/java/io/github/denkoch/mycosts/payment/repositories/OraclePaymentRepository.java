package io.github.denkoch.mycosts.payment.repositories;

import io.github.denkoch.mycosts.exceptions.ResourceNotFoundException;
import io.github.denkoch.mycosts.payment.models.Payment;
import io.github.denkoch.mycosts.payment.models.enums.PaymentType;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Repository
@Primary
@AllArgsConstructor
public class OraclePaymentRepository implements PaymentFilterRepository {


    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_BY_USER_ID_AND_BY_ID = "SELECT * FROM payments WHERE user_id=? AND id=?";
    private static final String FIND_ALL_BY_USER_ID = "SELECT * FROM payments WHERE user_id=?";
    private static final String FIND_All_BY_DATE = "SELECT * FROM payments WHERE user_id=? AND date_time BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')";
    private static final String FIND_ALL_BY_CATEGORY = "SELECT * FROM payments WHERE user_id=? AND category_id=?";
    private static final String FIND_LOWEST_DATE = "SELECT MIN(date_time) FROM payments WHERE user_id = ?";
    private static final String UPDATE = "UPDATE payments SET amount=?, date_time=?, user_id=?, category_id=?, payment_type=? WHERE id=?";
    private static final String INSERT = "INSERT INTO payments(amount, date_time, user_id, category_id, payment_type) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM payments WHERE user_id=? AND id=?";
    private static final String DELETE_ALL_FOR_USER = "DELETE FROM payments WHERE user_id=?";

    private static final RowMapper ROW_MAPPER = (rs, rowNum) -> new Payment(rs.getLong(1),
            rs.getDouble(2), rs.getTimestamp(3).toLocalDateTime(),
            rs.getLong(4), rs.getLong(5), PaymentType.valueOf(rs.getString(6)));

    @Override
    public Collection<Payment> findAllByDate(Long userId, LocalDate before, LocalDate after) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return jdbcTemplate.query(FIND_All_BY_DATE, ROW_MAPPER, userId,
                after.format(formatter), before.format(formatter));
    }

    @Override
    public Collection<Payment> findAllByCategory(Long userId, Long categoryId) {
        return jdbcTemplate.query(FIND_ALL_BY_CATEGORY, ROW_MAPPER, userId, categoryId);
    }

    @Override
    public LocalDate findLowestDate(Long userId) {
        return jdbcTemplate.queryForObject(FIND_LOWEST_DATE, LocalDate.class, userId);
    }

    @Override
    public Payment findById(Long userId, Long id) {
        try {
            return (Payment) jdbcTemplate.queryForObject(FIND_BY_USER_ID_AND_BY_ID, ROW_MAPPER, userId, id);
        } catch (IncorrectResultSizeDataAccessException exception) {
            throw new ResourceNotFoundException("Payment {" + id + "} not found", exception);
        }
    }

    @Override
    public Collection<Payment> findAll(Long userId) {
        return jdbcTemplate.query(FIND_ALL_BY_USER_ID, ROW_MAPPER, userId);
    }


    private Payment create(Payment payment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(INSERT,
                Types.FLOAT, Types.TIMESTAMP, Types.NUMERIC, Types.NUMERIC, Types.VARCHAR);
        pscf.setGeneratedKeysColumnNames("id");

        PreparedStatementCreator preparedStatementCreator = pscf.newPreparedStatementCreator(
                new Object[]{payment.getMoneyAmount(), payment.getDateTime(),
                        payment.getUserId(), payment.getCategoryId(), payment.getPaymentType()});

        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Payment(id, payment.getMoneyAmount(), payment.getDateTime(),
                payment.getUserId(), payment.getCategoryId(), payment.getPaymentType());
    }

    private int update(Payment payment) {
        return jdbcTemplate.update(UPDATE, payment.getMoneyAmount(),
                payment.getDateTime(), payment.getUserId(), payment.getCategoryId(), payment.getPaymentType().name(), payment.getId());
    }

    @Override
    public Payment save(Payment payment) {
        if (update(payment) == 1) return payment;
        return create(payment);
    }

    @Override
    public void deleteById(Long userId, Long id) {
        jdbcTemplate.update(DELETE, userId, id);
    }

    public void deleteByUserId(Long userId) {
        jdbcTemplate.update(DELETE_ALL_FOR_USER, userId);
    }
}
