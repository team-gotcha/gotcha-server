package com.gotcha.server.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {
    private final List<String> tableNames = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void afterPropertiesSet() {
        entityManager.createNativeQuery("SHOW TABLES")
                .getResultList().stream()
                .forEach(name->tableNames.add(String.valueOf(name)));
    }

    private void truncate() {
        entityManager.createNativeQuery("SET foreign_key_checks = 0").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }
        entityManager.createNativeQuery("SET foreign_key_checks = 1").executeUpdate();
    }

    @Transactional
    public void clear() {
        entityManager.clear();
        truncate();
    }
}
