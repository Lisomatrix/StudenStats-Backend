package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.lisomatrix.Sockets.models.File;
import pt.lisomatrix.Sockets.models.Storage;
import pt.lisomatrix.Sockets.redis.models.RedisStorageServer;
import pt.lisomatrix.Sockets.redis.repositories.RedisStorageServersRepository;
import pt.lisomatrix.Sockets.repositories.FilesRepository;
import pt.lisomatrix.Sockets.repositories.StorageRepository;
import pt.lisomatrix.Sockets.requests.models.CreateStorage;
import pt.lisomatrix.Sockets.response.models.StorageResponse;
import pt.lisomatrix.Sockets.service.StorageService;
import pt.lisomatrix.Sockets.storage.Alive;
import pt.lisomatrix.Sockets.storage.SetSlave;
import pt.lisomatrix.Sockets.storage.SetStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StorageController {

    @Autowired
    private RedisStorageServersRepository redisStorageServersRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private FilesRepository filesRepository;

    @GetMapping("/storage")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> getStorage() {
        List<Storage> storages = storageRepository.findAll();

        List<StorageResponse> storageResponses = new ArrayList<>();

        for(int i = 0; i < storages.size(); i++) {
            storageResponses.add(new StorageResponse(storages.get(i)));
        }

        return ResponseEntity.ok(storageResponses);
    }

    @GetMapping("/storage/{storageId}/alive")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> checkStorageOnline(@PathVariable("storageId") long storageId) {

        Optional<Storage> foundStorage = storageRepository.findById(storageId);

        if(foundStorage.isPresent()) {
            Storage storage = foundStorage.get();

            Alive alive = new Alive();

            try {
                alive = storageService.getAlive("http://" + storage.getIp() + ":8080").block();
            } catch (Exception ex) {
                alive.setAlive(false);

                return ResponseEntity.ok(alive);
            }

            return ResponseEntity.ok(alive);
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Storage not found");
    }

    @DeleteMapping("/storage/{storageId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> removeStorage(@PathVariable("storageId") long storageId) {

        Optional<Storage> foundStorage = storageRepository.findById(storageId);

        if(foundStorage.isPresent()) {

            Storage storage = foundStorage.get();

            Optional<List<File>> foundFiles = filesRepository.findAllByStorageNumber(storage.getStorageNumber());

            if(foundFiles.isPresent()) {
                List<File> files = foundFiles.get();

                for(int i = 0; i < files.size(); i++) {
                    storageService.deleteFile(files.get(i).getFileName(), storage.getIp()).block();
                    filesRepository.delete(files.get(i));
                }
            }

            storageRepository.delete(storage);

            return ResponseEntity.ok().build();
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Storage not found");
    }

    @PostMapping("/config/storage")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @CrossOrigin
    public ResponseEntity<?> addStorage(@RequestBody CreateStorage createStorage) {

        List<RedisStorageServer> storageServers = (List<RedisStorageServer>) redisStorageServersRepository.findAll();

        long lastStorageNumber = 0;

        for(int i = 0; i < storageServers.size(); i++) {
            if(storageServers.get(i).getStorageNumber() > lastStorageNumber) {
                lastStorageNumber = storageServers.get(i).getStorageNumber();
            }
        }

        if(lastStorageNumber == 0) {
            lastStorageNumber = 1;
        }

        SetStorage setStorage = new SetStorage();

        setStorage.setMaster(createStorage.isMaster());
        setStorage.setToken("8899");
        setStorage.setStorageNumber((int) lastStorageNumber);

        SetStorage receivedStorage = storageService.setStorageServer(setStorage).block();

        if(createStorage.isHasSlave()) {

            SetSlave setSlave = new SetSlave();

            setSlave.setAvailableSpace(0);
            setSlave.setUrl("http://" + createStorage.getSlaveIp() + "/:8080/");
            setSlave.setUsedSpace(0);

            SetSlave receivedSlave = storageService.setSlaveStorage(setSlave).block();
        }

        Storage storage = new Storage();

        storage.setCapacity(createStorage.getTotalSpace());
        storage.setIp(createStorage.getStorageIp());
        storage.setFiles(new ArrayList<File>());
        storage.setUsed(0l);
        storage.setMaster(createStorage.isMaster());
        storage.setStorageNumber((int) lastStorageNumber);

        storageRepository.save(storage);

        StorageResponse storageResponse = new StorageResponse(storage);

        return ResponseEntity.ok(storageResponse);
    }
}
