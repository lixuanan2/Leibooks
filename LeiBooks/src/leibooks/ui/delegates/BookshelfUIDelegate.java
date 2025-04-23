package leibooks.ui.delegates;

import javax.naming.OperationNotSupportedException;

import leibooks.domain.facade.DocumentProperties;
import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.LBEvent;
import leibooks.ui.swing.BookshelfUI;
import leibooks.utils.Listener;

/**
 * Delegate that mediates the interaction between ui and domain
 * 
 * @author fmartins
 *
 */
public abstract class BookshelfUIDelegate implements Listener<LBEvent> {
	
	/**
	 * Reference to the UI's delegates
	 */
	private BookshelfUI bookshelf;
	
	
	/**
	 * Links the delegate back to its UI
	 * 
	 * @param bookshelf The bookshelf UI
	 */
	public void setBookshelfUI (BookshelfUI bookshelf) {
		this.bookshelf = bookshelf;
	}
	
	
	/**
	 * Adds a tree node to the UI for a shelf with name shelfName
	 * 
	 * @param shelfName The name of the shelf to be added as a tree node
	 */
	protected void addShelfTreeNode (String shelfName) {
		bookshelf.addShelfTreeNode(shelfName);
	}
	
	
	/**
	 * Removes the selected tree node shelf from the tree of shelves
	 */
	protected void removeSelectedShelfTreeNode () {
		bookshelf.removeSelectedShelfTreeNode();
	}
	
	
	/**
	 * Adds a thumbnail representing the document to the document's panel
	 * 
	 * @param document The document whose thumbnail will be added to the panel
	 */
	protected void addToDocumentsPanel (IDocument document) {
		bookshelf.addToDocumentsPanel(document);
	}
	
	
	/**
	 * Removes the selected thumbnail from the document's panel
	 */
	protected void removeSelectedDocumentFromPanel () {
		bookshelf.removeSelectedDocumentFromPanel (); 
	}
	
	
	/**
	 * Gets shelf's list of documents
	 * 
	 * @param shelfName The name of the shelf to get the documents from
	 * @return An iterable with the shelf's documents.
	 */
	public abstract Iterable<IDocument> getShelfDocuments(String shelfName);

	
	/**
	 * Gets library's list of documents
	 * 
	 * @return An iterable with the library's documents.
	 */
	public abstract Iterable<IDocument> getLibraryDocuments();

	
	/**
	 * Creates a normal shelf
	 * 
	 * @param shelfName The name of the shelf to be added
	 * @return if the shelf was added 
	 */
	public abstract boolean addNormalShelf(String shelfName);

	
	/**
	 * Removes a shelf
	 * 
	 * @param string The name of the shelf to be removed
	 * @throws OperationNotSupportedException 
	 */
	public abstract void removeShelf(String shelfName) throws OperationNotSupportedException;

	
	/**
	 * Updates document's properties
	 * 
	 * @param document The document to be updated
	 * @param documentProperties The new document properties
	 */
	public abstract void updateDocument(IDocument document,
			DocumentProperties documentProperties);

	
	/**
	 * Removes a document from the library
	 * 
	 * @param document The document to be removed from the library
	 */
	public abstract void removeLibraryDocument(IDocument document);

	
	/**
	 * Removes a document from a shelf
	 * 
	 * @param shelfName The name of the shelf to remove the document from
	 * @param document The document to be removed
	 * @throws OperationNotSupportedException Thrown in case of a special shelf
	 */
	public abstract void removeDocumentShelf(String shelfName, IDocument document) throws OperationNotSupportedException;

	
	/**
	 * Adds a document to the library given its properties
	 * 
	 * @param title	The document's title
	 * @param path The document's pat
	 * @return if the document was added
	 */
	public abstract  boolean addDocumentLibrary(String title, String path);

	
	/**
	 * @return The existent shelves
	 */
	public abstract Iterable<String> getShelves();

	
	/**
	 * Adds an existent document  that is already in the library) to a shelf
	 * 
	 * @param shelfName The shelf's name
	 * @param document The document to be added
	 * @return if the document was added to the shelf
	 * @throws OperationNotSupportedException Thrown in case of a special shelf
	 */
	public abstract boolean addDocumentShelf(String shelfName, IDocument document) throws OperationNotSupportedException;

	
	/**
	 * Gets the document's title
	 * 
	 * @param document The document to query the title
	 * @return the title of the document
	 */
	public abstract String getDocumentTitle(IDocument document);

	/**
	 * Retrieves a list of documents that match the given regular expression.
	 *
	 * @param regex the regular expression to match documents against.
	 * @return An iterable collection of IDocument representing the matching documents.
	 */
	public abstract Iterable<IDocument> searchDocuments(String query);
}
