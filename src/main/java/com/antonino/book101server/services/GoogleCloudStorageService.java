package com.antonino.book101server.services;

import com.antonino.book101server.exceptions.GCPFileUploadException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;

@Service
public class GoogleCloudStorageService {

    Logger LOGGER = LogManager.getLogger(GoogleCloudStorageService.class);
    public void uploadFile(String stringData, String fileName) {

        try {
            String base64Image = stringData.split(",")[1];
            byte[] fileData = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            LOGGER.info("fileData created");
            InputStream inputStream = new ClassPathResource("book101-cloud-6aa187256a0a.json").getInputStream();
            LOGGER.info("input stream initialized");
            StorageOptions options = StorageOptions.newBuilder().setProjectId("Book101-Cloud")
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
            LOGGER.info("storage options created");
            Storage storage = options.getService();
            LOGGER.info("storage instanciated");
            Bucket bucket = storage.get("book101-storage", Storage.BucketGetOption.fields());
            LOGGER.info("bucket instanciated");
            Blob blob = bucket.create(fileName, fileData, "application/octet-stream");
            LOGGER.info("blob created");
        } catch (Exception e) {
            throw new GCPFileUploadException("An error occurred while storing data to GCS", e);
        }
    }

    public String downloadFile(String fileName) {
        try {
            InputStream inputStream = new ClassPathResource("book101-cloud-6aa187256a0a.json").getInputStream();
            LOGGER.info("input stream initialized");
            StorageOptions options = StorageOptions.newBuilder().setProjectId("Book101-Cloud")
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
            LOGGER.info("storage options created");
            Storage storage = options.getService();
            LOGGER.info("storage instanciated");
            Blob blob = storage.get(BlobId.of("book101-storage", fileName));
            LOGGER.info("blob created");
            return Base64.getEncoder().encodeToString(blob.getContent());

        } catch (Exception e) {
            throw new GCPFileUploadException("An error occurred while storing data to GCS", e);
        }

    }

}
