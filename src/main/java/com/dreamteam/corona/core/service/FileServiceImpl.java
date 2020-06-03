package com.dreamteam.corona.core.service;

import com.dreamteam.corona.core.configuration.FileStorageProperties;
import com.dreamteam.corona.quarantine.exception.FileStorageException;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@AllArgsConstructor
@Service("fileService")
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private final FileStorageProperties fileStorageProperties;

    @Override
    public String saveFile(String base64data, String fileName) {
        try {
            String fullFilename = fileName + ".jpg";

            byte[] imageByte = Base64.decodeBase64(base64data);
            Path uploadPath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

            String directory = uploadPath + "/images/" + fullFilename;
            logger.info("Store file: {}", directory);
            new FileOutputStream(directory).write(imageByte);
            return fullFilename;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file. Please try again!");
        }
    }

    @Override
    public String getImagesPath() {
        return Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize() + "/images/";

    }
}
