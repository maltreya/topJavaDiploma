package com.topjava.restaurantvoiting.service;

import com.topjava.restaurantvoiting.TimingExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import static com.topjava.restaurantvoiting.util.ValidationUtil.getRootCause;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Sql(scripts = "classpath:data.sql", config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TimingExtension.class)
public abstract class AbstractServiceTest {
    @SuppressWarnings("all")
    protected <T extends Throwable> void validateRootCause(Class<T> rootExceptionClass, Runnable runnable){
        assertThrows(rootExceptionClass, () -> {
            try {
                runnable.run();
            }catch (Exception e){
                throw getRootCause(e);
            }
        });
    }
}
