package com.abclonal.product;

import com.abclonal.product.web.Application;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
public class TestBase {
}
