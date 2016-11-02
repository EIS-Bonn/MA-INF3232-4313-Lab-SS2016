package de.uni.bonn.iai.eis.etl.linkedpipes;

import java.util.HashMap;

public class ExecutionStatus {
    private static final HashMap<String, String> states = new HashMap<>();

    static {
        states.put("http://etl.linkedpipes.com/resources/status/mapped", "Mapped");
        states.put("http://etl.linkedpipes.com/resources/status/queued", "Queued");
        states.put("http://etl.linkedpipes.com/resources/status/initializing", "Initializing");
        states.put("http://etl.linkedpipes.com/resources/status/running", "Running");
        states.put("http://etl.linkedpipes.com/resources/status/finished", "Finished");
        states.put("http://etl.linkedpipes.com/resources/status/failed", "Failed");
    }

    public static String get(String uri) {
        return states.get(uri);
    }
};