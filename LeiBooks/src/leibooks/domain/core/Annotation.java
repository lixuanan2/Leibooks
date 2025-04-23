package leibooks.domain.core;
/**
 * Represents a text annotation on a page.
 *
 * An annotation holds a piece of text that describes or comments on
 * part of the document content.
 */
public class Annotation {

    private String text;

    /**
     * Constructors
     *
     * @param text the initial text for this annotation
     * @ensures text != null
     */
    public Annotation(String text) {
        this.text = text;
    }

    /**
     * Returns the text of this annotation.
     *
     * @return the annotation text
     * @ensures \result != null
     */
    public String getAnnotationText() {
        return this.text;
    }

    /**
     * Updates the text of this annotation.
     *
     * @param text the new annotation text
     * @ensures this.annotationText == text
     */
    public void setAnnotationText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Annotation [text=" + text + "]";
    }
}
