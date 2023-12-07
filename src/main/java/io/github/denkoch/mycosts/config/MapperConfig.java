package io.github.denkoch.mycosts.config;

import io.github.denkoch.mycosts.config.mappers.CategoryMapper;
import io.github.denkoch.mycosts.config.mappers.PaymentMapper;
import io.github.denkoch.mycosts.config.mappers.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public UserMapper userMapper(){
        return new UserMapper();
    }

    @Bean
    public PaymentMapper paymentMapper(){
        return new PaymentMapper();
    }

    @Bean
    public CategoryMapper categoryMapper(){
        return new CategoryMapper();
    }

}
