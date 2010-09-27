package org.xmlcml.www;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

public class QNameValidatorTest {
	
	private String goodUriString = "http://www.xml-cml.org/dictionary/cml/name/iupac";
	private String badUriString = "http://opsin.cam.ac.uk";
	private String anotherUriString = "http://another.com";
	private URI goodUri = null;
	private URI badUri = null;
	private URI anotherUri = null;
	private Set<URI> set = null; 
	private Document document = null;
	private Builder builder = new Builder();
	private XStream xStream = new XStream();
	
	private File workspace;
	
	@Before
	public void setUp() throws Exception {
		workspace = new File("target"+File.separatorChar+"temp"+File.separatorChar+UUID.randomUUID().toString());
		if (!workspace.mkdirs()) {
			throw new IOException("Failed to create workspace: "+workspace);
		}				
		goodUri = URI.create(goodUriString);
		badUri = URI.create(badUriString);
		anotherUri = URI.create(anotherUriString);
		set = new HashSet<URI>(2);
		set.add(goodUri);
		set.add(badUri);
		
		String uri = "http://www.xml-cml.org/schema";		
		Element cml = new Element("cml", uri);
		cml.addNamespaceDeclaration("nameDict", "http://www.xml-cml.org/dictionary/cml/name/");
		Element name = new Element("name", uri);
		name.addAttribute(new Attribute("dictRef", "nameDict:iupac"));
		cml.appendChild(name);
		this.document=new Document(cml);	
	}

	@After
	public void tearDown() throws Exception {
//		if (workspace.isDirectory()) {
//			FileUtils.deleteDirectory(workspace);
//		}
	}
	
	@Test 
	public void testConstructorWithNonExistantReachableAndUnreachableStorageFiles() {
		String reachableUrisStorage = workspace.getAbsolutePath()+File.separatorChar+"reachable.xml";
		String unreachableUrisStorage = workspace.getAbsolutePath()+File.separatorChar+"unreachable.xml";
		QNameValidator validator = new QNameValidator(reachableUrisStorage, unreachableUrisStorage);
		assertNotNull("should not be null - regardless of whether or not the files exist", validator);
		File reachableUris = new File(reachableUrisStorage);
		assertTrue("reachableUriStorage should be created", reachableUris.exists());
		File unreachableUris = new File(unreachableUrisStorage);
		assertTrue("unreachableUriStorage should be created", unreachableUris.exists());		
	}
	
	@Test 
	public void testConstructorWithReachableStorageFile() throws Exception {
		File reachableUrisStorage = setupReachableUrisStorage(workspace.getAbsolutePath()+File.separatorChar+"reachable.xml");
		String unreachableUrisStorage = workspace.getAbsolutePath()+File.separatorChar+"unreachable.xml";
		
		QNameValidator validator = new QNameValidator(reachableUrisStorage.toString(), unreachableUrisStorage);

		File unreachableUris = new File(unreachableUrisStorage);
		assertTrue("unreachableUriStorage should be created", unreachableUris.exists());	

		assertNotNull("should not be null - regardless of whether or not the files exist", validator);
		
		Collection<URI> allReachableUris = validator.getAllReachableUris();
		assertNotNull("should not be null", allReachableUris);
		assertTrue(this.goodUri+" should be in the collection", allReachableUris.contains(this.goodUri));
		assertFalse(this.badUri+" should not be in the collection", allReachableUris.contains(this.badUri));		
		
		Collection<URI> allUnreachableUris = validator.getAllUnreachableUris();
		assertNotNull("should not be null", allUnreachableUris);
		assertTrue("collection should be empty", allUnreachableUris.isEmpty());
	}
	
