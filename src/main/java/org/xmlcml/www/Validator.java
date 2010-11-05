package org.xmlcml.www;

/**
 * Entry point for validation of CML.
 * <p/>
 * User: jat45
 * Date: 28-Oct-2010
 * Time: 15:48:36
 */
public class Validator {

    private XmlDocumentValidator xmlDocumentValidator = new XmlDocumentValidator();
    private SchemaValidator schemaValidator = SchemaValidator.newInstance();
    private ConventionValidator conventionValidator = ConventionValidator.newInstance();    

    public final static String CmlNS = "http://www.xml-cml.org/schema";

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
     * @param input              the document to validate
     * @param checkQNamesAreURLs - if true then all values which are QNames are reachable URLs
     * @return true if the document validates, false if there are any errors.
     */
    public boolean validate(String input, boolean checkQNamesAreURLs) {
        boolean valid = false;
        if (xmlDocumentValidator.validate(input)) {
            if (schemaValidator.validate(input)) {
                System.out.println("schema valid");
                if (conventionValidator.validate(input)) {
                    System.out.println("convention valid");
                    valid = true;
                }
            }
        }
        return valid;
    }

}
