package leibooks.ui.delegates;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.ui.swing.DocumentMetadataUI;
import leibooks.utils.Listener;

public abstract class DocumentMetadataUIDelegate implements Listener<DocumentEvent> {

	/**
	 * Reference to the UI's delegate
	 */
	private DocumentMetadataUI documentMetadataUI;
	
	
	/**
	 * Links the delegate back to its UI
	 * 
	 * @param documentMetadataUI The document viewer UI
	 */
	public void setDocumentetadataUI (DocumentMetadataUI documentMetadataUI) {
		this.documentMetadataUI = documentMetadataUI;
	}
	
	
	/**
	 * Adds a tree node to the UI for a bookname in the page number
	 * 
	 * @param pageNum The page number bookmark to be added as a tree node
	 */
	protected void addBookmarkTreeNode (int pageNum) {
		documentMetadataUI.addBookmarkTreeNode (pageNum);
	}
	
	
	/**
	 * Removes a tree node from the UI for a bookname in the page number
	 * 
	 * @param pageNum The page number bookmark to be removed from the tree
	 */
	protected void removeBookmarkTreeNode (int pageNum) {
		documentMetadataUI.removeBookmarkTreeNode (pageNum);
	}
	
	
	/**
	 * Adds a tree node to the UI for an annotation text 
	 * 
	 * @param text The annotation text to be added as a tree node
	 */
	protected void addAnnotationTreeNode (String text) {
		documentMetadataUI.addAnnotationTreeNode (text);
	}
	
	
	/**
	 * Removes the tree node from the UI corresponding the the annotation number 
	 * 
	 * @param text The annotation number to be removed from the tree 
	 */
	protected void removeAnnotationTreeNode (int annotationNum) {
		documentMetadataUI.removeAnnotationTreeNode (annotationNum);
	}

	
	/**
	 * Deletes registered observers
	 */
	public abstract void deleteObservers();
	
	
	/**
	 * Indicates the document that the UI delegate is working with
	 * 
	 * @param document The document this delegate is working with
	 */
	public abstract void setDocument (IDocument document);
	
	
	/**
	 * @return The list of document bookmarks
	 */
	public abstract Iterable<Integer> getDocumentBookmarks();
	
	
	/**
	 * Inquire the annotations for a page
	 * 
	 * @param pageNum The page to get the list of annotations
	 * @return The list of annotations of pageNum
	 */
	public abstract Iterable<String> getPageAnnotations(int pageNum);

	
	/**
	 * @return The document's title
	 */
	public abstract String getDocumentTitle();

	
	/**
	 * Adds an annotation to a given page number
	 * 
	 * @param pageNum The page number to add the annotation to
	 * @param text The text of the annotation
	 */
	public abstract void addAnnotation(int pageNum, String text);

	
	/**
	 * Removes an annotation from a page
	 * 
	 * @param pageNum The page to remove the annotation from
	 * @param annotNum The annotation id number to be removed
	 */
	public abstract void removeAnnotation(int pageNum, int annotNum);

	
	/**
	 * Toggles (set on if off, otherwise set off) the bookmark 
	 * of a given page
	 * 
	 * @param pageNum The page number to toggle the bookmark
	 */
	public abstract void toggleBookmark(int pageNum);

	
	/**
	 * Gets the annotation text of a given annotation for a given page
	 * 
	 * @param pageNum The page number to fetch the annotation
	 * @param annotNum The annotation number to be retrived
	 * @return The annotation text
	 */
	public abstract String getAnnotationText(int pageNum, int annotNum);
}
