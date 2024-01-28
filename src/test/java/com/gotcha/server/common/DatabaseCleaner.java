package com.gotcha.server.common;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {
    private final List<String> tableNames = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MongoTemplate mongoTemplate;

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

    private void truncateMongo() {
        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        for (String collectionName : collectionNames) {
            mongoTemplate.dropCollection(collectionName);
        }
    }

    @Transactional
    public void clear() {
        entityManager.clear();
        truncate();
        truncateMongo();
    }
}
