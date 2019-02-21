package pt.lisomatrix.Sockets.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.lisomatrix.Sockets.exceptions.FileStorageException;
import pt.lisomatrix.Sockets.exceptions.MyFileNotFoundException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * This class is responsible for storing and loading files
 *
 */
@Service
public class FileStorageService {

    /***
     * The path of the files storage folder
     */
    private Path fileStorageLocation;

    /***
     * Initialize all class dependencies and
     * Create files storage folder if it doesn't exist
     *
     * @param fileStorageProperties
     */
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    /***
     * TODO OR COMPLETE OR REMOVE
     *
     * @param file
     * @return
     */
    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /***
     * Handles saving a file logic
     *
     * @param file
     * @param userId
     * @return
     */
    public String saveFile(MultipartFile file, String userId) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {

            if(fileName.contains("..")) {
                fileName = fileName.replace("..", "");
            }

            Path targetLocation = this.fileStorageLocation.resolve(createUserFolder(userId, fileName));
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();

        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    /***
     * TODO OR COMPLETE OR REMOVE
     *
     * @param path
     * @return
     */
    public Resource loadFile(String path) {

        try {
            Resource  resource = new UrlResource(path);

            if(resource.exists()) {
                return resource;
            } else {
                throw  new MyFileNotFoundException("Ficheiro não encontrado.");
            }

        } catch (MalformedURLException e) {
            throw  new MyFileNotFoundException("Ficheiro não encontrado.");
        }
    }

    /***
     * Handle getting file location logic
     *
     * @param fileName
     * @return
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    /***
     * Sets the file storage Path
     *
     * @param path
     */
    private void setPath(String path) {
        this.fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    /***
     * Helper to create the User personal folder
     *
     * @param userId
     * @param fileName
     * @return
     */
    private String createUserFolder(String userId, String fileName) {

        // Create date format and get date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

        // Join user id with current date
        String userFolder =  System.getProperty("user.dir") + "/" + userId + "/" + dateFormat.format(date).trim().toLowerCase();

        // Create Folder with path
        File files = new File(userFolder);

        // If folders doesn't exist then create them
        if (!files.exists()) {
            files.mkdirs();
        }

        // Change save file path
        setPath(userFolder);

        // Add file name
        userFolder = userFolder + "/" + fileName;

        // return file path
        return userFolder;
    }
}
