package ru.otus.demo;

import lombok.SneakyThrows;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.TemplateProcessor;
import ru.otus.crm.service.TemplateProcessorImpl;
import ru.otus.server.JettyWebServerImpl;

public class WebApplication {

    private static final Logger log = LoggerFactory.getLogger(WebApplication.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private static final String TEMPLATES_DIR = "/templates/";

    private static final int WEB_SERVER_PORT = 8080;

    @SneakyThrows
    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        var server = new JettyWebServerImpl(dbServiceClient, templateProcessor, WEB_SERVER_PORT);
        server.start();
        server.join();
    }
}