	@Test 
	public void testConstructorWithUnreachableStorageFile() throws Exception {
		File unreachableUrisStorage = setupUnreachableUrisStorage(workspace.getAbsolutePath()+File.separatorChar+"unreachable.xml");
		String reachableUrisStorage = workspace.getAbsolutePath()+File.separatorChar+"reachable.xml";
		
		QNameValidator validator = new QNameValidator(reachableUrisStorage, unreachableUrisStorage.toString());

		File reachableUris = new File(reachableUrisStorage);
		assertTrue("reachableUriStorage should be created", reachableUris.exists());	

		assertNotNull("should not be null - regardless of whether or not the files exist", validator);
		
		Collection<URI> allReachableUris = validator.getAllReachableUris();
		assertNotNull("should not be null", allReachableUris);
		assertTrue("collection should be empty", allReachableUris.isEmpty());
		
		Collection<URI> allUnreachableUris = validator.getAllUnreachableUris();
		assertNotNull("should not be null", allUnreachableUris);
		assertTrue(this.badUri+" should be in the collection", allUnreachableUris.contains(this.badUri));
		assertFalse(this.goodUri+" should not be in the collection", allUnreachableUris.contains(this.goodUri));			
	}
	
	@Test 
	public void testConstructorWithReachableAndUnreachableStorageFile() throws Exception {
		File reachableUrisStorage = setupReachableUrisStorage(workspace.getAbsolutePath()+File.separatorChar+"reachable.xml");
		File unreachableUrisStorage = setupUnreachableUrisStorage(workspace.getAbsolutePath()+File.separatorChar+"unreachable.xml");
		
		QNameValidator validator = new QNameValidator(reachableUrisStorage.toString(), unreachableUrisStorage.toString());
		assertNotNull("should not be null - regardless of whether or not the files exist", validator);
		
		Collection<URI> allReachableUris = validator.getAllReachableUris();
		assertNotNull("should not be null", allReachableUris);
		assertTrue(this.goodUri+" should be in the collection", allReachableUris.contains(this.goodUri));
		assertFalse(this.badUri+" should not be in the collection", allReachableUris.contains(this.badUri));			
		
		Collection<URI> allUnreachableUris = validator.getAllUnreachableUris();
		assertNotNull("should not be null", allUnreachableUris);
		assertTrue(this.badUri+" should be in the collection", allUnreachableUris.contains(this.badUri));
		assertFalse(this.goodUri+" should not be in the collection", allUnreachableUris.contains(this.goodUri));	
		
	}
	
	private File setupReachableUrisStorage(String path) throws Exception {
		File file = new File(path);
		OutputStream output = FileUtils.openOutputStream(file);
		String xml = "<set>\r\n" + 
				"  <java.net.URI serialization=\"custom\">\r\n" + 
				"    <java.net.URI>\r\n" + 
				"      <default>\r\n" + 
				"        <string>"+this.goodUriString+"</string>\r\n" + 
				"      </default>\r\n" + 
				"    </java.net.URI>\r\n" + 
				"  </java.net.URI>\r\n" + 
				"</set>" + 
				"</map>";
		IOUtils.write(xml, output);
		IOUtils.closeQuietly(output);
		return file;		
	}

	private File setupUnreachableUrisStorage(String path) throws Exception {
		File file = new File(path);
		OutputStream output = FileUtils.openOutputStream(file);
		String xml = "<set>\r\n" + 
				"  <java.net.URI serialization=\"custom\">\r\n" + 
				"    <java.net.URI>\r\n" + 
				"      <default>\r\n" + 
				"        <string>"+this.badUriString+"</string>\r\n" + 
				"      </default>\r\n" + 
				"    </java.net.URI>\r\n" + 
				"  </java.net.URI>\r\n" + 
				"</set>" + 
				"</map>";
		IOUtils.write(xml, output);
		IOUtils.closeQuietly(output);
		return file;		
	}
	
	@Test
	public void testIsReachable1() {		
		assertTrue(goodUriString+ " should be reachable ", QNameValidator.isReachable(goodUri));
	}

	@Test
	public void testIsReachable2() {
		assertFalse(badUriString+ " should not be reachable ", QNameValidator.isReachable(badUri));
	}
	
