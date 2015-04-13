drop table if exists class;
create sequence class_id_seq start with 1;
create table class (
  id bigint not null default nextval('class_id_seq') primary key,
  grade int not null,
  name varchar(255) not null
);
alter table class add unique key (grade, name);

drop table if exists student;
create sequence student_id_seq start with 1;
create table student (
  id bigint not null default nextval('student_id_seq') primary key,
  last_name varchar(255) not null,
  first_name varchar(255) not null,
  kana varchar(255) not null
);

drop table if exists class2student;
create table class2student (
  class_id bigint not null,
  student_id bigint not null
);
alter table class2student add primary key (class_id, student_id);

insert into class (grade, name) values (1, '1');
insert into class (grade, name) values (2, '1');
insert into class (grade, name) values (3, '1');

insert into student (last_name, first_name, kana) values ('梅沢', 'めぐみ', 'うめざわめぐみ');
insert into student (last_name, first_name, kana) values ('平賀', 'メイサ', 'ひらがめいさ');
insert into student (last_name, first_name, kana) values ('とよた', '由樹', 'とよたゆき');
insert into student (last_name, first_name, kana) values ('水嶋', 'ジローラモ', 'みずしまじろーらも');
insert into student (last_name, first_name, kana) values ('船橋', '裕司', 'ふなばしゆうじ');
insert into student (last_name, first_name, kana) values ('神崎', '恭子', 'かんざききょうこ');
insert into student (last_name, first_name, kana) values ('川井', 'なつみ', 'かわいなつみ');
insert into student (last_name, first_name, kana) values ('多田', '有起哉', 'ただゆきや');
insert into student (last_name, first_name, kana) values ('大和', '隆之介', 'やまとりゅうのすけ');
insert into student (last_name, first_name, kana) values ('梅村', '由宇', 'うめむらゆう');

insert into student (last_name, first_name, kana) values ('山川', 'ひろみ', 'やまかわひろみ');
insert into student (last_name, first_name, kana) values ('荒川', '妃里', 'あらかわゆり');
insert into student (last_name, first_name, kana) values ('今田', '沙耶', 'いまださや');
insert into student (last_name, first_name, kana) values ('小泉', '美菜', 'こいずみみな');
insert into student (last_name, first_name, kana) values ('三宅', '育二', 'みやけいくじ');
insert into student (last_name, first_name, kana) values ('岩田', 'そら', 'いわたそら');
insert into student (last_name, first_name, kana) values ('牧', '拓郎', 'まきたくろう');
insert into student (last_name, first_name, kana) values ('堀', '勤', 'ほりつとむ');
insert into student (last_name, first_name, kana) values ('岡島', '雅彦', 'おかじままさひこ');
insert into student (last_name, first_name, kana) values ('片桐', '聡', 'かたぎりさとし');

insert into student (last_name, first_name, kana) values ('福原', 'さんま', 'ふくはらさんま');
insert into student (last_name, first_name, kana) values ('服部', 'みき', 'はっとりみき');
insert into student (last_name, first_name, kana) values ('服部', '薫', 'はっとりかおる');
insert into student (last_name, first_name, kana) values ('熊倉', '恵麻', 'くまくらえま');
insert into student (last_name, first_name, kana) values ('上原', '洋介', 'うえはらようすけ');
insert into student (last_name, first_name, kana) values ('草村', '咲', 'くさむらさき');
insert into student (last_name, first_name, kana) values ('大坪', '光洋', 'おおつぼみつひろ');
insert into student (last_name, first_name, kana) values ('竹内', '隼士', 'たけうちしゅんじ');
insert into student (last_name, first_name, kana) values ('池本', '恵梨香', 'いけもとえりか');
insert into student (last_name, first_name, kana) values ('深田', 'フミヤ', 'ふかだふみや');

-- 1年1組
insert into class2student (class_id, student_id) values (1, 1);
insert into class2student (class_id, student_id) values (1, 2);
insert into class2student (class_id, student_id) values (1, 3);
insert into class2student (class_id, student_id) values (1, 4);
insert into class2student (class_id, student_id) values (1, 5);
insert into class2student (class_id, student_id) values (1, 6);
insert into class2student (class_id, student_id) values (1, 7);
insert into class2student (class_id, student_id) values (1, 8);
insert into class2student (class_id, student_id) values (1, 9);
insert into class2student (class_id, student_id) values (1, 10);

-- 2年1組
insert into class2student (class_id, student_id) values (2, 11);
insert into class2student (class_id, student_id) values (2, 12);
insert into class2student (class_id, student_id) values (2, 13);
insert into class2student (class_id, student_id) values (2, 14);
insert into class2student (class_id, student_id) values (2, 15);
insert into class2student (class_id, student_id) values (2, 16);
insert into class2student (class_id, student_id) values (2, 17);
insert into class2student (class_id, student_id) values (2, 18);
insert into class2student (class_id, student_id) values (2, 19);
insert into class2student (class_id, student_id) values (2, 20);

-- 3年1組
insert into class2student (class_id, student_id) values (3, 21);
insert into class2student (class_id, student_id) values (3, 22);
insert into class2student (class_id, student_id) values (3, 23);
insert into class2student (class_id, student_id) values (3, 24);
insert into class2student (class_id, student_id) values (3, 25);
insert into class2student (class_id, student_id) values (3, 26);
insert into class2student (class_id, student_id) values (3, 27);
insert into class2student (class_id, student_id) values (3, 28);
insert into class2student (class_id, student_id) values (3, 29);
insert into class2student (class_id, student_id) values (3, 30);
