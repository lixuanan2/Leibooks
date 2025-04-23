package leibooks.domain.core;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Objects;


import leibooks.domain.facade.IDocument;
import leibooks.domain.facade.events.AddAnnotationEvent;
import leibooks.domain.facade.events.DocumentEvent;
import leibooks.domain.facade.events.RemoveAnnotationEvent;
import leibooks.domain.facade.events.ToggleBookmarkEvent;
import leibooks.utils.AbsSubject;
import leibooks.utils.Listener;
import leibooks.utils.RegExpMatchable;

public class Document extends AbsSubject<DocumentEvent>
					  implements IDocument, RegExpMatchable {

	// atributos
	private String title;
	private String author;
	private LocalDate lastModifiedDate;
	private String mimeType;
	private String pathToFile;
	private Optional<Integer> numberOfPages;

	// The last page number the user visited
	private int lastPageVisited;

	// Store pages by their page number. and the TreeMap keeps them in ascending order.
	private final Map<Integer, Page> pages = new TreeMap<>();

	/**
	 * Constructor
	 *
	 * @param expectedTitle        the document title
	 * @param expectedAuthor       the document author
	 * @param expectedModifiedDate the date this document was last modified
	 * @param expectedMimeType     the MIME type of the document (e.g. "application/pdf")
	 * @param expectedPath         the file system path to the document
	 * @param expectedNumPages     an optional number of total pages
	 * @ensures expectedPath != null
	 */
	public Document(String expectedTitle,
					String expectedAuthor,
					LocalDate expectedModifiedDate,
					String expectedMimeType,
					String expectedPath,
					Optional<Integer> expectedNumPages) {

		this.title = expectedTitle;
		this.author = expectedAuthor;
		this.lastModifiedDate = expectedModifiedDate;
		this.mimeType = expectedMimeType;
		this.pathToFile = expectedPath;
		this.numberOfPages = expectedNumPages;
	}

	/**
	 * Emits a DocumentEvent to all registered listeners.
	 *
	 * @param e the event to emit
	 */
	@Override
	public void emitEvent(DocumentEvent e) {
		super.emitEvent(e);
	}

	/**
	 * Registers a listener for DocumentEvent notifications.
	 *
	 * @param obs the listener to register
	 */
	@Override
	public void registerListener(Listener<DocumentEvent> obs) {
		super.registerListener(obs);
		
	}

	/**
	 * Unregisters a listener from receiving DocumentEvent notifications.
	 *
	 * @param obs the listener to unregister
	 */
	@Override
	public void unregisterListener(Listener<DocumentEvent> obs) {
		super.unregisterListener(obs);
	}

	/**
	 * Gets the file associated with the document.
	 *
	 * @return the file associated with the document.
	 * @ensures \result != null
	 */
	@Override
	public File getFile() {
		return new File(pathToFile);
	}

	/**
	 * Gets the title of the document.
	 *
	 * @return the title of the document.
	 * @ensures \result != null
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the author of the document.
	 *
	 * @return the author of the document.
	 * @ensures \result != null
	 */
	@Override
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets the MIME type of the document.
	 *
	 * @return the MIME type of the document.
	 * @ensures \result != null
	 */
	@Override
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Gets the last modified date.
	 *
	 * @return the last modifie date of the document.
	 * @ensures \result != null
	 */
	@Override
	public LocalDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * Gets the number of pages of the document,if that info is available.
	 *
	 * @return  number of pages of the document, if available.
	 */
	@Override
	public Optional<Integer> getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * Gets the last page visited in the document.
	 *
	 * @return the last page visited.
	 * @ensures \result >= 0
	 */
	@Override
	public int getLastPageVisited() {
		return lastPageVisited;
	}

	/**
	 * Gets the list of bookmarked pages.
	 *
	 * @return the list of bookmarked pages.
	 * @ensures \result != null
	 */
	@Override
	public List<Integer> getBookmarks() {
		List<Integer> bookmarkedPages = new ArrayList<>();
		for (Map.Entry<Integer, Page> entry : pages.entrySet()) {
			if (entry.getValue().isBookmarked()) {
				bookmarkedPages.add(entry.getKey());
			}
		}
		return bookmarkedPages;
	}

	/**
	 * Toggles the bookmark status of a specific page.
	 * Changes the last modified date.
	 *
	 * @param pageNum the page number to toggle bookmark.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 */
	@Override
	public void toggleBookmark(int pageNum) {
		Page page = pages.computeIfAbsent(pageNum, Page::new);
		page.toggleBookmark();
		this.lastModifiedDate = LocalDate.now();
		emitEvent(new ToggleBookmarkEvent(this, pageNum, page.isBookmarked()));
	}

	/**
	 * Sets the title of the document.
	 * Changes the last modified date.
	 *
	 * @param title the title to set.
	 * @requires title != null
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
		this.lastModifiedDate = LocalDate.now();
	}

	/**
	 * Sets the author of the document.
	 * Changes the last modified date.
	 *
	 * @param author the author to set.
	 * @requires author != null
	 */
	@Override
	public void setAuthor(String author) {
		this.author = author;
		this.lastModifiedDate = LocalDate.now();
	}

	/**
	 * Sets the last page visited in the document.
	 *
	 * @param lastPageVisited the last page visited to set.
	 * @requires lastPageVisited >= 0
	 * @requires getNumberOfPages().isPresent() => lastPageVisited < getNumberOfPages().get()
	 */
	@Override
	public void setLastPageVisited(int lastPageVisited) {
		this.lastPageVisited = lastPageVisited;
	}

	/**
	 * Adds an annotation to a specific page and changes the last modified date.
	 *
	 * @param pageNum the page number to add the annotation.
	 * @param text the text of the annotation.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @requires text != null
	 */
	@Override
	public void addAnnotation(int pageNum, String text) {
		Page page = pages.computeIfAbsent(pageNum, Page::new);
		page.addAnnotation(text);
		this.lastModifiedDate = LocalDate.now();
		int annotIndex = page.getAnnotationCount() - 1;
		boolean hasAnnots = page.hasAnnotations();
		emitEvent(new AddAnnotationEvent(this, pageNum, annotIndex, text, hasAnnots));
	}

	/**
	 * Removes an annotation from a specific page and changes the last modified date.
	 *
	 * @param pageNum the page number to remove the annotation from.
	 * @param annotNum the annotation number to remove.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @requires hasAnnotations(pageNum) &&
	 * @requires annotNum >= 0 && annotNum < numberOfAnnotations(pageNum)
	 */
	@Override
	public void removeAnnotation(int pageNum, int annotNum) {
		Page page = pages.get(pageNum);
		if (page != null) {
			page.removeAnnotation(annotNum);
			this.lastModifiedDate = LocalDate.now();
			emitEvent(new RemoveAnnotationEvent(this, pageNum, annotNum, false));
		}
	}

	/**
	 * Gets the number of annotations for a specific page.
	 *
	 * @param pageNum the page number to get the number of annotations .
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @return the number of annotations.
	 * @ensures \result >= 0
	 */
	@Override
	public int numberOfAnnotations(int pageNum) {
		Page page = pages.get(pageNum);
		return (page == null) ? 0 : page.getAnnotationCount();
	}

	/**
	 * Gets the annotations for a specific page.
	 *
	 * @param pageNum the page number to get annotations for.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @return an iterable of annotations.
	 * @ensures \result != null
	 */
	@Override
	public Iterable<String> getAnnotations(int pageNum) {
		Page page = pages.get(pageNum);
		if (page == null) {
			return Collections.emptyList();
		}
		List<String> result = new ArrayList<>();
		for (leibooks.domain.core.Annotation ann : page.getAnnotations()) {
			result.add(ann.getAnnotationText());
		}
		return result;
	}

	/**
	 * Gets the text of a specific annotation on a specific page.
	 *
	 * @param pageNum the page number of the annotation.
	 * @param annotNum the annotation number.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @requires hasAnnotations(pageNum) && annotNum >= 0 && annotNum < numberOfAnnotations(pageNum)
	 * @return the text of the annotation.
	 * @ensures \result != null
	 */
	@Override
	public String getAnnotationText(int pageNum, int annotNum) {
		Page page = pages.get(pageNum);
		if (page == null) {
			return null;
		}
		try {
			return page.getAnnotationText(annotNum);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Checks if a specific page has annotations.
	 *
	 * @param pageNum the page number to check.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @return true if the page has annotations, false otherwise.
	 */
	@Override
	public boolean hasAnnotations(int pageNum) {
		Page page = pages.get(pageNum);
		return page != null && page.hasAnnotations();
	}

	/**
	 * Checks if a specific page is bookmarked.
	 *
	 * @param pageNum the page number to check.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @return true if the page is bookmarked, false otherwise.
	 */
	@Override
	public boolean isBookmarked(int pageNum) {
		Page page = pages.get(pageNum);
		return page != null && page.isBookmarked();
	}

	/**
	 * Checks if the document has any bookmarked page.
	 *
	 * @return true if the document is bookmarked, false otherwise.
	 */
	@Override
	public boolean isBookmarked() {
		for (Page page : pages.values()) {
			if (page.isBookmarked()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if any document data matches the given regular expression
	 *
	 * @param regexp the regular expression to be used
	 * @requires regexp != null
	 * @return whether some data of the document matches with the given regexp
	 * @ensures returns true if title or author matches the regexp (case-insensitive)
	 */
	@Override
	public boolean matches(String regexp) {
		String combined = title + " " + author;
		return combined.matches("(?i).*" + regexp + ".*");
	}

	/**
	 * Compares this document to another document based on their file on disk.
	 *
	 * @param other the other document to compare to.
	 * @return a negative integer, zero, or a positive integer as this document
	 *         is less than, equal to, or greater than the specified document.
	 */
	@Override
	public int compareTo(IDocument other) {
		return this.getFile().getAbsolutePath()
				.compareTo(other.getFile().getAbsolutePath());
	}


	// funções opcionais
	@Override
	public String toString() {
		return "Document{title=" + title
				+ ", author=" + author
				+ ", file=" + pathToFile.replace("\\", "/")
				+ ", date=" + lastModifiedDate
				+ ", mimeType=" + mimeType
				+ ", numPages=" + numberOfPages
				+ ", lastPageVisited=" + lastPageVisited
				+ ", pages=" + pages
				+ "}";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof IDocument)) return false;
		IDocument other = (IDocument) obj;
		return this.getFile().getAbsolutePath()
				.equals(other.getFile().getAbsolutePath());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getFile().getAbsolutePath());
	}
}
