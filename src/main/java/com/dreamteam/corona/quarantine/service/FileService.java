package com.dreamteam.corona.quarantine.service;

public interface FileService {

    String saveFile(String base64, String fileName);

    String getImagesPath();
    
}
