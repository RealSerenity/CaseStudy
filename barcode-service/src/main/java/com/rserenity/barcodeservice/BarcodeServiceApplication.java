package com.rserenity.barcodeservice;

import com.rserenity.barcodeservice.data.enums.BarcodeType;
import com.rserenity.barcodeservice.data.enums.CategoryUnitType;
import com.rserenity.barcodeservice.exception.BarcodeTypeNotFoundException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.Arrays;

@SpringBootApplication
@EnableFeignClients
public class BarcodeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarcodeServiceApplication.class, args);
	}

}
