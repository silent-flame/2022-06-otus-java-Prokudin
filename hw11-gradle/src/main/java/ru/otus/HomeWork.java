package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.datasource.DriverManagerDataSource;
import ru.otus.mapper.*;
import ru.otus.service.DbServiceClientImpl;
import ru.otus.service.DbServiceManagerImpl;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 * VM options: -Xmx8m -Xms8m -Xlog:gc=debug
 */
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

// Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = EntityClassMetaDataImpl.of(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная

// Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientCacheProxy = new MyCache<>((Long id) -> dbServiceClient.getClient(id)
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + id)));
        var clientSecondSelected = supply(() -> clientCacheProxy.get(clientSecond.getId()));
        clientSecondSelected = supply(() -> clientCacheProxy.get(clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

// Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData<Manager> entityClassMetaDataManager = EntityClassMetaDataImpl.of(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl<>(entityClassMetaDataManager);
        var dataTemplateManager = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerCacheProxy = new MyCache<>((Long id) -> dbServiceManager.getManager(id)
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + id)));
        var managerSecondSelected = supply(() -> managerCacheProxy.get(managerSecond.getNo()));
        managerSecondSelected = supply(() -> managerCacheProxy.get(managerSecond.getNo()));

        for (long no = 0; no < 1_000; no++) {
            var manager = dbServiceManager.saveManager(new Manager("Manager %s".formatted(no)));
            var cachedManager = supply(() -> managerCacheProxy.get(manager.getNo()));
            cachedManager = supply(() -> managerCacheProxy.get(manager.getNo()));
        }

        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }

    private static <R> R supply(Supplier<R> supplier) {
        var start = System.currentTimeMillis();
        var result = supplier.get();
        var end = System.currentTimeMillis();
        log.info("Time: {} ms", (end - start));
        return result;
    }
}