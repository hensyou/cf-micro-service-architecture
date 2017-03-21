create sequence if not exists hibernate_sequence;

create table if not exists product(
 id serial primary key,
 name varchar(100) UNIQUE,
 description varchar(100),
 price decimal,
 quantity int
);

insert into product
 (name, description, price, quantity)
 VALUES ('Black Coffee with sugar', 'Borrrinnnng', 1.50, 15),
 ('Coffee with milk and sugar', 'Lactose', 2.50, 10),
 ('Cappuccino', 'Double espresso, hot milk, and steamed milk foam.', 3.25, 10);

