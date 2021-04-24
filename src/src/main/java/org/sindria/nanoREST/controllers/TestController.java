package org.sindria.nanoREST.controllers;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

import java.util.Map;
import org.json.JSONObject;

public class TestController<T> extends BaseController<TestController> {

    /**
     * Controller constructor
     */
    public TestController(Class<T> typeController) {
        super((Class<TestController>) typeController);
    }

    public JSONObject test(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        return new JSONObject("{\"test\": [] }");
    }
}