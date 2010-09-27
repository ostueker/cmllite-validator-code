package org.xmlcml.www;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import nu.xom.Builder;
import nu.xom.Document;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CmllLiteValidatorTest {

	CmlLiteValidator validator;
	Builder builder = new Builder();
	Collection<Document> validCmlLite = new LinkedList<Document>();
	Collection<Document> invalidCmlLite = new LinkedList<Document>();
	
	
	@Before
	public void setUp() throws Exception {
		validator = CmlLiteValidator.newInstance();
				
		Collection<File> validFiles = FileUtils.listFiles(new File("./src/test/resources/cmllite/valid"), new String[]{"cml"}, false);
		for (File file : validFiles) {
			validCmlLite.add(builder.build(file));			
		}
		
		Collection<File> invalidFiles = FileUtils.listFiles(new File("./src/test/resources/cmllite/invalid"), new String[]{"cml"}, false);
		for (File file : invalidFiles) {
			invalidCmlLite.add(builder.build(file));			
		}
		
	}

	@After
	public void tearDown() throws Exception {
		validCmlLite.clear();
		invalidCmlLite.clear();
	}
	
	@Test
	public void testValidCmlLite() {
		assertFalse("there should be valid test documents", validCmlLite.isEmpty());
		for (Document document : validCmlLite) {
			System.out.println("validating: "+document.getBaseURI());
			boolean isValid = validator.validate(document);
			assertTrue(document.getBaseURI()+" should be valid", isValid);
		}
	}
	
	@Test
	public void testInvalidCmlLite() {
		assertFalse("there should be invalid test documents", invalidCmlLite.isEmpty());		
		for (Document document : invalidCmlLite) {
			System.out.println("validating: "+document.getBaseURI());
			boolean isValid = validator.validate(document);
			assertFalse(document.getBaseURI()+" should be invalid", isValid);
		}
	}
	

}
