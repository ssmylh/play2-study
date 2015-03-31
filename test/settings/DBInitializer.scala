package settings

import scalikejdbc._

object DBInitializer {
  def run(dbName: Symbol): Unit = {
    NamedDB(dbName) readOnly { implicit session =>
      try {
        sql"select 1 from class limit 1".map(_.long(1)).single.apply()
      } catch {
        case e: java.sql.SQLException =>
          NamedDB(dbName) autoCommit { implicit session =>
            sql"""
create sequence class_id_seq start with 1;
create table class (
  id bigint not null default nextval('class_id_seq') primary key,
  grade int not null,
  name varchar(255) not null
);

create sequence student_id_seq start with 1;
create table student (
  id bigint not null default nextval('student_id_seq') primary key,
  last_name varchar(255) not null,
  first_name varchar(255) not null,
  kana varchar(255) not null
);

create table class2student (
  class_id bigint not null,
  student_id bigint not null
);
alter table class2student add primary key (class_id, student_id);
""".execute.apply()
          }
      }
    }
  }
}