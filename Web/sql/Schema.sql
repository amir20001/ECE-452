USE ece452;
DROP SCHEMA ece452;
CREATE SCHEMA ece452;

CREATE TABLE user (
  username VARCHAR(255),
  picture_url VARCHAR (255),
 PRIMARY KEY (username)
);

CREATE TABLE song(
	id INT AUTO_INCREMENT,
	file_name VARCHAR(255),
	`uuid` VARCHAR(255),
	title VARCHAR(225),
	artist VARCHAR(225),
	duration VARCHAR(225),
	room_id INT,
	PRIMARY KEY (id)
);

CREATE TABLE room (
 id INT AUTO_INCREMENT,
 `name` VARCHAR (225),
 owner_user_name VARCHAR(255),
 listenerCount INT,
 current_song_id INT NULL,
 PRIMARY KEY (id),
 CONSTRAINT fk_room_owner_user_name FOREIGN KEY (owner_user_name) REFERENCES `user`(username),
 CONSTRAINT fk_room_current_song_id FOREIGN KEY (current_song_id) REFERENCES song (id)
);

ALTER TABLE song ADD CONSTRAINT fk_song_room_id FOREIGN KEY (room_id) REFERENCES room(id);
