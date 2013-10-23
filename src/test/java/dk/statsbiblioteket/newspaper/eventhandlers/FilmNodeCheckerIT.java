package dk.statsbiblioteket.newspaper.eventhandlers;

import java.io.File;
import java.io.FileInputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.TreeIterator;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventRunner;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.filesystem.transforming.TransformingIteratorForFileSystems;
import dk.statsbiblioteket.newspaper.treenode.TreeNodeState;
import org.testng.annotations.Test;

/**
 */
public class FilmNodeCheckerIT {

    private final static String TEST_BATCH_ID = "400022028241";

    /**
     * Tests that the BatchStructureChecker can parse a production like batch.
     */
    //@Test(groups = "integrationTest")
    //This is a integration test, please reimplement using BatchStructureCheckerComponent
    //Yes, but it's still a valid test of a single component against the external file structure.
    public void testFileNodeChecker() throws Exception {
        String pathToProperties = System.getProperty("integration.test.newspaper.properties");
        Properties properties = new Properties();
        properties.load(new FileInputStream(pathToProperties));

        TreeIterator iterator = getIterator();
        EventRunner batchStructureChecker = new EventRunner(iterator);
        ResultCollector resultCollector = new ResultCollector("Batch Structure Checker", "v0.1");
        Batch batch = new Batch();
        batch.setBatchID(TEST_BATCH_ID);
        batch.setRoundTripNumber(1);
        List<TreeEventHandler> handlers = new ArrayList<>();
        TreeNodeState treeNodeState = new TreeNodeState();
        handlers.add(treeNodeState);
        handlers.add(new FilmNodeChecker(batch, treeNodeState, resultCollector));
        batchStructureChecker.runEvents(handlers);
    }

    /**
     * Creates and returns a iteration based on the test batch file structure found in the test/ressources folder.
     * @return A iterator the the test batch
     * @throws java.net.URISyntaxException
     */
    public TreeIterator getIterator() throws URISyntaxException {
        String pathToTestBatch = System.getProperty("integration.test.newspaper.testdata");
        File file = new File(pathToTestBatch + "/small-test-batch/");
        System.out.println(file);
        return new TransformingIteratorForFileSystems(file, "\\.", ".*\\.jp2$", ".md5");
    }
}
