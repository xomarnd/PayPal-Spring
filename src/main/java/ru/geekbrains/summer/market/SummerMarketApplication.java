package ru.geekbrains.summer.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

// Параметр VM Options для выбора профиля: -Dspring.profiles.active=dev

@SpringBootApplication
@PropertySource("classpath:secret.properties")
public class SummerMarketApplication {
	// Домашнее задание:
	// 1. Попробуйте допилить PayPal

	// План по магазину:

	// Занятие 11:
	// - Swagger
	// - Платежная система (PayPal)
	// - Картинки
	// - Разобраться с запуском memurai

	// Занятие 12:
	// - KeyCloak
	// - Регистрация

	// - Рассылка писем
	// - Админка
	// - Промо-коды

	public static void main(String[] args) {
		SpringApplication.run(SummerMarketApplication.class, args);
	}
}
