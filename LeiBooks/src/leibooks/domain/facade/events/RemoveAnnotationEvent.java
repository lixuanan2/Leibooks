package leibooks.domain.facade.events;

import leibooks.domain.facade.IDocument;

public class RemoveAnnotationEvent extends AAnnotationEvent {

	public RemoveAnnotationEvent(IDocument doc, int pageNum, int annotationNum, boolean hasAnnotations) {
		super (doc, pageNum, annotationNum, hasAnnotations); 	
	}
	
	@Override
	public String toString() {
		return "RemoveAnnotationEvent [document=" + getDocument().getFile() + 
				" pageNum=" + this.getPageNum() + 
				" annNum=" + this.getAnnotationNum() + 
				" hasAnnotations=" + this.hasAnnotations() + "]";
	}
}
