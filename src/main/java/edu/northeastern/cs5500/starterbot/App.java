/**
 * App actually starts the app.
 */
package edu.northeastern.cs5500.starterbot;

import static spark.Spark.get;
import static spark.Spark.port;

public class App {

    public static void main(String[] arg) {
        // starts the bot with Bot component being created
        DaggerBotComponent.create().bot().start();

        port(8080);
        // check the status of the bot
        get("/", (request, response) -> "{\"status\": \"OK\"}");
    }
}
