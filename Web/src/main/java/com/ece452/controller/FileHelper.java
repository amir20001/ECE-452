package com.ece452.controller;

import java.io.File;

import org.springframework.stereotype.Repository;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class FileHelper {

	public static final String bucket = "instadj";

	private AmazonS3 s3;

	public FileHelper() {
		s3 = new AmazonS3Client(new BasicAWSCredentials(
				"AKIAIFO2AOLHPJYFFBVA",
				"CbQk+Yq1OHTpmN6bgtF0ewrgZKfxW+ayJh6HlM7S"));
	}

	public void upload(File file, String key) {
		try {
			s3.putObject(new PutObjectRequest("instadj", key, file));
		} catch (AmazonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AmazonClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
