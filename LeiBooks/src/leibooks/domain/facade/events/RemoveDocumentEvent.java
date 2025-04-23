package leibooks.domain.facade.events;

import leibooks.domain.facade.IDocument;

public class RemoveDocumentEvent extends ADocumentEvent {

	public RemoveDocumentEvent(IDocument document) {
		super(document);
	}
	
	@Override
	public String toString() {
		return "RemoveDocumentEvent [document="+ getDocument().getFile() + "]";
	}
}
