package pt.lisomatrix.Sockets.service;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import pt.lisomatrix.Sockets.requests.models.UploadFileResponse;
import pt.lisomatrix.Sockets.storage.Alive;
import pt.lisomatrix.Sockets.storage.SetSlave;
import pt.lisomatrix.Sockets.storage.SetStorage;
import reactor.core.publisher.Mono;

@Service
public class StorageService {

    private final WebClient webClient = WebClient.create("http://localhost:8080/");

    private final String AUTHORIZATION_HEADER = "Authorization";

    public StorageService() {

    }

    public Mono<Resource> getFile(String file, String url) {

        webClient.mutate().baseUrl("http://" + url + ":8080");

        return webClient.get()
                .uri("/download/{file}", file).accept(MediaType.ALL)
                .header(AUTHORIZATION_HEADER, "8899")
                .retrieve()
                .bodyToMono(Resource.class);
    }

    public Mono<UploadFileResponse> saveResource(FileSystemResource file, String url) throws Exception {

        webClient.mutate().baseUrl("http://" + url + ":8080");

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file);

        return webClient.post()
                .uri("/upload")
                .syncBody(builder.build())
                .header("Authorization", "8899")
                .retrieve()
                .bodyToMono(UploadFileResponse.class)
                .onErrorResume(x -> {
                    System.out.println(x.getMessage());

                    return Mono.just(new UploadFileResponse("", "", "", 1l));
                });
    }

    public Mono<ClientResponse> deleteFile(String file, String url) {

        webClient.mutate().baseUrl("http://" + url + ":8080");

        return webClient.delete()
                .uri("/delete/{file}", file)
                .header(AUTHORIZATION_HEADER, "8899")
                .exchange();

    }

    public Mono<SetStorage> setStorageServer(SetStorage setStorage) {

        return webClient.post()
                .uri("/config")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(setStorage)
                .header(AUTHORIZATION_HEADER, "8899")
                .retrieve()
                .bodyToMono(SetStorage.class);
    }

    public Mono<SetSlave> setSlaveStorage(SetSlave setSlave) {

        return webClient.post()
                .uri("/slave")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(setSlave)
                .header(AUTHORIZATION_HEADER, "8899")
                .retrieve()
                .bodyToMono(SetSlave.class);

    }

    public Mono<Alive> getAlive(String url) {

        webClient.mutate().baseUrl(url);

        return webClient.get()
                .uri("/alive")
                .header(AUTHORIZATION_HEADER, "8899")
                .retrieve()
                .bodyToMono(Alive.class);
    }
}
