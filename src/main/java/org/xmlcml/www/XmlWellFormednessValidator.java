package org.xmlcml.www;

import nu.xom.Document;

/**
 *
 * @author Weerapong Phadungsukanan
 */
public class XmlWellFormednessValidator extends AbstractValidator {

    /**
     * An XML document is well-formed if it is not null.
     * Method validate(String input) and validate(File file) in the AbstractValidator
     * try to create XOM document first and pass it as argument to this method. If
     * XOM document cannot be created by any reason it is considered malformed.
     * 
     * @param doc
     * @return
     */
    @Override
    public boolean validate(Document doc) {
        return doc != null;
    }
}
