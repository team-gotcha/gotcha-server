package com.gotcha.server.applicant.repository;

import static com.gotcha.server.common.TestFixture.종미면접관;
import static com.gotcha.server.common.TestFixture.종미면접관_지원자A;
import static com.gotcha.server.common.TestFixture.종미면접관_지원자B;
import static com.gotcha.server.common.TestFixture.종미면접관_지원자C;
import static com.gotcha.server.common.TestFixture.지원자A;
import static com.gotcha.server.common.TestFixture.지원자B;
import static com.gotcha.server.common.TestFixture.지원자C;
import static com.gotcha.server.common.TestFixture.테스트면접;
import static com.gotcha.server.common.TestFixture.테스트프로젝트;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gotcha.server.common.TestRepository;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Slf4j
@DataJpaTest
@Import(TestRepository.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InterviewerRepositoryTest {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private InterviewerRepository interviewerRepository;

    @Test
    @DisplayName("면접관이 오늘 예정된 면접 건수를 조회한다.")
    void 오늘_면접_개수_조회하기() {
        //given
        testRepository.save(종미면접관, 테스트프로젝트, 테스트면접);

        지원자A.setDate(LocalDate.now());
        지원자B.setDate(LocalDate.now());
        지원자C.setDate(LocalDate.now().plusDays(3L));
        testRepository.save(지원자A, 지원자B, 지원자C);

        testRepository.save(종미면접관_지원자A, 종미면접관_지원자B, 종미면접관_지원자C);

        // when
        long 조회된_면접_개수 = interviewerRepository.countTodayInterview(종미면접관);

        // then
        assertEquals(2L, 조회된_면접_개수);
    }
}