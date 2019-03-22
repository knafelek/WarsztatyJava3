CREATE TABLE users_groups (
	id INT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE unique_name (name)
    );

CREATE TABLE users (
	id INT AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    user_group_id INT NOT NULL,
    UNIQUE unique_username (username),
    UNIQUE unique_email (email),
    PRIMARY KEY (id),
    FOREIGN KEY (user_group_id) REFERENCES users_groups(id)
    );

CREATE TABLE exercises (
	id INT AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    UNIQUE unique_title (title),
    PRIMARY KEY (id)
    );

CREATE TABLE solutions (
	id INT AUTO_INCREMENT,
    created TIMESTAMP,
    updated TIMESTAMP,
    description TEXT,
    user_id INT NOT NULL,
    exercise_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (exercise_id) REFERENCES exercises(id)
    );