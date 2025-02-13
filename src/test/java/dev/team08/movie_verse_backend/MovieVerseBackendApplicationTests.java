package dev.team08.movie_verse_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "spring.datasource.url=jdbc:mysql://mysql:3306/movie_verse_pivot",
                "spring.datasource.username=user",
                "spring.datasource.password=123456"
        }
)
class MovieVerseBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