	@Test
	public void testAreReachable() {
		Map<URI, Boolean> map = QNameValidator.areReachable(set);
		assertTrue("should be in map", map.containsKey(goodUri));
		assertTrue("should be in map", map.containsKey(badUri));
		assertFalse("should not be in map", map.containsKey(anotherUri));	
		assertTrue(goodUriString+ " is reachable ", map.get(goodUri).booleanValue());
		assertFalse(badUriString+ " is not reachable ", map.get(badUri).booleanValue());
	}
	
	@Test
	public void testValidate() {
		QNameValidator validator = new QNameValidator();
		boolean isValid = validator.validate(this.document);
		assertTrue("should be valid", isValid);
		assertTrue("only single valid uri", validator.getReachableUrisFromCurrentDocument().size() == 1);
	}
	
	
	@Test
	public void testValidate1() throws ValidityException, ParsingException, IOException {		
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
		Document doc = builder.build(is);		
				
		QNameValidator validator = new QNameValidator();
		boolean isValid = validator.validate(doc);
		assertFalse("should not be valid", isValid);
		
		Collection<URI> reachableUris = validator.getReachableUrisFromCurrentDocument();
		assertEquals("only the iupac entry resolves", 1, reachableUris.size());
				
		Collection<URI> unreachableUris = validator.getUnreachableUrisFromCurrentDocument();
		assertEquals("there should be 2 unreachable uris",2, unreachableUris.size());
	}
	
	@Test
	public void testValidateWithNameSpacePrefix() throws ValidityException, ParsingException, IOException {		
		String text = "<?xml version=\"1.0\" ?>\r\n" + 
		"<cml:cml convention=\"cmlDict:cmllite\"\r\n" + 
		"     xmlns:cml=\"http://www.xml-cml.org/schema\"\r\n" + 
		"     xmlns:cmlDict=\"http://www.xml-cml.org/dictionary/cml/\"\r\n" + 
		"     xmlns:nameDict=\"http://www.xml-cml.org/dictionary/cml/name/\">\r\n" + 
		"  <cml:molecule id=\"m1\">\r\n" + 
		"    <cml:name dictRef=\"nameDict:trivial\">H_{2}O</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:iupac\">oxidane</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:trivial\">aqua</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:trivial\">water</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:systematic\">dihydrogen monoxide</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:systematic\">hydrogen hydroxide</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:trivial\">Adam's ale</cml:name>\r\n" + 
		"    <cml:atomArray>\r\n" + 
		"      <cml:atom id=\"a1\" elementType=\"O\"\r\n" + 
		"            x2=\"-1.5950000286102295\" y2=\"1.1549999713897705\" />\r\n" + 
		"      <cml:atom id=\"a2\" elementType=\"H\"\r\n" + 
		"            x2=\"-0.05500002861022946\" y2=\"1.1549999713897705\" />\r\n" + 
		"      <cml:atom id=\"a3\" elementType=\"H\"\r\n" + 
		"            x2=\"-2.3650000286102295\" y2=\"2.4886790932178062\" />\r\n" + 
		"    </cml:atomArray>\r\n" + 
		"    <cml:bondArray>\r\n" + 
		"      <cml:bond id=\"b1\" atomRefs2=\"a1 a2\" order=\"1\" />\r\n" + 
		"      <cml:bond id=\"b2\" atomRefs2=\"a1 a3\" order=\"1\" />\r\n" + 
		"    </cml:bondArray>\r\n" + 
		"  </cml:molecule>\r\n" + 
		"</cml:cml>";
	
		InputStream is = IOUtils.toInputStream(text, "UTF-8");
		Document doc = builder.build(is);		
				
		QNameValidator validator = new QNameValidator();
		boolean isValid = validator.validate(doc);
		assertFalse("should not be valid", isValid);
		
		Collection<URI> reachableUris = validator.getReachableUrisFromCurrentDocument();
		assertEquals("only the iupac entry resolves", 1, reachableUris.size());
				
		Collection<URI> unreachableUris = validator.getUnreachableUrisFromCurrentDocument();
		assertEquals("there should be 2 unreachable uris",2, unreachableUris.size());
	}
	
