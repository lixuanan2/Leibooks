package leibooks.domain.controllers;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import leibooks.domain.core.ILibrary;
import leibooks.domain.core.Library;
import leibooks.domain.core.DocumentFactory;
import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.ILibraryController;
import leibooks.domain.facade.events.AddDocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.LBEvent;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;

/**
 * LibraryController is a controller for managing a library.
 * It propagates events concerning documents in the library to its observers.
 *
 * <p>This class extends {@link AbsSubject} for {@link LBEvent} and implements
 * {@link ILibraryController}.</p>
 */
public class LibraryController extends AbsSubject<LBEvent> implements ILibraryController {

    private final ILibrary library;
    
    /**
     * Constructs a LibraryController for the given library.
     *
     * @param library the library to be controlled; must not be null.
     * @throws IllegalArgumentException if library is null.
     */
    public LibraryController(Library library) {
        if(library == null) {
            throw new IllegalArgumentException("Library cannot be null");
        }
        this.library = library;
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
     * Processes a DocumentEvent received from the library.
     * Propagates the event to registered observers.
     *
     * @param e the document event to process.
     */
    @Override
    public void processEvent(DocumentEvent e) {
        // Propagate the document event as an LBEvent.
        emitEvent(e);
    }
    
    /**
     * Returns all documents in the library.
     *
     * @return an Iterable of IDocument.
     */
    @Override
    public Iterable<IDocument> getDocuments() {
        return library;
    }

    /**
     * Attempts to import a document.
     *
     * @param title the document's title; must not be null.
     * @param pathTofile the file system path; must not be null.
     * @return Optional containing the document if successful; empty if not.
     * @ensures If the file is missing or unreadable, returns Optional.empty().
     */
    @Override
    public Optional<IDocument> importDocument(String title, String pathTofile) {
        if (title == null || pathTofile == null) {
            return Optional.empty();
        }
        try {
            Path path = Path.of(pathTofile);
            if (!Files.exists(path) || !Files.isReadable(path)) {
                System.out.println("-------->> File " + pathTofile.replace("\\", "/") + " not found or could not be open");
                return Optional.empty();
            }
            IDocument document = DocumentFactory.INSTANCE.createDocument(title, pathTofile);
            emitEvent(new AddDocumentEvent(document));
            library.addDocument(document);
            return Optional.of(document);
        } catch (Exception ex) {
            System.out.println("-------->> File " + pathTofile.replace("\\", "/") + " not found or could not be open");
            return Optional.empty();
        }
    }
    
    /**
     * Removes the specified document from the library.
     *
     * @param document the document to remove.
     */
    @Override
    public void removeDocument(IDocument document) {
        library.removeDocument(document);
        emitEvent(new RemoveDocumentEvent(document));
    }
    
    /**
     * Updates the properties of the specified document in the library.
     *
     * @param document the document to update.
     * @param documentProperties the new properties to apply.
     */
    @Override
    public void updateDocument(IDocument document, DocumentProperties documentProperties) {
        library.updateDocument(document, documentProperties);
    }
    
    /**
     * Retrieves a list of documents from the library that match the given regular expression.
     *
     * @param regex the regular expression to match documents against.
     * @return a List of matching documents.
     */
    @Override
    public List<IDocument> getMatches(String regex) {
        return library.getMatches(regex);
    }
    
    /**
     * Returns a string representation of the library by iterating over its documents.
     *
     * @return a string listing all documents in the library.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Library = \n");
        for (IDocument doc : this.getDocuments()) {
            result.append(doc.toString()).append("\n");
        }
        return result.toString();
    }
}
