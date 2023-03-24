package com.restapi.scoregoat.client;

import com.restapi.scoregoat.config.FootballConfig;
import com.restapi.scoregoat.domain.ResponseDto;
import com.restapi.scoregoat.domain.SeasonDto;
import com.restapi.scoregoat.domain.SeasonsListDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FootballClientTests {
    private static final int LEAGUE_ID = 39;
    @InjectMocks
    private FootballClient client;
    @Mock
    private RestTemplate rest;
    @Mock
    private FootballConfig config;

    @Test
    public void shouldFetchSeasonYear() throws URISyntaxException {
        //Given
        when(config.getFootballApiEndpoint()).thenReturn("https://test.com");
        when(config.getFootballAppHeader()).thenReturn("AppHeaderTest");
        when(config.getFootballAppKey()).thenReturn("ApiKeyTest");

        URI uri = new URI("https://test.com/leagues?id=39");

        SeasonDto seasonDto = new SeasonDto("2023");

        SeasonsListDto list = new SeasonsListDto();
        list.getSeasonsDto().add(seasonDto);

        ResponseDto responseDto = new ResponseDto();
        responseDto.getResponse().add(list);

        HttpHeaders headers = new HttpHeaders();
        headers.set("AppHeaderTest", "ApiKeyTest");
        HttpEntity header = new HttpEntity<>(headers);

        ResponseEntity<ResponseDto> respond = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(rest.exchange(uri, HttpMethod.GET, header, ResponseDto.class)).thenReturn(respond);

        //When
        String year = client.getFootballSeason(LEAGUE_ID);

        //Then
        assertEquals("2023", year);
    }
}
