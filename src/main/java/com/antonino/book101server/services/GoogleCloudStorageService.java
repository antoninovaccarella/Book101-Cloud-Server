package com.antonino.book101server.services;

import com.antonino.book101server.exceptions.GCPFileUploadException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

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
            InputStream inputStream = new ClassPathResource("book101-cloud-6aa187256a0a.json").getInputStream();
            StorageOptions options = StorageOptions.newBuilder().setProjectId("Book101-Cloud")
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
            Storage storage = options.getService();
            Bucket bucket = storage.get("book101-storage", Storage.BucketGetOption.fields());
            Blob blob = bucket.create(fileName, fileData, "application/octet-stream");
        } catch (Exception e) {
            throw new GCPFileUploadException("An error occurred while storing data to GCS", e);
        }
    }

    public String downloadAnteprima(String fileName) {
        try {
            InputStream inputStream = new ClassPathResource("book101-cloud-6aa187256a0a.json").getInputStream();
            StorageOptions options = StorageOptions.newBuilder().setProjectId("Book101-Cloud")
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
            Storage storage = options.getService();
            Blob blob = storage.get("book101-storage", fileName);
            System.out.println(fileName);
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(blob.getContent(Blob.BlobSourceOption.generationMatch()));        } catch (Exception e) {
            throw new GCPFileUploadException("An error occurred while storing data to GCS", e);
        }

    }
    public String downloadPDF(String fileName) {
        try {
            InputStream inputStream = new ClassPathResource("book101-cloud-6aa187256a0a.json").getInputStream();
            StorageOptions options = StorageOptions.newBuilder().setProjectId("Book101-Cloud")
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
            Storage storage = options.getService();
            Blob blob = storage.get("book101-storage", fileName);
            System.out.println(fileName);
            return "data:application/octet-stream;base64," + Base64.getEncoder().encodeToString(blob.getContent(Blob.BlobSourceOption.generationMatch()));        } catch (Exception e) {
            throw new GCPFileUploadException("An error occurred while storing data to GCS", e);
        }

}
