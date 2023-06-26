package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.Graduation;
import com.restapi.scoregoat.domain.GraduationDto;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
@Service
public class GraduationMapper {

    public GraduationDto mapGraduationToGraduationDto(Graduation graduation) {
    return new GraduationDto(
            graduation.getRank(),
            graduation.getUser().getName(),
            graduation.getPoints()
    );}

    public List<GraduationDto> mapGraduationToGraduationDtoList(final List<Graduation> graduations) {
        return graduations.stream().map(this::mapGraduationToGraduationDto).collect(toList());
    }
}
