package org.xmlcml.www;

import nu.xom.Builder;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

public class XmlDocumentValidator {

    private static Logger log = Logger.getLogger(SchemaValidator.class);

    private Builder builder = new Builder();

    public boolean validate(InputStream input) {
        try {
            builder.build(input);
            return true;
        } catch (ValidityException e) {
            log.info(e);
        } catch (ParsingException e) {
            log.info(e);
        } catch (IOException e) {
            log.info(e);
        } catch (Exception e) {
            log.info(e);
        }
        return false;
    }
}
