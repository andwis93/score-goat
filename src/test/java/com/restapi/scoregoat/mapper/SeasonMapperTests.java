package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.domain.client.mapJSON.Year;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SeasonMapperTests {
    @Autowired
    private SeasonMapper mapper;

    @Test
    void testMapSeasonToSeasonDto() {
        //Given
        Season season = new Season("2023");

        //When
        Year year = mapper.mapSeasonToSeasonDto(season);

        //Then
        assertEquals("2023", year.getYear());

    }
}
