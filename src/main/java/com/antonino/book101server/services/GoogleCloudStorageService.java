package com.antonino.book101server.services;

import com.antonino.book101server.exceptions.GCPFileUploadException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;

import java.io.*;

@Service
public class GoogleCloudStorageService {
    public void uploadFile(byte[] fileData, String fileName) {

        try{

            InputStream inputStream = new ClassPathResource("book101-cloud-6aa187256a0a.json").getInputStream();

            StorageOptions options = StorageOptions.newBuilder().setProjectId("Book101-Cloud")
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();

            Storage storage = options.getService();
            Bucket bucket = storage.get("book101-storage",Storage.BucketGetOption.fields());

            Blob blob = bucket.create(fileName, fileData, "application/octet-stream");

        }catch (Exception e){
            throw new GCPFileUploadException("An error occurred while storing data to GCS");
        }
        throw new GCPFileUploadException("An error occurred while storing data to GCS");
    }
    public ByteArrayResource downloadFile(String fileName) throws IOException {
        InputStream inputStream = new ClassPathResource("book101-cloud-6aa187256a0a.json").getInputStream();
        StorageOptions options = StorageOptions.newBuilder().setProjectId("Book101-Cloud")
                .setCredentials(GoogleCredentials.fromStream(inputStream)).build();

        Storage storage = options.getService();

        Blob blob = storage.get("book101-storage", fileName);
        ByteArrayResource resource = new ByteArrayResource(
                blob.getContent());

        return resource;
    }


    private String checkFileExtension(String fileName) {
        if(fileName != null && fileName.contains(".")){
            String[] extensionList = {".png", ".jpeg", ".pdf"};

            for(String extension: extensionList) {
                if (fileName.endsWith(extension)) {
                    return extension;
                }
            }
        }
        throw new RuntimeException("Not a permitted file type");
    }
}
