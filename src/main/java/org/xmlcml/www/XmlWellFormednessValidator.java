package org.xmlcml.www;

import nu.xom.Document;

public class XmlWellFormednessValidator extends AbstractValidator {

    @Override
    public boolean validate(Document doc) {
        return doc != null;
    }
}
