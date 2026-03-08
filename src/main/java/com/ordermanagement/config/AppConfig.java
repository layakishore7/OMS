package com.ordermanagement.config;

import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(Product.class, ProductResponse.class).addMappings(mapper -> {
            mapper.map(src -> src.getOrganization().getId(), ProductResponse::setShipperId);
            mapper.map(src -> src.getCategory().getId(), ProductResponse::setCategoryId);
        });

        return modelMapper;
    }
}