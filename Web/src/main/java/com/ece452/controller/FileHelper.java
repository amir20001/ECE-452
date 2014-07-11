package com.ece452.controller;

import java.io.File;

import org.springframework.stereotype.Repository;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class FileHelper {

	private AmazonS3 s3;
	public static final String bucket = "instadj";

	public FileHelper() {
		s3 = new AmazonS3Client(new BasicAWSCredentials("AKIAIFO2AOLHPJYFFBVA",
				"CbQk+Yq1OHTpmN6bgtF0ewrgZKfxW+ayJh6HlM7S"));
	}

	public String upload(File file, String key) {
		try {
			s3.putObject(new PutObjectRequest(bucket, key, file));
			return "https://s3.amazonaws.com/instadj/" + key;
		} catch (Exception e) {
			e.printStackTrace();
			return "";

		}
	}

}
