package code.smith.craft.webp.service;

import com.luciad.imageio.webp.WebPWriteParam;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class ImageConverterService {

    public String imageFileToWebpImageFile(File inputFile) throws IOException {
        // Tasvirni o'qing
        BufferedImage image = ImageIO.read(inputFile);

        // WebP ImageWriter ni oling
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

        // WebP parametrlarini sozlash
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]); // Yo'qotishli siqish

        // Siqish sifatini belgilash (0.0 - 1.0 oralig'ida, 1.0 eng yuqori sifat)
        writeParam.setCompressionQuality(0.75f); // 75% sifat, bu hajmni sezilarli darajada kamaytiradi

        // Rasmni saqlash uchun papkaga saqlash yo'li
        String outputDirectory = "uploaded_images/"; // Tashqi papka (resources papkasidan tashqarida)
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();  // Agar papka mavjud bo'lmasa, uni yaratamiz
        }

        // Faylni WebP formatida saqlash
        String outputFileName = "converted_" + System.currentTimeMillis() + ".webp";
        String outputFilePath = outputDirectory + outputFileName;
        File outputFile = new File(outputFilePath);

        ImageOutputStream ios = new FileImageOutputStream(outputFile);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), writeParam);
        ios.flush();
        ios.close();

        // Fayl URL manzilini qaytarish
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(outputFileName)
                .toUriString();

        return fileDownloadUri;  // Saqlangan rasmning URL manzilini qaytarish
    }

}
