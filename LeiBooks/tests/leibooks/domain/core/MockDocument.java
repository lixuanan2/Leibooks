package leibooks.domain.core;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.utils.Listener;


/**
 * This class is a limited Mock for type IDocument (annotations and bookmarks are not covered). 
 * It allow us to write unit tests, for any class C that depends on the type IDocument,
 * that are independent of any implementation of type C. 
 * This is a way of testing C isolated from the bugs that might exist in class Document. 
 * For instance, we can  use this class to create a mock document like this:
 * 
 * 		MockDocument document = new MockDocument("Test Doc",date,"TestFile.pdf","","",
 * 									Optional.empty(),0,true);

 * There are frameworks that support the creation of mocks, for instance, Mockito.
 * Using Mockito framework, we would write instead:
 *  		
 * 		IDocument mockDoc = mock(IDocument.class);
 *		when(mockDoc.getTitle()).thenReturn("Test Doc");
 * 		when(mockDoc.getLastModifiedDate()).thenReturn(date);
 * 		when(mockDoc.getFile()).thenReturn(new File("TestFile.pdf");
 * 		when(mockDoc.getMimeType()).thenReturn("");
 * 		when(mockDoc.getAuthor()).thenReturn("");
 * 		when(mockDoc.getAuthor()).thenReturn(Optional.empty());
 * 		when(mockDoc.getLastVisitedPage()).thenReturn(0);
 *      when(mockDoc.matches(anyString())).thenReturn(true);
 * 
 * @author malopes DCO2425
 *
 */
public record MockDocument(
		String title,
		LocalDate modifiedDate,
		String pathTofile,
		String mimeType,
		String author,
		Optional<Integer> numberOfPages,
		int lastPageVisited,
		boolean matches
		) implements IDocument{

	public MockDocument(String pathTofile) {
		this("", LocalDate.of(2025, 1, 1), pathTofile, "", "", Optional.empty(), 0, true);
	}

	public MockDocument(String pathTofile, boolean matches) {
		this("", LocalDate.of(2025, 1, 1), pathTofile, "", "", Optional.empty(), 0, matches);
	}


	@Override
	public void emitEvent(DocumentEvent e) {		
		// mock is immutable		
	}

	@Override
	public void registerListener(Listener<DocumentEvent> obs) {
		// mock is immutable		
	}

	@Override
	public void unregisterListener(Listener<DocumentEvent> obs) {		
		// mock is immutable		
	}

	@Override
	public File getFile() {
		return new File(pathTofile);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getAuthor() {
		return author;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public LocalDate getLastModifiedDate() {
		return modifiedDate;
	}

	@Override
	public Optional<Integer> getNumberOfPages() {
		return Optional.empty();
	}

	@Override
	public int getLastPageVisited() {
		return lastPageVisited;
	}

	@Override
	public List<Integer> getBookmarks() {
		return new ArrayList<>();
	}

	@Override
	public void toggleBookmark(int pageNum) {
		// mock is immutable

	}

	@Override
	public void setTitle(String title) {
		// mock is immutable		
	}

	@Override
	public void setAuthor(String author) {
		// mock is immutable		
	}

	@Override
	public void setLastPageVisited(int lastPageVisited) {
		// mock is immutable		
	}

	@Override
	public void addAnnotation(int pageNum, String text) {
		// mock is immutable		
	}

	@Override
	public void removeAnnotation(int pageNum, int annotNum) {
		// mock is immutable	
	}

	@Override
	public int numberOfAnnotations(int pageNum) {
		return 0;
	}

	@Override
	public Iterable<String> getAnnotations(int pageNum) {
		return new ArrayList<>();
	}

	@Override
	public String getAnnotationText(int pageNum, int annotNum) {
		return null;
	}

	@Override
	public boolean hasAnnotations(int pageNum) {
		return false;
	}

	@Override
	public boolean isBookmarked(int pageNum) {
		return false;
	}

	@Override
	public boolean isBookmarked() {
		return false;
	}
	
	@Override
	public boolean matches(String regexp) {
		return matches;
	}
	
	@Override
	public int compareTo(IDocument other) {
		return new File(pathTofile).compareTo(other.getFile());
	}

}
