package ru.practicum.ewm.stats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.ewm.stats.dto.HitRequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/schema.sql"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HitStatServiceTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @SneakyThrows
    @Test
    @Order(0)
    void hitCreationTest() {
        HitRequestDto body = createHit();
        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    @Sql(value = {"/schema.sql", "/create-test-hits.sql" })
    @Order(1)
    void getStatNotUniqueIpTest() {
        String start = "2023-06-01 00:00:00";
        String end = "2023-07-02 00:00:00";
        String uris = "/events/1";

        mockMvc.perform(get("/stats?start={start}&end={end}&uris={uris}", start, end, uris))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].app").value("ewm-main-service"))
                .andExpect(jsonPath("$[0].uri").value("/events/1"))
                .andExpect(jsonPath("$[0].hits").value(2));
    }

    private HitRequestDto createHit() {
        return HitRequestDto.builder()
                .ip("192.168.0.1")
                .app("ewm-main-service")
                .uri("/events/1")
                .timeStamp(LocalDateTime.of(2023, 7, 1, 0,0,0))
                .build();
    }
}
