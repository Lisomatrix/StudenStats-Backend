package pt.lisomatrix.Sockets.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.lisomatrix.Sockets.models.Storage;
import pt.lisomatrix.Sockets.models.User;
import pt.lisomatrix.Sockets.redis.models.RedisStorageServer;
import pt.lisomatrix.Sockets.repositories.FilesRepository;
import pt.lisomatrix.Sockets.repositories.StorageRepository;
import pt.lisomatrix.Sockets.repositories.UsersRepository;
import pt.lisomatrix.Sockets.requests.models.Response;
import pt.lisomatrix.Sockets.requests.models.UploadFileResponse;
import pt.lisomatrix.Sockets.service.StorageService;
import pt.lisomatrix.Sockets.storage.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserStorageController {

    private final long STORAGE_PER_USER = 1000000000;

    /***
     * File Storage Service for save and get stored files
     */
    @Autowired
    private FileStorageService fileStorageService;

    /***
     * Database Repository to get and add info about files
     */
    @Autowired
    private FilesRepository filesRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private UsersRepository usersRepository;

    /***
     * Allows users to upload files
     *
     *
     * @param file
     * @param principal
     * @return
     */
    @CrossOrigin
    @PostMapping("/api/file")
    public UploadFileResponse uploadAvatar(@RequestParam("file") MultipartFile file, Principal principal) throws Exception {

        User user = usersRepository.findById(Long.parseLong(principal.getName())).get();

        // Name the file with an ID prefix
        File namedFile = getFileWithId(file);

        // Find an available storage server
        Storage storage = getSuitableStorage(file.getSize());

        if(storage == null) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "There is no available space");
        }

        if(!hasStorage(user)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "There is no available space for this user");
        }

        // Upload file to the storage server
        UploadFileResponse uploadFileResponse = storageService.saveResource(new FileSystemResource(namedFile), storage.getIp()).block();

        // Save file on the database after it uploads
        pt.lisomatrix.Sockets.models.File userFile = new pt.lisomatrix.Sockets.models.File();

        userFile.setFileSize(file.getSize());
        userFile.setFileName(namedFile.getName());
        userFile.setStorageNumber(storage.getStorageNumber());
        userFile.setUser(user);
        userFile.setFileId(StringUtils.substringBefore(namedFile.getName(), "-"));

        filesRepository.save(userFile);

        // TODO: UPDATE THIS TO USE REDIS INSTEAD OF DATABASE
        // Update storage available space
        storage.setUsed(storage.getUsed() + file.getSize());
        storageRepository.save(storage);

        return uploadFileResponse;
    }

    /***
     * Allows users to download files
     *
     *
     * @param fileId
     * @param request
     * @param principal
     * @return
     */
    @CrossOrigin
    @GetMapping("/file/{fileId:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, HttpServletRequest request, Principal principal) {

        Optional<pt.lisomatrix.Sockets.models.File> foundFile = filesRepository.findById(fileId);

        if(foundFile.isPresent()) {

            pt.lisomatrix.Sockets.models.File file = foundFile.get();

            if(file.getUser().getUserId() != Long.parseLong(principal.getName())) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
            }

            Storage storage = findStorage(file.getStorageNumber());

            Resource resource = storageService.getFile(file.getFileName(), storage.getIp()).block();

            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {

                ex.printStackTrace();
            }

            // Fallback to the default content type if type could not be determined
            if(contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "File not found");
    }

    private boolean hasStorage(User user) {
        return filesRepository.getUsedSpace(user.getUserId()) <= STORAGE_PER_USER;
    }

    private File getFileWithId(MultipartFile file) throws Exception {

        String fileName = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID().toString().replace("-", "").replace(" ", "__") + "--" + file.getOriginalFilename();

        java.io.File namedFile = new java.io.File(fileName);

        file.transferTo(namedFile);

        return namedFile;
    }

    private Storage getSuitableStorage(long fileSize) {

        List<Storage> storages = storageRepository.findAll();


        for(int i = 0; i < storages.size(); i++) {

            Storage temp = storages.get(i);

            if(temp.isMaster() && (temp.getCapacity() - temp.getUsed()) > fileSize) {
                return temp;
            }
        }

        return null;
    }

    private Storage findStorage(int storageNumber) {

        List<Storage> storages = storageRepository.findAll();

        for(int i = 0; i < storages.size(); i++) {

            Storage temp = storages.get(i);

            if(temp.getStorageNumber() == storageNumber && temp.isMaster()) {
                return temp;
            }
        }

        return null;
    }

}
