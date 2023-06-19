package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.Season;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SeasonRepositoryTests {
    @Autowired
    private SeasonRepository repository;

    @Test
    void testFindAllAndSave() {
        //Given
        Season season = new Season("2023");

        //When
        repository.deleteAll();
        repository.save(season);
        List<Season> list = repository.findAll().stream().filter(s -> s.getYear().equals("2023")).toList();

        //Then
        assertEquals(1, list.size());
        assertEquals("2023", list.get(0).getYear());

        //CleanUp
        try {
            repository.deleteById(list.get(0).getId());
        } catch (Exception ex) {
            //do nothing
        }
    }
}
