package leibooks.domain.facade;

public class DocumentProperties {

	private String title;
	private String path;
	private String author;
	private String type;
	
	public DocumentProperties(IDocument document) {
		title = document.getTitle();
		path = document.getFile().getAbsolutePath();
		author = document.getAuthor();
		type = document.getMimeType();
	}
	
	public String title() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String path() {
		return path;
	}
	
	public String author() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String mimeType() {
		return type;
	}
}
