package org.sindria.nanoREST.controllers;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;
import org.json.JSONObject;
import org.sindria.nanoREST.BaseApp;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

public abstract class BaseController extends RouterNanoHTTPD.GeneralHandler {

    /**
     * Controller sigleton
     */
    private static Controller INSTANCE;

    /**
     * Controller instance
     */
    public static Controller getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Controller();
        }
        return INSTANCE;
    }

    /**
     * apiVersion
     */
    public String apiVersion;

    /**
     * serviceName
     */
    public String serviceName;

    /**
     * reservedUri
     */
    protected String reservedUri;

    /**
     * BaseController constructor
     */
    public BaseController() {
        this.apiVersion = BaseApp.apiVersion;
        this.serviceName = BaseApp.serviceName;
        this.reservedUri = "api/" + apiVersion + "/" + serviceName;
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

    /**
     * Wrapper call to action
     */
    @Override
    public NanoHTTPD.Response get(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        Object result = null;
        try {
            result = this.callControllerAction(uriResource, urlParams, session);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        assert result != null;
        return NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), result.toString());
    }

    /**
     * Call controller action
     */
    public JSONObject callControllerAction(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String currentUri = uriResource.getUri();

        if (currentUri.equals(this.reservedUri)) {
            return new JSONObject("{\"resource\":{\"message\":\"This route is reserved for crud controller\"}}");
        }

        String methodMatched = this.matchUriMethod(currentUri);

        var controller = BaseController.getInstance();
        var methodCall = controller.getClass().getMethod(methodMatched, RouterNanoHTTPD.UriResource.class, Map.class, NanoHTTPD.IHTTPSession.class);
        return (JSONObject) methodCall.invoke(controller, uriResource, urlParams, session);
    }

    /**
     * Match method for current uri
     */
    private String matchUriMethod(String currentUri) {
        String methodName = null;

        for (String key : BaseApp.appRoutes.keySet()) {

            String uriPath = this.reservedUri + "/" + key;

            if (currentUri.equals(uriPath)) {
                methodName = BaseApp.appRoutes.get(key);
            }
        }
        return methodName;
    }
}