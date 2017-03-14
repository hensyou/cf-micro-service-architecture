create sequence hibernate_sequence;

create table product (
  id serial primary key,
  name varchar(100),
  description varchar(100),
  price decimal
)
