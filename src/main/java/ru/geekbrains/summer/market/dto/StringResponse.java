package ru.geekbrains.summer.market.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.summer.market.model.Product;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class StringResponse {
    private String value;

    public StringResponse(String value) {
        this.value = value;
    }
}
