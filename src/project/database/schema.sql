DROP TABLE IF EXISTS task_task;
DROP TABLE IF EXISTS task_study_session;
DROP TABLE IF EXISTS task_gaming_session;
DROP TABLE IF EXISTS task_exercise_session;
DROP TABLE IF EXISTS task_shopping_session;
DROP TABLE IF EXISTS shopping_session_shopping_list;
DROP TABLE IF EXISTS exercise_session_exercises;
DROP TABLE IF EXISTS study_session_subjects;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS exercises;
DROP TABLE IF EXISTS exercise_session;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS study_session;
DROP TABLE IF EXISTS shopping_list;
DROP TABLE IF EXISTS gaming_session;
DROP TABLE IF EXISTS shopping_session;

--TASK:
CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    title VARCHAR(30) NOT NULL,
    priority INT default 0
);

--STUDY SESSION:
CREATE TABLE study_session (
    id SERIAL PRIMARY KEY,
    title VARCHAR(30) NOT NULL,
    duration INT NOT NULL
);

CREATE TABLE subjects (
    name VARCHAR(30) PRIMARY KEY
);

CREATE TABLE study_session_subjects (
    study_session_id SERIAL references study_session(id),
    subject_name VARCHAR(30) references subjects(name),
    PRIMARY KEY (study_session_id, subject_name)
);

--SHOPPING SESSION:
CREATE TABLE shopping_session (
    id SERIAL PRIMARY KEY ,
    title VARCHAR(30) NOT NULL,
    duration INT NOT NULL,
    max_budget INT NOT NULL,
    place VARCHAR(30)
);

CREATE TABLE shopping_list (
    item VARCHAR(30),
    quantity INT,
    PRIMARY KEY (item, quantity)
);

CREATE TABLE shopping_session_shopping_list (
    shopping_session_id SERIAL references shopping_session(id),
    item_name VARCHAR(30),
    item_quantity INT,
    PRIMARY KEY (shopping_session_id, item_name, item_quantity),
    FOREIGN KEY (item_name, item_quantity) REFERENCES shopping_list(item, quantity)
);

--EXERCISE SESSION:
CREATE TABLE exercise_session (
    id SERIAL PRIMARY KEY,
    title VARCHAR(30) NOT NULL
);

CREATE TABLE exercises (
    name VARCHAR(30),
    duration int,
    PRIMARY KEY (name, duration)
);

CREATE TABLE exercise_session_exercises (
    exercise_session_id SERIAL references exercise_session(id),
    exercise_name VARCHAR(30),
    exercise_duration INT,
    PRIMARY KEY (exercise_session_id, exercise_name, exercise_duration),
    FOREIGN KEY (exercise_name, exercise_duration) REFERENCES exercises(name, duration)
);

--GAMING SESSION:
CREATE TABLE gaming_session (
    id SERIAL PRIMARY KEY,
    title VARCHAR(30) NOT NULL,
    duration INT NOT NULL,
    multiplayer boolean,
    platform VARCHAR(30)
);

-- ASSOCIATIONS:
CREATE TABLE task_study_session (
    task_id SERIAL references task(id),
    study_session_id SERIAL references study_session(id),
    PRIMARY KEY (task_id, study_session_id)
);

CREATE TABLE task_shopping_session (
    task_id SERIAL references task(id),
    shopping_session_id SERIAL references shopping_session(id),
    PRIMARY KEY (task_id, shopping_session_id)
);

CREATE TABLE task_exercise_session (
    task_id SERIAL references task(id),
    exercise_session_id SERIAL references exercise_session(id),
    PRIMARY KEY (task_id, exercise_session_id)
);
CREATE TABLE task_gaming_session (
    task_id SERIAL references task(id),
    gaming_session_id SERIAL references gaming_session(id),
    PRIMARY KEY (task_id, gaming_session_id)
);

CREATE TABLE task_task (
    task_id SERIAL references task(id),
    task_id2 SERIAL references task(id),
    PRIMARY KEY (task_id, task_id2)
);