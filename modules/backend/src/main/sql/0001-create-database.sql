create schema if not exists `topfirst`
  default character set = `utf8`;

use `topfirst`;

create table if not exists `test`
(
  `test_id`   int(10) unsigned not null auto_increment,
  `c_int`     int not null default 0,
  `c_float`   float,
  `c_varchar` varchar(100),
  `c_text`    text,
  primary key (`test_id`)
);

insert into `test`
            (c_int, c_float, c_varchar, c_text)
     values (    1,   1.123, 'Test-1' , 'Test table, test data');

insert into `test`
            (c_int, c_float, c_varchar, c_text)
     values (    2,   2.456, 'Test-2' , 'Test table, test data');

insert into `test`
            (c_int, c_float, c_varchar, c_text)
     values (    3,   3.789, 'Test-3' , 'Test table, test data');

create user 'topfirst'@'localhost' identified by 'topfirst';

grant all on `topfirst`.* to 'topfirst' identified by 'topfirst' with grant option;
