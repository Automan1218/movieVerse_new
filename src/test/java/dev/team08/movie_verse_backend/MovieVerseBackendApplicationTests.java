package dev.team08.movie_verse_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;;
@ActiveProfiles("test")
@SpringBootTest
@Import(TestDataSourceConfig.class)
class MovieVerseBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
