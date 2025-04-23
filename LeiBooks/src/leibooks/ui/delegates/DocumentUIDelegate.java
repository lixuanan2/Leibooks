package leibooks.ui.delegates;

import java.io.File;
import java.util.Optional;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.services.viewer.IViewer;
import leibooks.ui.swing.DocumentUI;
import leibooks.utils.Listener;

/**
 * Delegate that mediates the interaction between a document viewer and the domain
 * 
 * @author fmartins
 *
 */
public abstract class DocumentUIDelegate implements Listener<DocumentEvent> {

	/**
	 * Reference to the UI's delegate
	 */
	private DocumentUI documentViewer;
	
	
	/**
	 * Links the delegate back to its UI
	 * 
	 * @param documentViewer The document viewer UI
	 */
	public void setDocumentViewerUI (DocumentUI documentViewer) {
		this.documentViewer = documentViewer;
	}
	
	
	/**
	 * Updates the bookmark ui for a given page
	 * 
	 * @param pageNum The page number for updating the bookmark
	 * @param active if the bookmark is active
	 */
	protected void updateBookmarkLabel (int pageNum, boolean active) {
		documentViewer.updateBookmarkLabel (pageNum, active);
	}
	
	
	/**
	 * Updates the annotations ui for a given page
	 * 
	 * @param pageNum The page number for updating the annotations ui
	 * @param hasAnnotations if the page has annotions
	 */
	protected void updateAnnotationsLabel(int pageNum, boolean hasAnnotations) {
		documentViewer.updatePageActionLabel (pageNum, hasAnnotations);
	}
	
	
	/**
	 * Deletes registered observers
	 */
	public abstract void deleteListeners();
	
	
	/**
	 * Indicates the document that the UI delegate is working with
	 * 
	 * @param document The document this delegate is working with
	 */
	public abstract void setDocument (IDocument document);

	
	/**
	 * Sets the last page visited before document viewer closes
	 * 
	 * @param pageNum The last page visited
	 */
	public abstract void setLastPageVisited(int pageNum);


	public abstract int getLastPageVisited();

	/**
	 * Inquires if a given page number is bookmarked
	 * 
	 * @param pageNum The page to inquire
	 * @return if the page is bookmarked
	 */
	public abstract boolean isBookmarked(int pageNum);

	
	/**
	 * Inquires if a given page has annotations
	 * 
	 * @param pageNum The page to inquire 
	 * @return if the page has annotations
	 */
	public abstract boolean hasAnnotations(int pageNum);

	
	/**
	 * @return A File object representing the path of the 
	 * document being viewer 
	 */
	public abstract File getDocumentFile();

	
	/**
	 * @return The document being shown by the UI associated
	 * with the delegate
	 */
	public abstract IDocument getDocument();

	
	/**
	 * Toggles (set on if off, otherwise set off) the bookmark 
	 * of a given page
	 * 
	 * @param pageNum The page number to toggle the bookmark
	 */
	public abstract void toggleBookmark(int pageNum);
	
	
	/**
	 * Gets a viewer for this type of document, for a specific
	 * toolkit (in this case, swing)
	 * 
	 * @param documentMime The mime type of the document
	 * @param widgetToolkit The UI toolkit 
	 * @return a viewer that matches both requirements or null otherwise.
	 */
	public abstract Optional<IViewer> getViewer(String documentMime, String widgetToolkit);

	
	/**
	 * Establish the convenient listeners
	 */
	public abstract void setListeners();

	
	/**
	 * @return The document's type
	 */
	public abstract String getDocumentType();
}
