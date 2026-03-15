package ru.otus.hw.configuration;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class CustomJdbcMutableAclService extends JdbcMutableAclService {

    private static final String DEFAULT_SELECT_CLASS_PRIMARY_KEY = "select id from acl_class where class=?";

    private static final String DEFAULT_SELECT_SID_PRIMARY_KEY = "select id from acl_sid where principal=? and sid=?";

    public CustomJdbcMutableAclService(
        DataSource dataSource,
        LookupStrategy lookupStrategy,
        AclCache aclCache
    ) {
        super(dataSource, lookupStrategy, aclCache);
    }

    @Override
    protected Long createOrRetrieveClassPrimaryKey(String type, boolean allowCreate, Class idType) {
        List<Long> classIds = jdbcOperations.queryForList(DEFAULT_SELECT_CLASS_PRIMARY_KEY, Long.class, type);

        if (!classIds.isEmpty()) {
            return classIds.get(0);
        }

        if (!allowCreate) {
            return null;
        }

        return insertAclClass(type, idType, new GeneratedKeyHolder());
    }

    @Override
    protected Long createOrRetrieveSidPrimaryKey(
        String sidName,
        boolean sidIsPrincipal,
        boolean allowCreate
    ) {
        List<Long> sidIds = jdbcOperations.queryForList(
            DEFAULT_SELECT_SID_PRIMARY_KEY, Long.class, sidIsPrincipal, sidName);
        if (!sidIds.isEmpty()) {
            return sidIds.get(0);
        }
        if (!allowCreate) {
            return null;
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "insert into acl_sid (principal, sid) values (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setBoolean(1, sidIsPrincipal);
            ps.setString(2, sidName);
            return ps;
        }, keyHolder);

        Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(),
            "Transaction must be running");
        return keyHolder.getKey().longValue();
    }

    private Long insertAclClass(String type, Class idType, KeyHolder keyHolder) {
        if (!isAclClassIdSupported()) {
            jdbcOperations.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "insert into acl_class (class) values (?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, type);
                return ps;
            }, keyHolder);
        } else {
            jdbcOperations.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "insert into acl_class (class, class_id_type) values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, type);
                ps.setString(2, idType.getCanonicalName());
                return ps;
            }, keyHolder);
        }
        Assert.isTrue(TransactionSynchronizationManager.isSynchronizationActive(), "Transaction must be running");
        Number key = keyHolder.getKey();
        return (key != null) ? key.longValue() : null;
    }
}
