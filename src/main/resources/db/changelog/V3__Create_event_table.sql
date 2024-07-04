CREATE TABLE events
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    file_id INT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (file_id) REFERENCES files (id)
);