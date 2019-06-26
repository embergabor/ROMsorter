package hu.embergabor.romsorter.model;

import java.nio.file.Path;

public class Rom {
    private String title;
    private String region;
    private String type;
    private String version;
    private String filename;
    private String extension;
    private Path path;

    public Rom() {
    }

    public Rom(String title, String region, String type, String version, String filename, String extension, Path path) {
        this.title = title;
        this.region = region;
        this.type = type;
        this.version = version;
        this.filename = filename;
        this.extension = extension;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
