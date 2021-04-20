package org.sindria.nanoREST;

import java.io.IOException;
import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.sindria.nanoREST.controllers.Controller;

// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

public abstract class BaseApp extends RouterNanoHTTPD {

    /**
     * apiVersion
     */
    public static String apiVersion;

    /**
     * serviceName
     */
    public static String serviceName;

    /**
     * appRoutes
     */
    public static HashMap<String, String> appRoutes;

    /**
     * UriRouter
     */
    private final UriRouter router;

    /**
     * BaseApp constructor
     */
    public BaseApp(String apiVersion, String serviceName) throws IOException {
        super(80);
        BaseApp.apiVersion = apiVersion;
        BaseApp.serviceName = serviceName;
        BaseApp.appRoutes = this.appRoutes();
        router = new UriRouter();
        addMappings();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:80/ \n");
    }

    @Override
    public void addMappings() {
        router.setNotImplemented(NotImplementedHandler.class);
        router.setNotFoundHandler(Error404UriHandler.class);

        addRoute("/api/"+ BaseApp.apiVersion+"/"+ BaseApp.serviceName, Controller.class);

        for (String key : BaseApp.appRoutes.keySet()) {
            addRoute("/api/"+ BaseApp.apiVersion+"/"+ BaseApp.serviceName+"/"+key, Controller.class);
        }
    }

    /**
     * Default App routes
     */
    public HashMap<String, String> appRoutes() {
        HashMap<String, String> routes = new HashMap<String, String>();
        routes.put("test", "test");
        return routes;
    }

    /**
     * Main application server
     */
    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

}
