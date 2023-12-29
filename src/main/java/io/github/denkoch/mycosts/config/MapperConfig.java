package io.github.denkoch.mycosts.config;

import io.github.denkoch.mycosts.category.models.Category;
import io.github.denkoch.mycosts.category.models.CategoryRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
