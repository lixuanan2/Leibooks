package leibooks.ui.swing;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import leibooks.domain.facade.IDocument;
import leibooks.services.viewer.IViewer;

/**
 * An extended label for showing documents
 * 
 * @author fmartins
 *
 */
public class DocumentLabel extends JLabel {
	
	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -7009999655067511686L;
	
	/**
	 * The document displayed by this label
	 */
	private IDocument document;
	
	private int slideDuration;
	
	private IViewer documentViewer;

	/**
	 * Constructs a document label given a image and a document
	 * @param image The image to be shown
	 * @param document The document shown
	 */
	public DocumentLabel(ImageIcon image, IDocument document, IViewer documentViewer) {
		super (image);
		this.document = document;
		this.documentViewer = documentViewer;
		this.setSlideDuration(5);
	}
	

	/**
	 * @return The document attached to this label
	 */
	public IDocument getDocument() {
		return document;
	}
	

	/**
	 * @param document Attaches a document to this label
	 */
	public void setDocument(IDocument document) {
		this.document = document;
	}


	public void setSlideDuration(int slideDuration) {
		this.slideDuration = slideDuration;
	}


	public int getSlideDuration() {
		return slideDuration;
	}
	
	public IViewer getDocumentViewer() {
		return documentViewer;
	}


	public void setDocumentViewer(IViewer documentViewer) {
		this.documentViewer = documentViewer;
	}
}
