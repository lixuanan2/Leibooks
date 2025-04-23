package leibooks.app.delegates;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.AddAnnotationEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveAnnotationEvent;
import leibooks.domain.facade.events.ToggleBookmarkEvent;
import leibooks.ui.delegates.DocumentMetadataUIDelegate;

/**
 * The document's metadata ui delegate default implementation
 * 
 * @author fmartins
 *
 */
public class LEIBooksDocumentMetadataUIDelegate extends DocumentMetadataUIDelegate {
	
	private IDocument document;

	public LEIBooksDocumentMetadataUIDelegate () {
		
	}
	
	public void setDocument (IDocument doc) {
		if (document != null)
			document.unregisterListener(this);
		document = doc;
		if (document != null)
			document.registerListener(this);
	}
	
	@Override
	public void processEvent(DocumentEvent event) {
		if (event instanceof ToggleBookmarkEvent e) {
			if (e.isBookmarked()) 
				addBookmarkTreeNode(e.getPageNum());
			else 
				removeBookmarkTreeNode(e.getPageNum());
		} 
		else if (event instanceof AddAnnotationEvent e) 
			addAnnotationTreeNode(e.getAnnotationText());
		else if (event instanceof RemoveAnnotationEvent e) 
			removeAnnotationTreeNode(e.getAnnotationNum());
	}

	@Override
	public void deleteObservers() {
		document.unregisterListener(this);
	}

	@Override
	public Iterable<Integer> getDocumentBookmarks() {
		return document.getBookmarks();
	}

	@Override
	public Iterable<String> getPageAnnotations(int pageNum) {
		return document.getAnnotations(pageNum);
	}

	@Override
	public String getDocumentTitle() {
		return document.getTitle();
	}

	@Override
	public void addAnnotation(int pageNum, String text) {
		document.addAnnotation(pageNum, text);
	}

	@Override
	public void removeAnnotation(int pageNum, int annotNum) {
		document.removeAnnotation(pageNum, annotNum);
	}

	@Override
	public void toggleBookmark(int pageNum) {
		document.toggleBookmark(pageNum);
	}

	@Override
	public String getAnnotationText(int pageNum, int annotNum) {
		return document.getAnnotationText(pageNum, annotNum);
	}
}
