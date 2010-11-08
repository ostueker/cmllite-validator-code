package org.xmlcml.www;

import java.io.File;
import nu.xom.Document;

/**
 * Entry point for validation of CML.
 * <p/>
 * User: jat45
 * Date: 28-Oct-2010
 * Time: 15:48:36
 */
public class Validator extends AbstractValidator {

    private XmlWellFormednessValidator xmlWellFormednessValidator = new XmlWellFormednessValidator();
    private SchemaValidator schemaValidator = new SchemaValidator();
    private ConventionValidator conventionValidator = new ConventionValidator();
    private QNameValidator qnameValidator = new QNameValidator();
    public final static String CmlNS = "http://www.xml-cml.org/schema";
    private final boolean checkQNamesAreURLs;

    public Validator(boolean checkQNamesAreURLs) {
        this.checkQNamesAreURLs = checkQNamesAreURLs;
    }

    /**
     * Validate the input. The validation is done in stages:
     * 1. Check it is valid XML
     * 2. Check it conforms to Schema 3
     * 3. If it contains any conventions which are known then check the document is valid against them
     * 4. If checkQNamesAreURLs is true then any value which is specified by the schema to be a QName
     * is checked to see if it is a reachable URL.
     * <p/>
     * If all tests pass then the result is true. (Note there may still be warnings but there won't be errors)
     * <p/>
     * The report @see getReport() contains all the errors and warnings found in the document.
     *
     * @return true if the document validates, false if there are any errors.
     */
    @Override
    public boolean validate(Document doc) {
        boolean valid = false;
        if (xmlWellFormednessValidator.validate(doc)) {
            if (schemaValidator.validate(doc)) {
                if (conventionValidator.validate(doc)) {
                    if (checkQNamesAreURLs) {
                        valid = qnameValidator.validate(doc);
                    } else {
                        valid = true;
                    }
                }
            }
        }
        return valid;
    }
}