	@Test
	public void testValidateWithIncorrectNameSpacePrefix() throws ValidityException, ParsingException, IOException {		
		String text = "<?xml version=\"1.0\" ?>\r\n" + 
		"<cml:cml convention=\"cmlDict:cmllite\"\r\n" + 
		"     xmlns:cml=\"http://www.xml-cml.org/schema\"\r\n" +
		"     xmlns:other=\"http://www.other.org\"\r\n" +
		"     xmlns:cmlDict=\"http://www.xml-cml.org/dictionary/cml/\"\r\n" + 
		"     xmlns:nameDict=\"http://www.xml-cml.org/dictionary/cml/name/\">\r\n" +
		"  <cml:molecule id=\"m1\">\r\n" + 
		"    <cml:name dictRef=\"nameDict:trivial\">H_{2}O</cml:name>\r\n" + 
		"    <cml:name other:dictRef=\"nameDict:iupac\">oxidane</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:trivial\">aqua</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:trivial\">water</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:systematic\">dihydrogen monoxide</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:systematic\">hydrogen hydroxide</cml:name>\r\n" + 
		"    <cml:name dictRef=\"nameDict:trivial\">Adam's ale</cml:name>\r\n" + 
		"    <cml:atomArray>\r\n" + 
		"      <cml:atom id=\"a1\" elementType=\"O\"\r\n" + 
		"            x2=\"-1.5950000286102295\" y2=\"1.1549999713897705\" />\r\n" + 
		"      <cml:atom id=\"a2\" elementType=\"H\"\r\n" + 
		"            x2=\"-0.05500002861022946\" y2=\"1.1549999713897705\" />\r\n" + 
		"      <cml:atom id=\"a3\" elementType=\"H\"\r\n" + 
		"            x2=\"-2.3650000286102295\" y2=\"2.4886790932178062\" />\r\n" + 
		"    </cml:atomArray>\r\n" + 
		"    <cml:bondArray>\r\n" + 
		"      <cml:bond id=\"b1\" atomRefs2=\"a1 a2\" order=\"1\" />\r\n" + 
		"      <cml:bond id=\"b2\" atomRefs2=\"a1 a3\" order=\"1\" />\r\n" + 
		"    </cml:bondArray>\r\n" + 
		"  </cml:molecule>\r\n" + 
		"</cml:cml>";
	
		InputStream is = IOUtils.toInputStream(text, "UTF-8");
		Document doc = builder.build(is);		
				
		QNameValidator validator = new QNameValidator();
		boolean isValid = validator.validate(doc);
		assertFalse("should not be valid", isValid);
		
		Collection<URI> reachableUris = validator.getReachableUrisFromCurrentDocument();
		assertEquals("no entry should resolve", 0, reachableUris.size());
				
		Collection<URI> unreachableUris = validator.getUnreachableUrisFromCurrentDocument();
		assertEquals("there should be 2 unreachable uris",2, unreachableUris.size());
	}
	
	@Test
	public void testValidateWithReachableAndUnreachableUriFiles() throws ValidityException, ParsingException, IOException {
		String reachableUrisStorage = workspace.getAbsolutePath()+File.separatorChar+"reachable.xml";
		String unreachableUrisStorage = workspace.getAbsolutePath()+File.separatorChar+"unreachable.xml";

		QNameValidator validator = new QNameValidator(reachableUrisStorage, unreachableUrisStorage);
		boolean isValid = validator.validate(this.document);
		assertTrue("should be valid", isValid);
		assertTrue("only single valid uri", validator.getReachableUrisFromCurrentDocument().size() == 1);
		
		Collection<URI> reachableUris = (Set<URI>) xStream
		.fromXML(FileUtils.readFileToString(new File(reachableUrisStorage)));
		
		assertEquals("should only be one reachable uri", 1, reachableUris.size());
		Collection<URI> unreachableUris = (Set<URI>) xStream
		.fromXML(FileUtils.readFileToString(new File(unreachableUrisStorage)));
		assertTrue("should not be any unreachable uris", unreachableUris.isEmpty());
	}
	
