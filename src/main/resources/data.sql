CREATE TABLE movie (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       releaseDate DATE NOT NULL,
                       stars VARCHAR(1000) NOT NULL,
                       CONSTRAINT unique_title_releaseDate UNIQUE (title, releaseDate)
);

CREATE TABLE star (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL
);

CREATE TABLE movie_star (
                            movie_id INT NOT NULL,
                            star_id INT NOT NULL,
                            PRIMARY KEY (movie_id, star_id),
                            CONSTRAINT fk_movie_star_movie FOREIGN KEY (movie_id) REFERENCES movie (id) ON DELETE CASCADE,
                            CONSTRAINT fk_movie_star_star FOREIGN KEY (star_id) REFERENCES star (id) ON DELETE CASCADE,
                            CONSTRAINT unique_movie_star UNIQUE (movie_id, star_id)
);


