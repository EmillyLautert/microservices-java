package br.edu.atitus.productapi.servicesimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageStorageService {
    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/jpeg", ".jpg", "image/jpg", ".jpg",
            "image/png", ".png", "image/webp", ".webp");
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;

    @Value("${app.images.location:/data/images}")
    private String imageLocation;

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Envie um arquivo de imagem");
        if (file.getSize() > MAX_IMAGE_SIZE)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A imagem deve ter no máximo 5 MB");
        try {
            String extension = getAllowedExtension(file);
            Path directory = Paths.get(imageLocation).toAbsolutePath().normalize();
            Files.createDirectories(directory);
            String filename = UUID.randomUUID() + extension;
            try (InputStream input = file.getInputStream()) {
                Files.copy(input, directory.resolve(filename));
            }
            return "/images/" + filename;
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível armazenar a imagem");
        }
    }

    private String getAllowedExtension(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String declaredType = contentType == null ? ""
                : contentType.split(";", 2)[0].trim().toLowerCase(Locale.ROOT);
        String extension = ALLOWED_TYPES.get(declaredType);
        if (extension == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Formato de imagem não suportado; use JPG, JPEG, PNG ou WebP");

        String detectedType;
        try (InputStream input = file.getInputStream()) {
            detectedType = detectImageType(input.readNBytes(12));
        }
        String normalizedDeclaredType = "image/jpg".equals(declaredType) ? "image/jpeg" : declaredType;
        if (!Objects.equals(normalizedDeclaredType, detectedType))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O conteúdo do arquivo não corresponde ao formato de imagem informado");
        return extension;
    }

    private static String detectImageType(byte[] header) {
        if (header.length >= 3 && unsigned(header[0]) == 0xFF && unsigned(header[1]) == 0xD8
                && unsigned(header[2]) == 0xFF) return "image/jpeg";
        if (header.length >= 8 && unsigned(header[0]) == 0x89 && header[1] == 'P' && header[2] == 'N'
                && header[3] == 'G' && unsigned(header[4]) == 0x0D && unsigned(header[5]) == 0x0A
                && unsigned(header[6]) == 0x1A && unsigned(header[7]) == 0x0A) return "image/png";
        if (header.length >= 12 && asciiAt(header, 0, "RIFF") && asciiAt(header, 8, "WEBP"))
            return "image/webp";
        return null;
    }

    private static boolean asciiAt(byte[] bytes, int offset, String expected) {
        for (int index = 0; index < expected.length(); index++) {
            if (bytes[offset + index] != (byte) expected.charAt(index)) return false;
        }
        return true;
    }

    private static int unsigned(byte value) {
        return value & 0xFF;
    }
}
