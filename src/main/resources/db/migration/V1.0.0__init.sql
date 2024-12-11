CREATE TABLE student (
  id     bigint NOT NULL,
  name   text NOT NULL,
  status INT NOT NULL,
  ext    text
);
insert into student(id, name, status) values
(1, 'UserA', 0), (2, 'UserB', 1),
(3, 'UserC', 2), (4, 'UserD', 2),
(5, 'UserE', 2);