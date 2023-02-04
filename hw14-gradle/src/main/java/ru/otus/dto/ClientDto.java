package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
@Builder
public class ClientDto {
    private Long id;
    private String name;
    private String address;
    private String phone;

    public Map<String, Object> toMap() {
        return Map.of("id", id,
                "name", name,
                "address", address,
                "phone", phone);
    }
}