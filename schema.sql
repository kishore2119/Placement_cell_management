CREATE TABLE student (
    rollnum VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER,
    major VARCHAR(100) NOT NULL,
    gpa DECIMAL(4, 2) NOT NULL,
    password VARCHAR(150) NOT NULL
);

CREATE TABLE company (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL UNIQUE,
    location VARCHAR(200),
    industry VARCHAR(100),
    hrcontact VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE drive (
    D_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    companyId INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    availableSeats INT NOT NULL,
    lpa DECIMAL(10, 2) NOT NULL,
    mingpa DECIMAL(4, 2) NOT NULL,
    FOREIGN KEY (companyId) REFERENCES company (id)
);

CREATE TABLE applications (
    A_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    driveId INTEGER NOT NULL,
    s_id VARCHAR(50) NOT NULL,
    applicationDate DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (driveId) REFERENCES drive (D_id),
    FOREIGN KEY (s_id) REFERENCES student (rollnum),
    CHECK (
        status IN (
            'Applied',
            'Shortlisted',
            'Rejected',
            'Accepted'
        )
    )
);