CREATE TABLE student(
    rollnum TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    age INTEGER,
    major TEXT NOT NULL,
    gpa DECIMAL(4,2) NOT NULL,
    email TEXT NOT NULL UNIQUE
);

CREATE TABLE company(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    location TEXT,
    industry TEXT,
    hrcontact TEXT NOT NULL UNIQUE

);

CREATE Table drive(
    D_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    companyId INTEGER NOT NULL,
    start_date DATE not null,
    end_date DATE not null,
    availableSeats int NOT NULL,
    lpa DECIMAL(10,2) not null,
    mingpa DECIMAL(4,2) not null,
    FOREIGN KEY (companyId) REFERENCES company(id)
);

CREATE TABLE applications(
    A_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    driveId INTEGER NOT NULL,
    s_id TEXT NOT NULL,
    applicationDate date NOT NULL,
    status TEXT NOT NULL,
    FOREIGN KEY (driveId) REFERENCES drive(D_id),
    FOREIGN KEY (s_id) REFERENCES student(rollnum),
    CHECK (status IN ('Applied', 'Shortlisted', 'Rejected', 'Accepted'))
);