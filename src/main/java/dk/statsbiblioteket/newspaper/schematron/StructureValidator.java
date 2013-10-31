package dk.statsbiblioteket.newspaper.schematron;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.util.Strings;
import dk.statsbiblioteket.util.xml.DOM;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.schematron.SchematronException;
import com.phloc.schematron.pure.SchematronResourcePure;
import org.oclc.purl.dsdl.svrl.ActivePattern;
import org.oclc.purl.dsdl.svrl.FailedAssert;
import org.oclc.purl.dsdl.svrl.FiredRule;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.oclc.purl.dsdl.svrl.SuccessfulReport;
import org.w3c.dom.Document;

import java.io.InputStream;

/**
 * Class containing general-purpose functionality to validate an xml documents against a schematron document and gather
 * the results in a ResultCollector object.
 */
public class StructureValidator {

    public static final String TYPE = "BatchDirectoryStructure";
    private final ClassPathResource schemaResource;
    private final SchematronResourcePure schematron;

    /**
     * The constructor for this class.
     * @param schematronPath the path to the schematron document. This must be on the classpath of the current
     *                       ClassLoader.
     */
    public StructureValidator(String schematronPath) {
        schemaResource = new ClassPathResource(schematronPath);
        schematron = new SchematronResourcePure(schemaResource);
        if (!schematron.isValidSchematron()) {
            throw new RuntimeException("Failed to validate schematron resource as '"+schematronPath+"'");
        }

    }

    /**
     * Validate an xml document against this objects schematron and collect any failures.
     * @param batch The Batch object being validated.
     * @param contents An input stream which returns the xml to be validated.
     * @param resultCollector the ResultCollector in which the results are stored.
     * @return
     */
    public boolean validate(Batch batch, InputStream contents, ResultCollector resultCollector) {
        Document document = DOM.streamToDOM(contents);
        boolean success= true;
        SchematronOutputType result = null;
        try {
            result = schematron.applySchematronValidation(document);
        } catch (SchematronException e) {
            resultCollector.addFailure(batch.getFullID(),
                    TYPE,
                    getComponent(),
                    "Schematron Exception. Error was " + e
                            .toString(),
                    Strings.getStackTrace(e));
            success = false;
            return success;
        }
        for (Object o : result.getActivePatternAndFiredRuleAndFailedAssert()) {
            if (o instanceof FailedAssert) {
                success = false;
                FailedAssert failedAssert = (FailedAssert) o;
                resultCollector.addFailure(batch.getFullID(),
                        TYPE,
                        getComponent(),
                        failedAssert.getText(),
                        "Location: '" + failedAssert.getLocation() + "'",
                        "Test: '" + failedAssert.getTest() + "'");
            }
        }
        return success;
    }

    /**
        * Get the name of this component for error reporting purposes.
        *
        * @return the component name.
        */
       private String getComponent() {
           return getClass().getName() + "-" + getClass().getPackage().getImplementationVersion();
       }


}
