USE instadj;
DROP SCHEMA instadj;
CREATE SCHEMA instadj;

CREATE TABLE user (
  username VARCHAR(255),
  picture_url VARCHAR (255),
 PRIMARY KEY (username)
);

CREATE TABLE playlist(
	id INT AUTO_INCREMENT,
	`name` VARCHAR(255),
	genre VARCHAR(255),
	track_count INT,
	PRIMARY KEY (id)
); 

CREATE TABLE song(
	id INT AUTO_INCREMENT,
	file_name VARCHAR(255),
	`uuid` VARCHAR(255),
	title VARCHAR(225),
	artist VARCHAR(225),
	duration VARCHAR(225),
	playlist_id INT,
	PRIMARY KEY (id)
	-- add constraint to playlist after demo
);

CREATE TABLE room (
 id INT AUTO_INCREMENT,
 `name` VARCHAR (225),
 owner_user_name VARCHAR(255),
 playlist_id INT NULL,
 listenerCount INT,
 current_song_id INT NULL,
 PRIMARY KEY (id),
 CONSTRAINT fk_room_owner_user_name FOREIGN KEY (owner_user_name) REFERENCES `user`(username),
 CONSTRAINT fk_room_current_song_id FOREIGN KEY (current_song_id) REFERENCES song (id),
 CONSTRAINT fk_room_playlist FOREIGN KEY (playlist_id) REFERENCES playlist (id)
);
