package com.ece452.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import org.springframework.stereotype.Repository;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

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

	public static String getTitle(Mp3File mp3file) {
		if (mp3file.hasId3v1Tag()) {
			return mp3file.getId3v1Tag().getTitle();
		} else if (mp3file.hasId3v2Tag()) {
			return mp3file.getId3v2Tag().getTitle();
		} else
			return "";
	}

	public static String getAlbum(Mp3File mp3file) {
		if (mp3file.hasId3v1Tag()) {
			return mp3file.getId3v1Tag().getAlbum();
		} else if (mp3file.hasId3v2Tag()) {
			return mp3file.getId3v2Tag().getAlbum();
		} else
			return "";
	}

	public static String getArtist(Mp3File mp3file) {
		if (mp3file.hasId3v1Tag()) {
			return mp3file.getId3v1Tag().getArtist();
		} else if (mp3file.hasId3v2Tag()) {
			return mp3file.getId3v2Tag().getArtist();
		} else
			return "";
	}

	public static String secToMin(long sec) {
		long min = sec / 60;
		sec = sec - min * 60;

		return min + ":" + sec;
	}

	public static File getAlbumArt(Mp3File mp3file) throws MimeTypeParseException, IOException {
		if (mp3file.hasId3v2Tag()) {
			ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			byte[] imageData = mp3file.getId3v2Tag().getAlbumImage();
			if (imageData != null) {
				String albumImageMimeType = id3v2Tag.getAlbumImageMimeType();
				MimeType mime = new MimeType(albumImageMimeType);
				String uuid = UUID.randomUUID().toString();
				File file = File.createTempFile(uuid, mime.getSubType());
				return file;
			}
			return null;
		} else
			return null;
	}

}
