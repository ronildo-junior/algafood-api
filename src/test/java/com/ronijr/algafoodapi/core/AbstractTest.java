package com.ronijr.algafoodapi.core;

import com.ronijr.algafoodapi.core.util.DatabaseCleaner;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.MethodName.class)
@TestPropertySource("/application-test.properties")
public abstract class AbstractTest {
    @Autowired
    protected DatabaseCleaner cleaner;

    protected static Stream<String> blankStrings() {
        return Stream.of("", " ", null);
    }
}