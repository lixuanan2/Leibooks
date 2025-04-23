package leibooks.domain.facade.events;

import leibooks.domain.facade.IDocument;

public class AddDocumentEvent extends ADocumentEvent {

	public AddDocumentEvent(IDocument document) {
		super(document);
	}

	@Override
	public String toString() {
		return "AddDocumentEvent [document="+ getDocument().getFile() + "]";
	}

}
