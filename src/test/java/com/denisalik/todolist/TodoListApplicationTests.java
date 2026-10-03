package com.denisalik.todolist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class TodoListApplicationTests {

    @Test
    void contextLoads() {
//        todo add test for actuator and prometheus
    }

}
