CREATE TABLE movie (
                       movieID INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(100) UNIQUE NOT NULL,
                       releaseDate DATE,
                       genreID INT NOT NULL,
                       directorID INT NOT NULL,
                       actorID INT NOT NULL,
                       awardID INT NOT NULL,
                       FOREIGN KEY (genreID) REFERENCES genre(genreID),
                       FOREIGN KEY (directorID) REFERENCES director(directorID),
                       FOREIGN KEY (actorID) REFERENCES actor(actorID),
                       FOREIGN KEY (awardID) REFERENCES award(awardID)
);

CREATE TABLE genre(
                      genreID INT AUTO_INCREMENT PRIMARY KEY,
                      genreName VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE director(
                         directorID INT AUTO_INCREMENT PRIMARY KEY,
                         directorName VARCHAR(100) NOT NULL,
                         birthdate DATE
);

CREATE TABLE actor(
                      actorID INT AUTO_INCREMENT PRIMARY KEY,
                      actorName VARCHAR(100),
                      birthdate DATE
);

CREATE TABLE award(
                      awardID INT AUTO_INCREMENT PRIMARY KEY,
                      awardName VARCHAR(100) UNIQUE
);
