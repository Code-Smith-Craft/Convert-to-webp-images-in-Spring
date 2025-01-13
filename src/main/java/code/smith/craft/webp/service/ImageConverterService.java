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
        BufferedImage image = ImageIO.read(inputFile);

        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]); // Yo'qotishli siqish

        writeParam.setCompressionQuality(0.75f);

        String outputDirectory = "uploaded_images/";
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        String outputFileName = "converted_" + System.currentTimeMillis() + ".webp";
        String outputFilePath = outputDirectory + outputFileName;
        File outputFile = new File(outputFilePath);

        ImageOutputStream ios = new FileImageOutputStream(outputFile);
        writer.setOutput(ios);
        writer.write(null, new IIOImage(image, null, null), writeParam);
        ios.flush();
        ios.close();

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(outputFileName)
                .toUriString();

        return fileDownloadUri;
    }

}
