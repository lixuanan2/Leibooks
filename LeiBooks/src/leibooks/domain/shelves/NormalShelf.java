package leibooks.domain.shelves;

import java.util.Iterator;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;

/**
 * Represents a normal shelf where documents can be manually added or removed by the user.
 *
 * <p>A NormalShelf simply keeps a set of documents explicitly managed by the user.
 * If a document is removed from the library, it should no longer appear in this shelf,
 * which can be handled by listening for the document removal events and removing
 * from this shelf accordingly (if desired).</p>
 */
public class NormalShelf extends AShelf {

    /** A set of documents contained in this NormalShelf. */

    /**
     * Constructs a NormalShelf with the specified name.
     *
     * @param name the unique name of this shelf.
     * @throws IllegalArgumentException if name is null.
     */
    public NormalShelf(String name) {
        super(name);
    }

    /**
     * Adds a document to this normal shelf, if not already present.
     *
     * @param document the document to add.
     * @return true if the document was successfully added; false if it was already present.
     * @throws OperationNotSupportedException if the shelf does not allow manual addition.
     */
    @Override
    public boolean addDocument(IDocument document) {
        return documents.add(document);
    }

    /**
     * Removes a document from this normal shelf, if present.
     *
     * @param document the document to remove.
     * @return true if the document was successfully removed; false if it was not present.
     * @throws OperationNotSupportedException if the shelf does not allow manual removal.
     */
    @Override
    public boolean removeDocument(IDocument document) {
        return documents.remove(document);
    }

    /**
     * Returns an iterator over the documents in this normal shelf.
     *
     * @return an iterator over the documents stored in this shelf.
     */
    @Override
    public Iterator<IDocument> iterator() {
        return documents.iterator();
    }

    /**
     * Removes the document from this shelf if it was deleted from the library.
     *
     * @param e the event to process
     */
    @Override
    public void processEvent(DocumentEvent e) {
        IDocument doc = e.getDocument();
        if (e instanceof RemoveDocumentEvent) {
		    removeDocument(doc);
		}
    }
}
