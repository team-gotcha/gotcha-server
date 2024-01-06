package com.gotcha.server.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Component;

@Component
public class TestRepository {

    @Autowired
    private TestEntityManager em;

    public void save(Object... objects) {
        for(Object o: objects) {
            em.persist(o);
        }
    }
}