package com.github.almostfamiliar.product.web;

import com.github.almostfamiliar.product.web.mapper.CategoryWebMapperImpl;
import com.github.almostfamiliar.product.web.mapper.MoneyWebMapperImpl;
import com.github.almostfamiliar.product.web.mapper.ProductWebMapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
@Import({CategoryWebMapperImpl.class, ProductWebMapperImpl.class, MoneyWebMapperImpl.class})
public class WebTestConfiguration {}
