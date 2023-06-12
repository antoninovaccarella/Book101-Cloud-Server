package com.antonino.book101server.services;

import com.antonino.book101server.exceptions.GCPFileUploadException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;

@Service
public class GoogleCloudStorageService {
    public void uploadFile(String stringData, String fileName) {

        try{
            byte[] fileData = Base64.getDecoder().decode(stringData.getBytes("UTF-8"));

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
    public String downloadFile(String fileName) throws IOException {
        InputStream inputStream = new ClassPathResource("book101-cloud-6aa187256a0a.json").getInputStream();
        StorageOptions options = StorageOptions.newBuilder().setProjectId("Book101-Cloud")
                .setCredentials(GoogleCredentials.fromStream(inputStream)).build();

        Storage storage = options.getService();

        Blob blob = storage.get("book101-storage", fileName);


        return Base64.getEncoder().encodeToString(blob.getContent());

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
