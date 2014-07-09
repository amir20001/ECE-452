USE instadj;
DROP SCHEMA instadj;
CREATE SCHEMA instadj;

CREATE TABLE `user` (
	first_name VARCHAR(255),
	last_name VARCHAR(255),
	user_id VARCHAR(255),
	score INT,
	room_id INT NULL,
	PRIMARY KEY (user_id)
);

CREATE TABLE follow (
	id INT AUTO_INCREMENT,
	follwing VARCHAR(255),
	followed VARCHAR(255),
	PRIMARY KEY (id),
	CONSTRAINT fk_following_following FOREIGN KEY (follwing) REFERENCES `user`(user_id),
	CONSTRAINT fk_following_followed FOREIGN KEY (followed) REFERENCES `user`(user_id)
);

CREATE TABLE favourite (
	id INT AUTO_INCREMENT,
	title VARCHAR(255),
	album VARCHAR(255),
	artist VARCHAR(255),
	user_id VARCHAR(255),
	PRIMARY KEY (id),
	CONSTRAINT fk_favourite_user FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);

CREATE TABLE playlist(
	id INT AUTO_INCREMENT,
	`name` VARCHAR(255),
	genre VARCHAR(255),
	track_count INT,
	user_id VARCHAR(255),
	PRIMARY KEY (id),
	CONSTRAINT fk_playlist_user FOREIGN KEY (user_id) REFERENCES `user`(user_id)

); 

CREATE TABLE song(
	id INT AUTO_INCREMENT,
	file_name VARCHAR(255),
	`uuid` VARCHAR(255),
	title VARCHAR(225),
	album VARCHAR(225),
	artist VARCHAR(225),
	duration VARCHAR(225),
	playlist_id INT,
	netScore INT,
	PRIMARY KEY (id),
	CONSTRAINT fk_song_playlist_id FOREIGN KEY (playlist_id) REFERENCES `playlist`(id)
);

CREATE TABLE room (
	 id INT AUTO_INCREMENT,
	 `name` VARCHAR (225),
	 owner_id VARCHAR(255),
	 playlist_id INT NULL,
	 listener_count INT,
	 current_song_id INT NULL,
	 PRIMARY KEY (id),
	 CONSTRAINT fk_room_owner_id FOREIGN KEY (owner_id) REFERENCES `user`(user_id),
	 CONSTRAINT fk_room_current_song_id FOREIGN KEY (current_song_id) REFERENCES song (id)
 );
