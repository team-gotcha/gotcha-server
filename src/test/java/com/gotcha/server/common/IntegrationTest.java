package com.gotcha.server.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles(profiles = "dev")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class IntegrationTest {
    @Autowired
    private DatabaseCleaner databaseCleaner;
    @Autowired
    protected IntegrationTestEnviron environ;
    @MockBean
    private TestRepository testRepository;

    @BeforeEach
    void beforeEach(){
        databaseCleaner.clear();
    }
}
