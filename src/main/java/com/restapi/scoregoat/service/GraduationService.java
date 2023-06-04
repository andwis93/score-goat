package com.restapi.scoregoat.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class GraduationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchService.class);

}
