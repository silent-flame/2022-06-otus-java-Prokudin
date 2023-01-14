package ru.otus.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.TemplateProcessor;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.server.auth.AuthorizationFilter;
import ru.otus.server.auth.LoginServlet;
import ru.otus.server.servlet.AdminServlet;
import ru.otus.server.servlet.ClientsServlet;

import java.util.Arrays;

import static ru.otus.server.Urls.ADD_CLIENT;
import static ru.otus.server.Urls.CLIENT_LIST;

public class JettyWebServerImpl implements JettyWebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;
    private final Server server;

    public JettyWebServerImpl(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor, int port) {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
        this.server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, CLIENT_LIST));


        server.setHandler(handlers);
        return server;
    }

    private Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(dbServiceClient, templateProcessor)), CLIENT_LIST);
        servletContextHandler.addServlet(new ServletHolder(new AdminServlet(dbServiceClient)), ADD_CLIENT);
        return servletContextHandler;
    }
}