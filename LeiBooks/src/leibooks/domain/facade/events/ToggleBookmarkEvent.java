package leibooks.domain.facade.events;

import leibooks.domain.facade.IDocument;

public class ToggleBookmarkEvent extends ADocumentEvent{

	private final int pageNum;
	private final boolean isBookmarked;
	
	public ToggleBookmarkEvent(IDocument document, int pageNum, boolean isBookmarked) {
		super(document);
		this.pageNum = pageNum;
		this.isBookmarked = isBookmarked;
	}

	public int getPageNum() {
		return pageNum;
	}

	public boolean isBookmarked() {
		return isBookmarked;
	}
	
	@Override
	public String toString() {
		return "ToggleBookmarkEvent [document=" + getDocument().getFile() + 
				" pageNum=" + this.pageNum + 
				" isBookmarked=" + this.isBookmarked + "]";
	}
}
