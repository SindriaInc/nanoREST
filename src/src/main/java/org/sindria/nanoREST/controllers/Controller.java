package org.sindria.nanoREST.controllers;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

import java.util.Map;
import org.json.JSONObject;

public class Controller extends BaseController<Controller> {

    /**
     * Controller constructor
     */
    public Controller() {
        super(Controller.class);
    }

    public JSONObject test(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        return new JSONObject("{\"test\": [] }");
    }
}