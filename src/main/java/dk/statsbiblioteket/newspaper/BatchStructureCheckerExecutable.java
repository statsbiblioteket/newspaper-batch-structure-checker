package dk.statsbiblioteket.newspaper;

import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.AutonomousComponentUtils;
import dk.statsbiblioteket.medieplatform.autonomous.CallResult;
import dk.statsbiblioteket.medieplatform.autonomous.RunnableComponent;
import dk.statsbiblioteket.newspaper.mfpakintegration.configuration.ConfigurationProperties;
import dk.statsbiblioteket.newspaper.mfpakintegration.configuration.MfPakConfiguration;
import dk.statsbiblioteket.newspaper.mfpakintegration.database.MfPakDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This is a sample component to serve as a guide to developers */
public class BatchStructureCheckerExecutable {
    private static Logger log = LoggerFactory.getLogger(BatchStructureCheckerExecutable.class);

    /**
     * The class must have a main method, so it can be started as a command line tool
     *
     * @param args the arguments.
     *
     * @throws Exception
     * @see AutonomousComponentUtils#parseArgs(String[])
     */
    public static void main(String... args) throws Exception {
        System.exit(doMain(args));
    }
    /**
     * The class must have a main method, so it can be started as a command line tool
     *
     * @param args the arguments.
     *
     * @throws Exception
     * @see AutonomousComponentUtils#parseArgs(String[])
     */
    private static int doMain(String[] args) throws Exception {
        log.info("Starting with args {}", args);

        //Parse the args to a properties construct
        Properties properties = AutonomousComponentUtils.parseArgs(args);

        MfPakConfiguration mfPakConfiguration = new MfPakConfiguration();
        mfPakConfiguration.setDatabaseUrl(properties.getProperty(ConfigurationProperties.DATABASE_URL));
        mfPakConfiguration.setDatabaseUser(properties.getProperty(ConfigurationProperties.DATABASE_USER));
        mfPakConfiguration.setDatabasePassword(properties.getProperty(ConfigurationProperties.DATABASE_PASSWORD));

        //make a new runnable component from the properties
        RunnableComponent component = new BatchStructureCheckerComponent(properties, new MfPakDAO(mfPakConfiguration));

        CallResult result = AutonomousComponentUtils.startAutonomousComponent(properties, component);
        System.out.print(result);

        if (result.getError() != null) return 2;
        else return result.containsFailures();
    }
}
