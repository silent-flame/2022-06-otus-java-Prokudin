package ru.otus.server.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.http.HttpHeader;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;

import java.io.IOException;
import java.util.Collections;

import static ru.otus.server.Urls.CLIENT_LIST;


@RequiredArgsConstructor
public class AdminServlet extends HttpServlet {
    private static final String NAME_PARAM = "name";
    private static final String STREET_PARAM = "street";
    private static final String PHONE_PARAM = "phone";

    private final DBServiceClient dbServiceClient;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        var name = req.getParameter(NAME_PARAM);
        var street = req.getParameter(STREET_PARAM);
        var phoneParam = req.getParameter(PHONE_PARAM);


        var address = new Address();
        address.setStreet(street);

        var client = new Client();
        client.setName(name);
        client.setAddress(address);

        var phone = new Phone();
        phone.setNumber(phoneParam);
        client.setPhones(Collections.singletonList(phone));
        dbServiceClient.saveClient(client);

        resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        resp.setHeader(HttpHeader.LOCATION.asString(), CLIENT_LIST);
    }
}