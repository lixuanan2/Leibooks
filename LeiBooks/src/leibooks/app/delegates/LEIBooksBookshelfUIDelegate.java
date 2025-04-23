package leibooks.app.delegates;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.ILibraryController;
import leibooks.domain.facade.IShelvesController;
import leibooks.domain.facade.events.AddDocumentEvent;
import leibooks.domain.facade.events.AddShelfEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.LBEvent;
import leibooks.domain.facade.events.RemoveDocumentEvent;
import leibooks.domain.facade.events.RemoveDocumentShelfEvent;
import leibooks.domain.facade.events.RemoveShelfEvent;
import leibooks.domain.facade.events.ShelfEvent;
import leibooks.ui.delegates.BookshelfUIDelegate;

/**
 * The bookshelf ui delegate default implementation
 * 
 * @author fmartins
 *
 */
public class LEIBooksBookshelfUIDelegate extends BookshelfUIDelegate {

	private IShelvesController shelvesController;
	private ILibraryController libraryController;
	
	public LEIBooksBookshelfUIDelegate(IShelvesController shelvesHandler,
			ILibraryController libraryHandler) {
		this.shelvesController = shelvesHandler;
		this.libraryController = libraryHandler;
		libraryHandler.registerListener(this); 
		shelvesHandler.registerListener(this);
	}

	@Override
	public Iterable<IDocument> getShelfDocuments(String selectedShelf) {
		return shelvesController.getDocuments(selectedShelf);
	}

	@Override
	public Iterable<IDocument> getLibraryDocuments() {
		return libraryController.getDocuments();
	}

	@Override
	public boolean addNormalShelf(String shelfName) {
		return shelvesController.addNormalShelf(shelfName);
	}

	@Override
	public void removeShelf(String shelfName) throws OperationNotSupportedException {
		shelvesController.remove (shelfName);
	}

	@Override
	public void updateDocument(IDocument document,
			DocumentProperties documentProperties) {
		libraryController.updateDocument (document, documentProperties);
	}

	@Override
	public void removeLibraryDocument(IDocument document) {
			libraryController.removeDocument(document);
	}
	
	@Override
	public void removeDocumentShelf(String shelfName, IDocument document) throws OperationNotSupportedException {
		shelvesController.removeDocument(shelfName, document);
	}
	
	@Override
	public boolean addDocumentLibrary(String title, String path) {
		return libraryController.importDocument(title, path).isPresent();
	}

	@Override
	public Iterable<String> getShelves() {
		return shelvesController.getShelves();
	}

	@Override
	public boolean addDocumentShelf(String shelfName, IDocument document) throws OperationNotSupportedException {
		return shelvesController.addDocument(shelfName, document);
	}
		
	@Override
	public void processEvent(LBEvent event) {
		if (event instanceof ShelfEvent se)
			handleShelfEvents (se);
		else if (event instanceof DocumentEvent de)
			handleDocumentEvents (de);
	}
	
	private void handleDocumentEvents (DocumentEvent event) {
		if (event instanceof RemoveDocumentEvent) 
			removeSelectedDocumentFromPanel ();
		else if (event instanceof AddDocumentEvent) 
			addToDocumentsPanel(event.getDocument());
	}

	private void handleShelfEvents (ShelfEvent event) {
		if (event instanceof RemoveShelfEvent)
			removeSelectedShelfTreeNode();
		else if (event instanceof AddShelfEvent) 
			addShelfTreeNode(event.getShelfName());
		else if (event instanceof RemoveDocumentShelfEvent)
			removeSelectedDocumentFromPanel ();
	}

	@Override
	public String getDocumentTitle(IDocument d) {
		return d.getTitle();
	}

	@Override
	public Iterable<IDocument> searchDocuments(String query) {
		return libraryController.getMatches(query);
	}
}
