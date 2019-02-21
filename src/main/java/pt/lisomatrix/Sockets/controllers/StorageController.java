package pt.lisomatrix.Sockets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.lisomatrix.Sockets.models.File;
import pt.lisomatrix.Sockets.models.Storage;
import pt.lisomatrix.Sockets.redis.models.RedisStorageServer;
import pt.lisomatrix.Sockets.redis.repositories.RedisStorageServersRepository;
import pt.lisomatrix.Sockets.repositories.StorageRepository;
import pt.lisomatrix.Sockets.requests.models.CreateStorage;
import pt.lisomatrix.Sockets.service.StorageService;
import pt.lisomatrix.Sockets.storage.SetSlave;
import pt.lisomatrix.Sockets.storage.SetStorage;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StorageController {

    @Autowired
    private RedisStorageServersRepository redisStorageServersRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageRepository storageRepository;

    @PostMapping("/config/storage")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
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

        storage.setCapacity(1000000000l);
        storage.setIp(createStorage.getStorageIp());
        storage.setFiles(new ArrayList<File>());
        storage.setUsed(0l);
        storage.setMaster(createStorage.isMaster());


        storageRepository.save(storage);

        return ResponseEntity.ok(createStorage);
    }
}
