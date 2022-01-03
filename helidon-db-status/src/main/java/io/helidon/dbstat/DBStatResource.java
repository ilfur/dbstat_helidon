package io.helidon.dbstat;

import io.helidon.microprofile.cors.CrossOrigin;
import java.util.Collections;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

/**
 * A simple JAX-RS resource to greet you. Examples:
 *
 * Get default greeting message: curl -X GET http://localhost:8080/greet
 *
 * Get greeting message for Joe: curl -X GET http://localhost:8080/greet/Joe
 *
 * Change greeting curl -X PUT -H "Content-Type: application/json" -d
 * '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 *
 * The message is returned as a JSON object.
 */
@Path("/dbStat")
@RequestScoped
public class DBStatResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * The greeting message provider.
     */
    private final DBStatProvider greetingProvider;

    /**
     * Using constructor injection to get a configuration property. By default
     * this gets the value from META-INF/microprofile-config
     *
     * @param greetingConfig the configured greeting message
     */
    @Inject
    public DBStatResource(DBStatProvider greetingConfig) {
        this.greetingProvider = greetingConfig;
    }

    /**
     * Return a worldly greeting message.
     *
     * @return {@link JsonObject}
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDefaultMessage(@HeaderParam("Host") String host) {
        logger.info("Incoming from host " + host + " ! Returning user name and url: " + greetingProvider.getDbUser() + " " + greetingProvider.getDbUrl());
        JsonObject retJ = JSON.createObjectBuilder()
                .add("DBuser", greetingProvider.getDbUser())
                .add("DBurl", greetingProvider.getDbUrl())
                .build();
        /*return Response
                .status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods",
                        "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .entity(retJ)
                .build();*/
        return Response
                .status(200)
                .entity(retJ)
                .build();
    }

    @OPTIONS
    //@Path("/dbStat")
    @CrossOrigin(
            //value = {"http://localhost:8000","http://127.0.0.1:8000"},
            //allowHeaders = {"Accept","Accept-Encoding"},
            value = {"*"},
            allowMethods = {HttpMethod.GET})
    public void optionsForGetDefaultMessage() {}

    @Path("/restart")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response restart() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> {
            logger.info("Exiting Container with error to restart...");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            System.exit(5);
        });
        return Response
                .status(200)
                .entity("{\"restart\":true}")
                .build();
    }


    @OPTIONS
    @Path("/restart")
    @CrossOrigin(
            //value = {"http://localhost:8000","http://127.0.0.1:8000"},
            //allowHeaders = {"Accept","Accept-Encoding"},
            value = {"*"},
            allowMethods = {HttpMethod.GET})
    public void optionsForRestart() {}

    /**
     * Return a greeting message using the name that was provided.
     *
     * @param name the name to greet
     * @return {@link JsonObject}
     */
    @Path("/params")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessage(@HeaderParam("Host") String host) {
        logger.info("Incoming from host " + host + " ! Returning V$PARAMETERS as JSON");
        JsonObject retJ = null;
        String sql = "select name, type, value, display_value, default_value, isdefault from v$parameter";
        try {
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + greetingProvider.getDbUrl(), greetingProvider.getDbUser(), greetingProvider.getDbPwd());
            logger.info("Incoming from Host " + host + " - connection to DB successful");
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            JsonArrayBuilder arr = JSON.createArrayBuilder();
            while (res.next()) {
                JsonObjectBuilder row = JSON.createObjectBuilder();
                row = row.add("name", res.getString(1));
                row = row.add("type", res.getString(2));
                row = row.add("value", (res.getString(3) == null ? "null" : res.getString(3)));
                row = row.add("display_value", (res.getString(4) == null ? "null" : res.getString(4)));
                row = row.add("default_value", (res.getString(5) == null ? "null" : res.getString(5)));
                row = row.add("is_default", (res.getString(6) == null ? "null" : res.getString(6)));
                arr = arr.add(row);
            }
            res.close();
            conn.close();
            retJ = JSON.createObjectBuilder().add("params", arr).build();

        } catch (SQLException sqle) {
            logger.warning("Incoming from Host " + host + " - connection to DB or query failed: " + sqle.getMessage());
            retJ = JSON.createObjectBuilder()
                    .add("error", sqle.getMessage())
                    .build();

        }

        return Response
                .status(200)
                .entity(retJ)
                .build();
    }

    @OPTIONS
    @Path("/params")
    @CrossOrigin(
            //value = {"http://localhost:8000","http://127.0.0.1:8000"},
            //allowHeaders = {"Accept","Accept-Encoding"},
            value = {"*"},
            allowMethods = {HttpMethod.GET})
    public void optionsForGetMessage() {}
    
    
    private JsonObject createResponse(String who) {
        String msg = String.format("%s %s!", "yada", who);

        return JSON.createObjectBuilder()
                .add("message", msg)
                .build();
    }
}
