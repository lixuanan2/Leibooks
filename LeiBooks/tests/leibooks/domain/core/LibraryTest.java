package leibooks.domain.core;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;

class LibraryTest {

	private Library library;

	@BeforeEach
	void setUp() {
		library = new Library();
	}

	@Test
	void testAddNewDocument() {
		MockDocument doc = new MockDocument("Test.jpg");
		assertTrue(library.addDocument(doc));
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertTrue(s.anyMatch(d -> d.equals(doc)));
		assertEquals(1, library.getNumberOfDocuments());
	}

	@Test
	void testAddDuplicatedDocument() {
		MockDocument doc = new MockDocument("Test.jpg");

		assertTrue(library.addDocument(doc));
		assertFalse(library.addDocument(doc));
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertTrue(s.anyMatch(d -> d.equals(doc)));
		assertEquals(1, library.getNumberOfDocuments());
	}


	@Test
	void testDeleteExistingDocument() {
		MockDocument doc = new MockDocument("Test.jpg");
		library.addDocument(doc);

		library.removeDocument(doc);
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertFalse(s.anyMatch(d -> d.equals(doc)));
		assertEquals(0, library.getNumberOfDocuments());
	}

	@Test
	void testDeleteNotExistingDocument() {
		MockDocument doc1 = new MockDocument("One.jpg");
		MockDocument doc2 = new MockDocument("Two.jpg");

		library.addDocument(doc1);
		library.removeDocument(doc2);
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertFalse(s.anyMatch(d -> d.equals(doc2)));
		assertEquals(1, library.getNumberOfDocuments());
	}


	@Test
	void testUpdateDocument() {
		MockDocument doc = new MockDocument("Test.jpg");
		DocumentProperties props = new DocumentProperties(doc);
		props.setTitle("New Title");
		props.setAuthor("New Author");
		
		library.addDocument(doc);
		library.updateDocument(doc, props);
		Stream<IDocument> s = StreamSupport.stream(library.spliterator(), false);
		assertTrue(s.anyMatch(d -> d.equals(doc)));
		//since we are using mocks that are imutable, the effect on properties is not visible
		assertEquals(1, library.getNumberOfDocuments());
	}

	@Test
	void testGetMatchesEmpty() {
		Collection<IDocument> matches = library.getMatches(".*");
		assertNotNull(matches);
		assertTrue(matches.isEmpty());
	}

	@Test
	void testGetMatchesNotEmpty() {
		MockDocument docY = new MockDocument("Y.jpg",true);
		MockDocument docN = new MockDocument("N.jpg",false);

		library.addDocument(docY);
		library.addDocument(docN);
		Collection<IDocument> matches = library.getMatches(".*");

		assertNotNull(matches);
		assertEquals(1, matches.size());
	}
	
	@Test
	void testAddNullDocumentDoesNotCrash() {
	    assertFalse(library.addDocument(null));
	    assertEquals(0, library.getNumberOfDocuments());
	}
	
	@Test
	void testUpdateMockDocumentNoEffect() {
	    MockDocument doc = new MockDocument("abc.pdf");
	    DocumentProperties props = new DocumentProperties(doc);
	    props.setTitle("Changed");

	    library.addDocument(doc);
	    library.updateDocument(doc, props);
	    assertEquals("abc.pdf", doc.getFile().getName()); // unchanged
	}



}