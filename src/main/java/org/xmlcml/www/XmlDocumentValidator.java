package org.xmlcml.www;

import nu.xom.Builder;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class XmlDocumentValidator {

    private static Logger log = Logger.getLogger(SchemaValidator.class);

    private Builder builder = new Builder();
   
    public boolean validate(String input) {
        try {
            builder.build(IOUtils.toInputStream(input));
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
