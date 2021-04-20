package it.tennisclubarzachena.scoreboards;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD;

// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Controller extends BaseController {

    /**
     * Model
     */
    public Model model;

    /**
     * Service
     */
    public Service service;

    /**
     * Controller constructor
     */
    public Controller() {
        super();
        this.service = new Service();
        this.model = new Model();
    }

    public JSONObject pippo(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        return new JSONObject("{\"pippo\": [] }");
    }



    public JSONObject test(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {

        // TODO: get createdAt by external HTTP request - maybe by get param
        JSONObject competitions = this.service.getCompetitions("07/04/2021");

        var competitionsCleaned = Helper.cleanCompetitions(competitions);
        System.out.println(competitionsCleaned);

//        for(int i = 0; i < competitionsCleaned.length(); i++) {
//            JSONObject entry = competitionsCleaned.getJSONObject(i);
//            System.out.println(entry);
//        }


        //Iterator<Boolean> objectIterator =  competitionsCleaned.iterator();

//        for(int i = 0; competitionsCleaned.length() < i; i++) {
//            JSONObject object = (JSONObject) competitionsCleaned.get(i);
//            System.out.println(object);
//        }


        // TODO: iterate all competitions and match only citta: Arzachena and competition status
//        String current = "16472";
//
//        String origin = "https://myfit.federtennis.it/";
//
//        JSONObject data = new JSONObject();
//        data.put("competitionId", current);
//        data.put("phaseId", "D5560D41-ADCB-454E-9495-FD503F26E192");
//        data.put("Guid", "NULL");
//
//        var result = Helper.post("/api/v3/tournament/bracket", origin, data.toString());

//        if (result == null) {
//            return "result is null";
//        }

        return competitions;
    }




}