package leibooks.domain.facade.events;

import leibooks.domain.facade.IDocument;

public abstract class AAnnotationEvent extends ADocumentEvent implements AnnotationEvent{
	
	private final int pageNum;
	private final int annotationNum;
	private final boolean hasAnnotations;
	
	protected AAnnotationEvent(IDocument doc, int pageNum, int annotationNum, boolean hasAnnotations) {
		super(doc);
		this.pageNum = pageNum;
		this.annotationNum = annotationNum;
		this.hasAnnotations = hasAnnotations;
	}

	@Override
	public int getPageNum() {
		return pageNum;
	}

	@Override
	public int getAnnotationNum() {
		return annotationNum;
	}

	@Override
	public boolean hasAnnotations() {
		return hasAnnotations;
	}

}
