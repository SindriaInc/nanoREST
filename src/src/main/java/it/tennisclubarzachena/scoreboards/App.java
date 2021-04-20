package it.tennisclubarzachena.scoreboards;

import java.io.IOException;
import java.util.HashMap;

public class App extends BaseApp {

    /**
     * App constructor
     */
    protected App() throws IOException {
        super("v1","scoreboards");
    }

    /**
     * Custom App routes
     */
    @Override
    public HashMap<String, String> appRoutes() {
        HashMap<String, String> routes = new HashMap<String, String>();

        routes.put("add", "add");
        routes.put("test", "test");
        routes.put("store", "store");
        routes.put("test/pippo", "pippo");

        return routes;
    }
}