CREATE TABLE task (
   id INT NOT NULL,
   task_title varchar(250) NOT NULL,
   started_on timestamp NOT NULL,
   completed_on timestamp NOT NULL,
   time_set varchar(10) NOT NULL,
   PRIMARY KEY (id)
);