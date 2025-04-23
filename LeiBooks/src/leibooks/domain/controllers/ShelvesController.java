package leibooks.domain.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.IShelvesController;
import leibooks.domain.facade.events.LBEvent;
import leibooks.domain.facade.events.RemoveDocumentShelfEvent;
import leibooks.domain.facade.events.RemoveShelfEvent;
import leibooks.domain.facade.events.ShelfEvent;
import leibooks.domain.shelves.IShelves;
import leibooks.domain.shelves.IShelf;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;

/**
 * ShelvesController is a controller for managing shelves in the LeiBooks application.
 * It extends AbsSubject<LBEvent> so that it can emit LBEvents and implements IShelvesController.
 * The controller delegates shelf-related operations to an underlying IShelves instance.
 */
public class ShelvesController extends AbsSubject<LBEvent> implements IShelvesController {

    private final IShelves shelves;

    /**
     * Constructs a ShelvesController with the specified shelves collection.
     *
     * @param shelves the IShelves instance to control; must not be null.
     * @throws IllegalArgumentException if shelves is null.
     */
    public ShelvesController(IShelves shelves) {
        if (shelves == null) {
            throw new IllegalArgumentException("Shelves cannot be null");
        }
        this.shelves = shelves;

        //addSmartShelf("Recent", d -> d.getLastModifiedDate().isAfter(LocalDate.now().minusDays(7)));
    }

    /**
     * Emits a document event to registered observers.
     *
     * @param e the event to emit; must not be null.
     */
    @Override
    public void emitEvent(LBEvent e) {
        listeners.forEach(listener -> listener.processEvent(e));
    }

    /**
     * Registers a new observer to receive events.
     *
     * @param obs the observer to register; must not be null.
     */
    @Override
    public void registerListener(Listener<LBEvent> obs) {
        if (obs != null && !listeners.contains(obs)) {
            listeners.add(obs);
        }
    }

    /**
     * Unregisters a new observer to receive events.
     *
     * @param obs the observer to register; must not be null.
     */
    @Override
    public void unregisterListener(Listener<LBEvent> obs) {
        if (obs != null) {
            listeners.remove(obs);
        }
    }

    /**
     * Processes a ShelfEvent by propagating it to registered observers.
     *
     * @param e the shelf event to process.
     */
    @Override
    public void processEvent(ShelfEvent e) {
        emitEvent(e);
    }

    /**
     * Adds a normal shelf with the specified name.
     *
     * @param name the name of the shelf to add.
     * @return true if the shelf was added successfully, false otherwise.
     */
    @Override
    public boolean addNormalShelf(String name) {
        return shelves.addNormalShelf(name);
    }

    /**
     * Adds a smart shelf with the specified name and criteria.
     *
     * @param name the name of the shelf to add.
     * @param criteria the predicate determining which documents belong to the shelf.
     * @return true if the shelf was added successfully, false otherwise.
     */
    @Override
    public boolean addSmartShelf(String name, Predicate<IDocument> criteria) {
        return shelves.addSmartShelf(name, criteria);
    }

    /**
     * Returns an iterable collection of all shelf names currently registered.
     *
     * @return a list of shelf names in string format.
     */
    @Override
    public Iterable<String> getShelves() {
        List<String> shelfNames = new ArrayList<>();
        shelves.iterator().forEachRemaining(shelf -> shelfNames.add(shelf.getName()));
        return shelfNames;
    }

    /**
     * Removes the shelf with the specified name.
     *
     * @param name the name of the shelf to remove.
     * @throws OperationNotSupportedException if the shelf is unremovable.
     */
    @Override
    public void remove(String name) throws OperationNotSupportedException {
        if (name != null) {
            shelves.removeShelf(name);
            emitEvent(new RemoveShelfEvent(name));
        }
    }
    /**
     * Removes the specified document from the shelf identified by shelfName.
     *
     * @param shelfName the name of the shelf.
     * @param document the document to remove.
     * @throws OperationNotSupportedException if the shelf does not support document removal.
     */
    @Override
    public void removeDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
        if (shelfName != null && document != null) {
            shelves.removeDocument(shelfName, document);
            emitEvent(new RemoveDocumentShelfEvent(shelfName, document));
        }
    }

    /**
     * Adds the specified document to the shelf identified by shelfName.
     *
     * @param shelfName the name of the shelf.
     * @param document the document to add.
     * @return true if the document was added successfully, false otherwise.
     * @throws OperationNotSupportedException if the shelf does not support adding documents.
     */
    @Override
    public boolean addDocument(String shelfName, IDocument document) throws OperationNotSupportedException {
        return shelves.addDocument(shelfName, document);
    }

    /**
     * Returns an iterable collection of documents from the shelf with the specified name.
     *
     * @param shelfName the name of the shelf.
     * @return an iterable of IDocument objects from that shelf.
     */
    @Override
    public Iterable<IDocument> getDocuments(String shelfName) {
        return shelves.getDocuments(shelfName);
    }

    

    /**
     * Returns a string representation of the shelves.
     *
     * @return a string listing all shelf names and the files of the documents they contain.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Shelves=\n");
        for (IShelf s : shelves) {
            result.append(s.getName()).append(" = [");
            StringBuilder shelfInfo = new StringBuilder();
            for (IDocument d : s) {
                shelfInfo.append(d.getFile()).append(", ");
            }
            if (shelfInfo.length() > 0) {
                result.append(shelfInfo.substring(0, shelfInfo.length() - 2));
            }
            result.append("]\n");
        }
        return result.toString();
    }
}
