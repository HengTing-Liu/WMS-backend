package com.abclonal.product.service.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestJobService {
    public void testJob(){
        System.out.println("hello test job");
    }
}
