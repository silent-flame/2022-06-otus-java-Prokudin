package ru.otus.crm.mapper;

import lombok.experimental.ExtensionMethod;
import ru.otus.crm.dto.ClientDto;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.util.ObjectUtils;

@ExtensionMethod(ObjectUtils.class)
public class ClientMapper {
    public static ClientDto convert(Client client) {
        var address = client.getAddress().let(Address::getStreet);
        var phoneNumber = client.getPhones()
                .let(phones ->
                        phones.size() > 0 ? phones.get(0) : null
                ).let(Phone::getNumber);
        return new ClientDto(client.getId(), client.getName(),
                address, phoneNumber);
    }
}