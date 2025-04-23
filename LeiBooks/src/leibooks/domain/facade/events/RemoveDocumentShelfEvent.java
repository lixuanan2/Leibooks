package leibooks.domain.facade.events;

import leibooks.domain.facade.IDocument;

public class RemoveDocumentShelfEvent extends AShelfEvent{	
	private IDocument doc;
		
	public RemoveDocumentShelfEvent(String shelfName, IDocument doc) {
		super(shelfName);
		this.doc = doc;
	}
	@Override
	public String toString() {
		return "RemoveDocumentShelfEvent [document=" + this.doc.getFile() + 
				" shelf=" + this.getShelfName() + "]";
	}
}
