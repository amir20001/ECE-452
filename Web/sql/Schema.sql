USE instadj;
DROP SCHEMA instadj;
CREATE SCHEMA instadj;

CREATE TABLE `user` (
	first_name VARCHAR(255),
	last_name VARCHAR(255),
	user_id VARCHAR(255),
	score INT,
	room_id INT NULL,
	gcm_id VARCHAR(255) NULL,
	PRIMARY KEY (user_id)
);

CREATE TABLE follow (
	id INT AUTO_INCREMENT,
	follower VARCHAR(255),
	followee VARCHAR(255),
	PRIMARY KEY (id),
	CONSTRAINT fk_following_following FOREIGN KEY (follower) REFERENCES `user`(user_id),
	CONSTRAINT fk_following_followed FOREIGN KEY (followee) REFERENCES `user`(user_id),
	CONSTRAINT uc_followe_followee UNIQUE (follower,followee)

);

CREATE TABLE favourite (
	id INT AUTO_INCREMENT,
	user_id VARCHAR(255),
	song_id  int,
	PRIMARY KEY (id),
	CONSTRAINT fk_favourite_user FOREIGN KEY (user_id) REFERENCES `user`(user_id),
	CONSTRAINT fk_favourite_song FOREIGN KEY (song_id) REFERENCES `song`(id)

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
	net_score INT,
	song_url  VARCHAR(2083),
	song_uri  VARCHAR(2083),
	art_url  VARCHAR(2083),
	PRIMARY KEY (id)
	);

CREATE TABLE room (
	 id INT AUTO_INCREMENT,
	 `name` VARCHAR (225),
	 owner_user_id VARCHAR(255),
	 playlist_id INT NULL,
	 listener_count INT,
	 current_song_id INT NULL,
	 song_position INT,
	 song_is_playing boolean,
	 song_play_start_time BIGINT,	 
	 PRIMARY KEY (id),
	 CONSTRAINT fk_room_owner_id FOREIGN KEY (owner_user_id) REFERENCES `user`(user_id),
	 CONSTRAINT fk_room_current_song_id FOREIGN KEY (current_song_id) REFERENCES song (id)
 );
