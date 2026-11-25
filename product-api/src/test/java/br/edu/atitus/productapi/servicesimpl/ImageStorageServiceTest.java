package br.edu.atitus.productapi.servicesimpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class ImageStorageServiceTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void acceptsJpegPngAndWebpWithMatchingFileSignatures() throws Exception {
        ImageStorageService service = service();
        String jpegPath = service.store(file("image/jpg", new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        String pngPath = service.store(file("image/png", new byte[]{(byte) 0x89, 'P', 'N', 'G', 13, 10, 26, 10, 0, 0, 0, 0}));
        String webpPath = service.store(file("image/webp", new byte[]{'R', 'I', 'F', 'F', 0, 0, 0, 0, 'W', 'E', 'B', 'P'}));

        assertTrue(jpegPath.endsWith(".jpg"));
        assertTrue(pngPath.endsWith(".png"));
        assertTrue(webpPath.endsWith(".webp"));
        assertTrue(Files.exists(temporaryDirectory.resolve(Path.of(jpegPath).getFileName())));
        assertTrue(Files.exists(temporaryDirectory.resolve(Path.of(pngPath).getFileName())));
        assertTrue(Files.exists(temporaryDirectory.resolve(Path.of(webpPath).getFileName())));
    }

    @Test
    void rejectsAnImageWhoseContentDoesNotMatchItsDeclaredType() throws Exception {
        ImageStorageService service = service();
        MockMultipartFile fakeWebp = file("image/webp", new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.store(fakeWebp));

        assertEquals(400, exception.getStatusCode().value());
    }

    private ImageStorageService service() throws Exception {
        ImageStorageService service = new ImageStorageService();
        Field location = ImageStorageService.class.getDeclaredField("imageLocation");
        location.setAccessible(true);
        location.set(service, temporaryDirectory.toString());
        return service;
    }

    private static MockMultipartFile file(String contentType, byte[] contents) {
        return new MockMultipartFile("file", "cover", contentType, contents);
    }
}
