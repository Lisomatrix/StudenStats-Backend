package pt.lisomatrix.Sockets.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * File Storage Properties
 *
 * This properties are filled by the application.properties file
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
