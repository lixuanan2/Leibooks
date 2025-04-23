package leibooks.domain.shelves;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.function.Predicate;


import javax.naming.OperationNotSupportedException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import leibooks.domain.core.ILibrary;
import leibooks.domain.core.Library;
import leibooks.domain.core.MockDocument;
import leibooks.domain.facade.IDocument;

class ShelvesTest {

    private ILibrary library;
    private Shelves shelves;

    @BeforeEach
    void setUp() {
        library = new Library();
        shelves = new Shelves(library);
    }

    @Test
    void testAddNormalShelf() {
        boolean added = shelves.addNormalShelf("Shelf1");
        assertTrue(added, "Should add a new normal shelf");

        boolean addedAgain = shelves.addNormalShelf("Shelf1");
        assertFalse(addedAgain, "Duplicate shelf addition should fail");

        boolean found = false;
        Iterator<IShelf> it = shelves.iterator();
        while (it.hasNext()) {
            if ("Shelf1".equals(it.next().getName())) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Shelf 'Shelf1' should exist in shelves");
    }

    @Test
    void testAddSmartShelf() {
        Predicate<IDocument> criteria = doc -> doc.getTitle().contains("Test");
        boolean added = shelves.addSmartShelf("SmartShelf1", criteria);
        assertTrue(added, "Should add a new smart shelf");

        boolean addedAgain = shelves.addSmartShelf("SmartShelf1", criteria);
        assertFalse(addedAgain, "Duplicate smart shelf addition should fail");

        boolean found = false;
        for (IShelf s : shelves) {
            if ("SmartShelf1".equals(s.getName())) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Smart shelf 'SmartShelf1' should exist in shelves");
    }

    @Test
    void testRemoveShelf() throws OperationNotSupportedException {
        shelves.addNormalShelf("Shelf1");
        shelves.addSmartShelf("SmartShelf1", doc -> true);

        shelves.removeShelf("Shelf1");
        for (IShelf s : shelves) {
            assertNotEquals("Shelf1", s.getName(), "Shelf 'Shelf1' should have been removed");
        }

        assertDoesNotThrow(() -> shelves.removeShelf("NonExisting"));
    }

    @Test
    void testRemoveShelfUnremovable() throws OperationNotSupportedException {
        boolean added = shelves.addUnremovableSmartShelf("Unremovable", doc -> true);
        assertTrue(added, "Should successfully add an unremovable shelf");

        OperationNotSupportedException exception = assertThrows(OperationNotSupportedException.class, () -> {
            shelves.removeShelf("Unremovable");
        });
        assertEquals("This shelf cannot be removed.", exception.getMessage());
    }


    @Test
    void testAddAndRemoveDocumentFromShelf() throws OperationNotSupportedException {
        shelves.addNormalShelf("Shelf1");
        IDocument doc = new MockDocument("Test.pdf");

        boolean added = shelves.addDocument("Shelf1", doc);
        assertTrue(added, "Document should be added successfully");

        Iterable<IDocument> docs = shelves.getDocuments("Shelf1");
        boolean found = false;
        for (IDocument d : docs) {
            if (d.equals(doc)) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Document should be found in shelf 'Shelf1'");

        shelves.removeDocument("Shelf1", doc);
        docs = shelves.getDocuments("Shelf1");
        for (IDocument d : docs) {
            assertNotEquals(doc, d, "Document should have been removed from shelf 'Shelf1'");
        }
    }

    @Test
    void testGetDocumentsNonExistingShelf() {
        assertNull(shelves.getDocuments("NonExisting"), "Non-existing shelf should return null");
    }
    
    @Test
    void testAddUnremovableSmartShelfTwiceFails() {
        Predicate<IDocument> predicate = doc -> true;
        assertTrue(shelves.addUnremovableSmartShelf("Always", predicate));
        assertFalse(shelves.addUnremovableSmartShelf("Always", predicate));
    }
    
    @Test
    void testAddDocumentToSmartShelfThrows() {
        Predicate<IDocument> predicate = doc -> true;
        shelves.addSmartShelf("Smart", predicate);
        IDocument doc = new MockDocument("abc.pdf");

        assertThrows(OperationNotSupportedException.class, () -> shelves.addDocument("Smart", doc));
    }


}
