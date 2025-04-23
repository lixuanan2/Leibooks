package leibooks.app.delegates;

import java.io.File;
import java.util.Optional;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.AnnotationEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.ToggleBookmarkEvent;
import leibooks.services.viewer.IViewer;
import leibooks.services.viewer.swing.SwingViewerFactory;
import leibooks.ui.delegates.DocumentUIDelegate;

/**
 * The document viewer ui delegate default implementation
 * 
 * @author fmartins
 *
 */
public class LEIBooksDocumentUIDelegate extends DocumentUIDelegate {
	
	private IDocument document;


	public LEIBooksDocumentUIDelegate () {
		
	}
	
	public void setDocument (IDocument document) {
		this.document = document;
	}
	
	@Override
	public void processEvent(DocumentEvent e) {
		if (e instanceof ToggleBookmarkEvent event) {
			updateBookmarkLabel(event.getPageNum(), event.isBookmarked());
		} else if (e instanceof AnnotationEvent event) {
			updateAnnotationsLabel (event.getPageNum(), event.hasAnnotations());
		} 
	}


	@Override
	public void setLastPageVisited(int pageNum) {
		document.setLastPageVisited(pageNum);
	}


	@Override
	public boolean isBookmarked(int pageNum) {
		return document.isBookmarked(pageNum);
	}


	@Override
	public int getLastPageVisited() {
		return document.getLastPageVisited();
	}


	@Override
	public File getDocumentFile() {
		return document.getFile();
	}


	@Override
	public boolean hasAnnotations(int pageNum) {
		return document.hasAnnotations(pageNum);
	}


	@Override
	public void toggleBookmark(int pageNum) {
		document.toggleBookmark(pageNum);
	}
	
	@Override
	public Optional<IViewer> getViewer(String documentMime, String widgetToolkit) {
		return SwingViewerFactory.INSTANCE.getViewer(documentMime, widgetToolkit);
	}

	@Override
	public IDocument getDocument() {
		return document;
	}

	@Override
	public void setListeners() {
		document.registerListener(this);
	}

	@Override
	public void deleteListeners() {
		document.unregisterListener(this);
	}

	@Override
	public String getDocumentType() {
		return document.getMimeType();
	}


}
