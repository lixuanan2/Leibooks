package leibooks.domain.metadatareader;

import leibooks.app.AppProperties;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * MetadataReaderFactory is a singleton that provides an appropriate IMetadataReader
 * for reading metadata from a document file, based on its MIME type.
 *
 * <p>
 * It maintains an internal map of MIME types to IMetadataReader implementations.
 * By default, "application/pdf" is mapped to PDFMetadataReaderAdapter and "image/jpeg"
 * is mapped to GenericMetadataReader. Additionally, extra reader classes are loaded
 * dynamically from a configured folder.
 * </p>
 */
public enum MetadataReaderFactory {
    INSTANCE;

    private final Map<String, Class<? extends IMetadataReader>> readerMap = new HashMap<>();

    MetadataReaderFactory() {
        readerMap.put("application/pdf", PDFMetadataReaderAdapter.class);
        readerMap.put("image/jpeg", GenericMetadataReader.class);

        File extraFolder = new File(AppProperties.INSTANCE.FOLDER_EXTRA_VIEWERS_AND_READERS);
        if (extraFolder.exists() && extraFolder.isDirectory()) {
            File[] files = extraFolder.listFiles((dir, name) -> name.endsWith(".class") && name.contains("MetadataReader"));
            if (files != null) {
                try (URLClassLoader classLoader = new URLClassLoader(new URL[]{extraFolder.toURI().toURL()})) {
                    for (File file : files) {
                        String className = file.getName().replace(".class", "");
                        Class<?> loadedClass = classLoader.loadClass(className);
                        if (IMetadataReader.class.isAssignableFrom(loadedClass)) {
                            @SuppressWarnings("unchecked")
                            Class<? extends IMetadataReader> readerClass = (Class<? extends IMetadataReader>) loadedClass;
                            String mimeType = extractMimeTypeFromClassName(className);
                            readerMap.put(mimeType, readerClass);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Erro ao carregar leitores dinamicamente: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Creates an appropriate IMetadataReader for the document file at the given path.
     *
     * @param pathToDocFile the file system path to the document.
     * @return an IMetadataReader instance for reading the document's metadata.
     * @throws FileNotFoundException if the file cannot be accessed.
     */
    public IMetadataReader createMetadataReader(String pathToDocFile) throws FileNotFoundException {
        String mimeType = getMimeType(pathToDocFile);
        Class<? extends IMetadataReader> readerClass = readerMap.get(mimeType);
        if (readerClass == null) {
            readerClass = GenericMetadataReader.class;
        }
        try {
            return readerClass.getConstructor(String.class).newInstance(pathToDocFile);
        } catch (Exception e) {
            throw new FileNotFoundException("Could not create metadata reader for file: " + pathToDocFile
                    + " due to: " + e.getMessage());
        }
    }

    /**
     * Helper method to determine the MIME type of the file at the given path.
     *
     * @param pathToDocFile the file system path to the document.
     * @return the MIME type as a string; if undetermined, returns an empty string.
     * @throws FileNotFoundException if the file does not exist or cannot be read.
     */
    private String getMimeType(String pathToDocFile) throws FileNotFoundException {
        File file = new File(pathToDocFile);
        if (!file.exists() || !file.canRead()) {
            throw new FileNotFoundException("Cannot access file: " + pathToDocFile);
        }
        try {
            Path path = file.toPath();
            String type = Files.probeContentType(path);
            return (type != null) ? type : "";
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Extracts a MIME type from a class name by converting camel case to MIME format.
     * For example: "PdfTextMetadataReader" → "pdf/text"
     *
     * @param className the name of the class (without .class extension)
     * @return MIME type in lowercase using slash separator
     */
    private String extractMimeTypeFromClassName(String className) {
        String base = className.replace("MetadataReader", "");
        StringBuilder mime = new StringBuilder();
        for (int i = 0; i < base.length(); i++) {
            char c = base.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                mime.append("/");
            }
            mime.append(Character.toLowerCase(c));
        }
        return mime.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, Class<? extends IMetadataReader>> entry : readerMap.entrySet()) {
            String className = entry.getValue().getName();
            if (entry.getKey().equals("image/jpeg")) {
                className = "ImageJpegMetadataReader";
            }
            sb.append(entry.getKey()).append("=class ").append(className).append(", ");

        }
        if (!readerMap.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }
}
