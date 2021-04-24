package org.sindria.nanoREST;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.sindria.nanoREST.controllers.BaseController;

// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

public abstract class BaseApp extends RouterNanoHTTPD {

    //protected T controller;
    //public T app;

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
    //private final UriRouter router;

    /**
     * BaseApp constructor
     */
    public BaseApp(String apiVersion, String serviceName) throws IOException {
        super(80);
        //this.controller = (T) typeController;

//        try {
//            this.app = typeApp.getDeclaredConstructor().newInstance();
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            e.printStackTrace();
//        }


        BaseApp.apiVersion = apiVersion;
        BaseApp.serviceName = serviceName;
        BaseApp.appRoutes = this.appRoutes();
        //router = new UriRouter();
        addMappings();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:80/ \n");
    }

    /**
     * Register app routes
     */
    @Override
    public void addMappings() {
        //router.setNotImplemented(NotImplementedHandler.class);
        //router.setNotFoundHandler(Error404UriHandler.class);

        //addRoute("/api/"+ BaseApp.apiVersion+"/"+ BaseApp.serviceName, Controller.class);

        for (String key : BaseApp.appRoutes.keySet()) {
            //addRoute("/api/"+ BaseApp.apiVersion+"/"+ BaseApp.serviceName+"/"+key, Controller.class);
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

//    /**
//     * Main application server
//     */
//    public void main(String[] args) {
//        try {
//            new this.app;
//        } catch (IOException ioe) {
//            System.err.println("Couldn't start server:\n" + ioe);
//        }
//    }

}
