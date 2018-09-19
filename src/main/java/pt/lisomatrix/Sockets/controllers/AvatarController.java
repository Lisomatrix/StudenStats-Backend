package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.lisomatrix.Sockets.models.File;
import pt.lisomatrix.Sockets.models.Person;
import pt.lisomatrix.Sockets.repositories.FilesRepository;
import pt.lisomatrix.Sockets.requests.models.Response;
import pt.lisomatrix.Sockets.requests.models.UploadFileResponse;
import pt.lisomatrix.Sockets.storage.FileStorageService;
import pt.lisomatrix.Sockets.storage.StorageService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
public class AvatarController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FilesRepository filesRepository;

    @CrossOrigin
    @PostMapping("/api/{token}/avatar")
    public UploadFileResponse uploadAvatar(@PathVariable @NotNull String token, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        //String fileName = fileStorageService.storeFile(file);
        String fileName = fileStorageService.saveFile(file, token);

        File uploadFile = new File();

        uploadFile.setFilePath(fileName);
        uploadFile.setFileName(file.getName());
        uploadFile.setFileSize("" + file.getSize());
        Person temp = new Person(); temp.setUserId(1l);
        uploadFile.setOwner(temp);

        filesRepository.save(uploadFile);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @CrossOrigin
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //logger.info("Could not determine file type.");
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

    /***
     * Response Generate helper
     *
     * @param message // Message to send on response
     * @param success // Success message to send
     * @return Response
     */
    private Response generateResponse(Object message, boolean success) {
        // Create response and populate it
        Response response = new Response();

        response.setSuccess(success);
        response.setMessage(message);

        //return response
        return response;
    }
}