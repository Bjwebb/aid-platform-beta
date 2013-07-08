package uk.gov.dfid.data;

import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.Prop;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.Set;

/**
 * Entry point for loading XML into an embedded instance of BaseX
 */
public class Loader {
    /**
     * Main method.
     * @param args (ignored) command-line arguments
     * @throws Exception exception
     */
    public static void main(final String[] args) throws Exception {
        // --------------------------------------------------------------------
        // load all the iati dat into the databases
        Loader.seed();
    }

    /**
     * Seeds the database to give us access to the activities and organisations
     * @throws BaseXException
     */
    private static void seed() throws BaseXException {
        // --------------------------------------------------------------------
        // create the database context to execute stuff within
        final Context context = new Context();

        // --------------------------------------------------------------------
        // we cannot guarantee the quality of these IATI sources so we need
        // to tell the loader to skip corrupt ones.  We also simply want to load
        // everything in the folder not just the default .xml files.
        new Set(Prop.SKIPCORRUPT, true).execute(context);
        new Set(Prop.CREATEFILTER, "*").execute(context);

        // --------------------------------------------------------------------
        // create the organistaion and activities database from the XML folder
        new CreateDB("iati", "src/main/resources/xml/dfid/").execute(context);

        // --------------------------------------------------------------------
        // close the context and exit the loader
        context.close();
    }
}
