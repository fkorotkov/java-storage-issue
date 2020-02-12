package com.fkorotkov.storage;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.testing.RemoteStorageHelper;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class App {
  public static void main(String[] args) throws Throwable {
    String PROJECT_ID = "";
    String CREDENTIAL_PATH = "";

    RemoteStorageHelper helper =
      RemoteStorageHelper.create(PROJECT_ID, new FileInputStream(CREDENTIAL_PATH));

    Storage storage = helper.getOptions().getService();
    String bucketName = RemoteStorageHelper.generateBucketName();
    storage.create(BucketInfo.of(bucketName));

    System.out.println("Created bucket " + bucketName);

    String blobPrefix = "foo";
    String blobName = "'bar'.txt";
    String fullBlobPath = blobPrefix + "/" + blobName;

    WriteChannel writer = storage.writer(BlobInfo.newBuilder(bucketName, fullBlobPath).build());
    writer.write(ByteBuffer.wrap("Hello, issue!".getBytes()));
    writer.close();
    System.out.println("Successfully created " + fullBlobPath);

    System.out.println("All files under " + blobPrefix + ":");
    for (Blob blob : storage.list(bucketName, Storage.BlobListOption.prefix(blobPrefix)).iterateAll()) {
      System.out.println(blob.getName() + " " + blob.getSize() + " bytes");
    }

    try {
      byte[] bytes = storage.readAllBytes(bucketName, fullBlobPath);
      System.out.println("Content of " + fullBlobPath + ":");
      System.out.println(new String(bytes));
    } catch (Throwable e) {
      System.out.println("Failed to read " + blobName + ": " + e.getMessage());
    }

    RemoteStorageHelper.forceDelete(storage, bucketName, 5, TimeUnit.SECONDS);
  }
}
