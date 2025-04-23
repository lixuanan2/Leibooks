package leibooks.domain.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a page within a document.
 *
 * This class maintains a collection of annotations, each identified by
 * a unique ID, and a bookmark flag that indicates whether the page is
 * bookmarked or not.
 */
public class Page {

    private final int pageNum;
    private boolean bookmarked;

    // A map from annotation ID to the Annotation object
    private final Map<Integer, Annotation> annotations = new LinkedHashMap<>();
    private int nextAnnotationId = 0;

    /**
     * Constructs a Page with the specified page number.
     *
     * @param pageNum the number of this page
     * @ensures pageNum > 0
     */
    public Page(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * Adds a new annotation to this page.
     *
     * @param text the text content of the annotation
     * @ensures text != null
     */
    public void addAnnotation(String text) {
        nextAnnotationId++;
        annotations.put(nextAnnotationId, new Annotation(text));
    }

    /**
     * Returns the total number of annotations on this page.
     *
     * @return the count of annotations
     * @ensures \result >= 0
     */
    public int getAnnotationCount() {
        return annotations.size();
    }

    /**
     * Returns an iterable collection of all annotations on this page.
     *
     * @return an unmodifiable iterable over the annotations
     */
    public Iterable<Annotation> getAnnotations() {
        return Collections.unmodifiableCollection(annotations.values());
    }

    /**
     * Returns the text of the annotation with the specified ID.
     *
     * @param annotationId the unique ID of the annotation
     * @return the annotation text
     * @throws IllegalArgumentException if no annotation exists for the given ID
     * @ensures \result != null
     */
    public String getAnnotationText(int annotationId) {
        Annotation ann = annotations.get(annotationId);
        if (ann == null) {
            throw new IllegalArgumentException("No annotation found with ID: " + annotationId);
        }
        return ann.getAnnotationText();
    }

    /**
     * Returns the page number of this page.
     *
     * @return the page number
     * @ensures \result > 0
     */
    public int getPageNum() {
        return this.pageNum;
    }

    /**
     * Checks whether this page has any annotations.
     *
     * @return true if there is at least one annotation, false otherwise
     */
    public boolean hasAnnotations() {
        return !annotations.isEmpty();
    }

    /**
     * Checks if this page is bookmarked.
     *
     * @return true if bookmarked, false otherwise
     */
    public boolean isBookmarked() {
        return bookmarked;
    }

    /**
     * Removes the annotation with the specified ID.
     *
     * @param annotationId the unique ID of the annotation to remove
     * @throws IllegalArgumentException if the ID does not exist
     */
    public void removeAnnotation(int annotationId) {
        if (!annotations.containsKey(annotationId)) {
            throw new IllegalArgumentException("No annotation found with ID: " + annotationId);
        }
        annotations.remove(annotationId);
    }

    /**
     * Toggles the bookmark status of this page.
     * If the page is currently bookmarked, it will be unbookmarked.
     * Otherwise, it becomes bookmarked.
     */
    public void toggleBookmark() {
        bookmarked = !bookmarked;
    }

    public String toString() {
        return "Page{bookmark=" + this.isBookmarked()
                + ", annotations=" + this.annotations.values()
                + ", pageNum=" + this.pageNum + "}";
    }
}
