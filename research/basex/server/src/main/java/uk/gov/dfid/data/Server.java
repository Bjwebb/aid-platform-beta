package uk.gov.dfid.data;

import org.basex.BaseXHTTP;
import org.basex.BaseXServer;

/**
 * Entry point for loading XML into an embedded instance of BaseX
 */
public class Server {
    /**
     * Main method.
     * @param args (ignored) command-line arguments
     * @throws Exception exception
     */
    public static void main(final String[] args) throws Exception {
        // --------------------------------------------------------------------
        // Server the server here
        final BaseXServer server = new BaseXServer();

        // --------------------------------------------------------------------
        // We want to try our best to shut these down accordingly
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println("Shutting down BaseX Server");

                    server.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("Running BaseX Server on port 1984");
    }
}
