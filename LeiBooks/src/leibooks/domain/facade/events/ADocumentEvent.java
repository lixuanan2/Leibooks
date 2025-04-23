package leibooks.domain.facade.events;

import leibooks.domain.facade.IDocument;

public abstract class ADocumentEvent implements DocumentEvent{
	private final IDocument document;
	
	protected ADocumentEvent (IDocument document) {
		this.document = document;
	}
	
	public IDocument getDocument() {
		return document;
	}
	
}
