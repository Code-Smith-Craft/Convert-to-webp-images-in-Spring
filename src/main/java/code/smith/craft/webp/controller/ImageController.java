package code.smith.craft.webp.controller;

import code.smith.craft.webp.service.ImageConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/webp")
public class ImageController {

    @Autowired
    private ImageConverterService webpUtilsService;

    @PostMapping("/convert")
    public ResponseEntity<String> convertToWebp(@RequestParam("file") MultipartFile file) {
        try {
            // Faylni vaqtinchalik joyga saqlash
            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(tempFile);

            // WebP formatiga o'zgartirish va saqlash
            String outputFilePath = webpUtilsService.imageFileToWebpImageFile(tempFile);

            // Saqlangan faylning yo'lini qaytarish
            return ResponseEntity.ok(outputFilePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image");
        }
    }
}