	@Test
	public void testValidateWithReachableAndUnreachableUriFiles2() throws Exception {
		File reachableUrisStorage = setupReachableUrisStorage(workspace.getAbsolutePath()+File.separatorChar+"reachable.xml");
		
		String unreachableUrisStorage = workspace.getAbsolutePath()+File.separatorChar+"unreachable.xml";

		QNameValidator validator = new QNameValidator(reachableUrisStorage.getAbsolutePath(), unreachableUrisStorage);
		boolean isValid = validator.validate(this.document);
		assertTrue("should be valid", isValid);
		assertTrue("only single valid uri", validator.getReachableUrisFromCurrentDocument().size() == 1);
	
		Collection<URI> reachableUris = (Set<URI>) xStream
		.fromXML(FileUtils.readFileToString(reachableUrisStorage));
		
		assertEquals("should only be one reachable uri", 1, reachableUris.size());
		Collection<URI> unreachableUris = (Set<URI>) xStream
		.fromXML(FileUtils.readFileToString(new File(unreachableUrisStorage)));
		assertTrue("should not be any unreachable uris", unreachableUris.isEmpty());
	}
	
	@Test
	public void testValidateWithReachableAndUnreachableUriFiles3() throws Exception {
		File reachableUrisStorage = setupReachableUrisStorage(workspace.getAbsolutePath()+File.separatorChar+"reachable.xml");
		
		File unreachableUrisStorage = setupUnreachableUrisStorage(workspace.getAbsolutePath()+File.separatorChar+"unreachable.xml");

		QNameValidator validator = new QNameValidator(reachableUrisStorage.getAbsolutePath(), unreachableUrisStorage.getAbsolutePath());
		boolean isValid = validator.validate(this.document);
		assertTrue("should be valid", isValid);
		assertTrue("only single valid uri", validator.getReachableUrisFromCurrentDocument().size() == 1);
		
		Collection<URI> reachableUris = (Set<URI>) xStream
		.fromXML(FileUtils.readFileToString(reachableUrisStorage));
		
		assertEquals("should only be one reachable uri", 1, reachableUris.size());
		Collection<URI> unreachableUris = (Set<URI>) xStream
		.fromXML(FileUtils.readFileToString(unreachableUrisStorage));
		assertEquals("should be one unreachable uri", 1, unreachableUris.size());
	}
	
	@Test
	public void testValidateWithReachableAndUnreachableUriFiles4() throws Exception {
		File reachableUrisStorage = setupReachableUrisStorage(workspace.getAbsolutePath()+File.separatorChar+"reachable.xml");		
		File unreachableUrisStorage = setupUnreachableUrisStorage(workspace.getAbsolutePath()+File.separatorChar+"unreachable.xml");
		QNameValidator validator = new QNameValidator(reachableUrisStorage.getAbsolutePath(), unreachableUrisStorage.getAbsolutePath());
		
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
		Document doc = builder.build(is);

		validator.validate(doc);

		assertEquals("only single valid uri", 1, validator.getReachableUrisFromCurrentDocument().size());
		
		Collection<URI> reachableUris = (Set<URI>) xStream
		.fromXML(FileUtils.readFileToString(reachableUrisStorage));
		
		assertEquals("should only be one reachable uri", 1, reachableUris.size());
		Collection<URI> unreachableUris = (Set<URI>) xStream
		.fromXML(FileUtils.readFileToString(unreachableUrisStorage));
		assertEquals("should be three unreachable uris", 3, unreachableUris.size());
	}
	
}
