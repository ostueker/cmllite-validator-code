package org.xmlcml.www;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SchemaValidatorTest {

	private SchemaValidator validator = null;
		
	@Before
	public void setUp() throws Exception {
		validator = SchemaValidator.newInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidate() throws IOException {
		
		String text = "<?xml version=\"1.0\" ?>\r\n" + 
		"<cml convention=\"cmlDict:cmllite\"\r\n" + 
		"     xmlns=\"http://www.xml-cml.org/schema\"\r\n" + 
		"     xmlns:cmlDict=\"http://www.xml-cml.org/dictionary/cml/\"\r\n" + 
		"     xmlns:nameDict=\"http://www.xml-cml.org/dictionary/cml/name/\">\r\n" + 
		"  <molecule id=\"m1\">\r\n" + 
		"    <name dictRef=\"nameDict:trivial\">H_{2}O</name>\r\n" + 
		"    <name dictRef=\"nameDict:iupac\">oxidane</name>\r\n" + 
		"    <name dictRef=\"nameDict:trivial\">aqua</name>\r\n" + 
		"    <name dictRef=\"nameDict:trivial\">water</name>\r\n" + 
		"    <name dictRef=\"nameDict:systematic\">dihydrogen monoxide</name>\r\n" + 
		"    <name dictRef=\"nameDict:systematic\">hydrogen hydroxide</name>\r\n" + 
		"    <name dictRef=\"nameDict:trivial\">Adam's ale</name>\r\n" + 
		"    <formula concise=\"H 2 O 1\" />\r\n" + 
		"    <atomArray>\r\n" + 
		"      <atom id=\"a1\" elementType=\"O\"\r\n" + 
		"            x2=\"-1.5950000286102295\" y2=\"1.1549999713897705\" />\r\n" + 
		"      <atom id=\"a2\" elementType=\"H\"\r\n" + 
		"            x2=\"-0.05500002861022946\" y2=\"1.1549999713897705\" />\r\n" + 
		"      <atom id=\"a3\" elementType=\"H\"\r\n" + 
		"            x2=\"-2.3650000286102295\" y2=\"2.4886790932178062\" />\r\n" + 
		"    </atomArray>\r\n" + 
		"    <bondArray>\r\n" + 
		"      <bond id=\"b1\" atomRefs2=\"a1 a2\" order=\"1\" />\r\n" + 
		"      <bond id=\"b2\" atomRefs2=\"a1 a3\" order=\"1\" />\r\n" + 
		"    </bondArray>\r\n" + 
		"  </molecule>\r\n" + 
		"</cml>";
	
		InputStream is = IOUtils.toInputStream(text, "UTF-8");
		
		boolean isValid = validator.validate(is);
		assertTrue("should be valid", isValid);
		
	}

}
