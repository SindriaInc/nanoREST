package org.sindria.nanoREST.services;

import org.json.JSONObject;
import org.sindria.nanoREST.Helper;

public class BaseService {

    /**
     * Service constructor
     */
    public BaseService() {
        //
    }

    /**
     * Get all latest competitions by createdAt
     */
    public JSONObject getCompetitions(String createdAt) {

        String origin = "https://www.federtennis.it/";

        String json = "{\"guid\":\"\",\"profilazione\":\"\",\"freetext\":null,\"id_regione\":20,\"id_provincia\":90,\"id_stato\":null,\"id_disciplina\":4332,\"sesso\":null,\"data_inizio\": \""+createdAt+"\",\"data_fine\":null,\"tipo_competizione\":null,\"categoria_eta\":null,\"classifica\":null,\"massimale_montepremi\":null,\"id_area_regionale\":null,\"ambito\":null,\"rowstoskip\":0,\"fetchrows\":25,\"sortcolumn\":\"data_inizio\",\"sortorder\":\"asc\"}";
        JSONObject data = new JSONObject(json);

        return Helper.post("/api/v3/integration/puc/list", origin, data.toString());
    }
}