package leibooks.domain.facade;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import leibooks.domain.facade.events.DocumentEvent;
import leibooks.utils.RegExpMatchable;
import leibooks.utils.Subject;

/**
 * Interface representing a document whose pages can be viwed, annotated and bookmarked.
 * The title and author of the document can be set and the last page visited can be tracked.
 * Changes to the document are recorded with the last modified date. The documents's file is 
 * used as its unique identifier.
 * 
 * Extends {@link Subject} for {@link DocumentEvent}.
 * A document emits several document events
 * 		AddAnnotationEvent when an annotation is added
 * 		RemoveAnnotationEvent when an annotation is removed
 * 		ToggleBookmarkEvent when a bookmark in one of its page is toggled
 */
public interface IDocument extends Comparable<IDocument>, RegExpMatchable, Subject<DocumentEvent>{

	/**
	 * Gets the file associated with the document.
	 * 
	 * @return the file associated with the document.
	 * @ensures \result != null
	 */
	File getFile();

	/**
	 * Gets the title of the document.
	 * 
	 * @return the title of the document.
	 * @ensures \result != null
	 */
	String getTitle();

	/**
	 * Gets the author of the document.
	 * 
	 * @return the author of the document.
	 * @ensures \result != null
	 */
	String getAuthor();

	/**
	 * Gets the MIME type of the document.
	 * 
	 * @return the MIME type of the document.
	 * @ensures \result != null
	 */
	String getMimeType();

	/**
	 * Gets the last modified date.
	 * 
	 * @return the last modifie date of the document.
	 * @ensures \result != null
	 */
	LocalDate getLastModifiedDate();

	/**
	 * Gets the number of pages of the document,if that info is available.
	 * 
	 * @return  number of pages of the document, if available.
	 */
	Optional<Integer> getNumberOfPages();

	/**
	 * Gets the last page visited in the document.
	 * 
	 * @return the last page visited.
	 * @ensures \result >= 0
	 */
	int getLastPageVisited();

	/**
	 * Gets the list of bookmarked pages.
	 * 
	 * @return the list of bookmarked pages.
	 * @ensures \result != null
	 */
	List<Integer> getBookmarks();

	/**
	 * Toggles the bookmark status of a specific page. 
	 * Changes the last modified date.
	 * 
	 * @param pageNum the page number to toggle bookmark.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 */
	void toggleBookmark(int pageNum);

	/**
	 * Sets the title of the document.
	 * Changes the last modified date.
	 * 
	 * @param title the title to set.
	 * @requires title != null
	 */
	void setTitle(String title);

	/**
	 * Sets the author of the document.
	 * Changes the last modified date.
	 * 
	 * @param author the author to set.
	 * @requires author != null
	 */
	void setAuthor(String author);

	/**
	 * Sets the last page visited in the document.
	 * 
	 * @param lastPageVisited the last page visited to set.
	 * @requires lastPageVisited >= 0
	 * @requires getNumberOfPages().isPresent() => lastPageVisited < getNumberOfPages().get()
	 */
	void setLastPageVisited(int lastPageVisited);

	/**
	 * Adds an annotation to a specific page and changes the last modified date.
	 * 
	 * @param pageNum the page number to add the annotation.
	 * @param text the text of the annotation.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @requires text != null 
	 */
	void addAnnotation(int pageNum, String text);

	/**
	 * Removes an annotation from a specific page and changes the last modified date.
	 * 
	 * @param pageNum the page number to remove the annotation from.
	 * @param annotNum the annotation number to remove.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @requires hasAnnotations(pageNum) && 	
	 */
	void removeAnnotation(int pageNum, int annotNum);

	/**
	 * Gets the number of annotations for a specific page.
	 * 
	 * @param pageNum the page number to get the number of annotations .
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @return the number of annotations.
	 * @ensures \result >= 0
	 */
	int numberOfAnnotations(int pageNum);

	/**
	 * Gets the annotations for a specific page.
	 * 
	 * @param pageNum the page number to get annotations for.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @return an iterable of annotations.
	 * @ensures \result != null
	 */
	Iterable<String> getAnnotations(int pageNum);

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
	String getAnnotationText(int pageNum, int annotNum);

	/**
	 * Checks if a specific page has annotations.
	 * 
	 * @param pageNum the page number to check.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @return true if the page has annotations, false otherwise.
	 */
	boolean hasAnnotations(int pageNum);

	/**
	 * Checks if a specific page is bookmarked.
	 *  
	 * @param pageNum the page number to check.
	 * @requires pageNum >= 0
	 * @requires getNumberOfPages().isPresent() => pageNum < getNumberOfPages().get()
	 * @return true if the page is bookmarked, false otherwise.
	 */
	boolean isBookmarked(int pageNum);

	/**
	 * Checks if the document has any bookmarked page.
	 * 
	 * @return true if the document is bookmarked, false otherwise.
	 */
	boolean isBookmarked();

	/**
	 * Checks if any document data matches the given regular expression
	 *  
	 * @param regexp the regular expression to be used
	 * @requires regexp != null
	 * @return whether some data of the document matches with the given regexp
	 */
	boolean matches(String regexp);

	/**
	 * Compares this document to another document based on their file on disk.
	 * 
	 * @param other the other document to compare to.
	 * @return a negative integer, zero, or a positive integer as this document
	 *         is less than, equal to, or greater than the specified document.
	 */
	int compareTo(IDocument other);
}