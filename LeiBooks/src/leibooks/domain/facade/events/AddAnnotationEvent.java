package leibooks.domain.facade.events;

import leibooks.domain.facade.IDocument;

public class AddAnnotationEvent extends AAnnotationEvent {

	private final String annotationText;

	public AddAnnotationEvent(IDocument doc, int pageNum, int annotationNum, String annotationText, boolean hasAnnotations) {
		super (doc, pageNum, annotationNum, hasAnnotations);
		this.annotationText = annotationText;
	}
	
	public String getAnnotationText() {
		return annotationText;
	}

	@Override
	public String toString() {
		return "AddAnnotationEvent [document=" + getDocument().getFile() + 
				" pageNum=" + this.getPageNum() + 
				" annNum=" + this.getAnnotationNum() + 
				" annotationText=" + annotationText + "]";
	}

}
