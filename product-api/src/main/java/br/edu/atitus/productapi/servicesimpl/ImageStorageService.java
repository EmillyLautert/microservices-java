package br.edu.atitus.productapi.servicesimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageStorageService {
    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", ".jpg", "image/png", ".png");
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;

    @Value("${app.images.location:/data/images}")
    private String imageLocation;

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie um arquivo de imagem");
        if (file.getSize() > MAX_IMAGE_SIZE)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A imagem deve ter no máximo 5 MB");
        String extension = ALLOWED_TYPES.get(file.getContentType());
        if (extension == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de imagem não suportado; use JPEG ou PNG");
        try {
            Path directory = Paths.get(imageLocation).toAbsolutePath().normalize();
            Files.createDirectories(directory);
            String filename = UUID.randomUUID() + extension;
            Files.copy(file.getInputStream(), directory.resolve(filename));
            return "/images/" + filename;
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível armazenar a imagem");
        }
    }
}
