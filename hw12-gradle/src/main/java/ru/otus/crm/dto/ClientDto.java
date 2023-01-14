package ru.otus.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClientDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
}