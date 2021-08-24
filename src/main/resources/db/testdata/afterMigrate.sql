-- disable foreign key check
set foreign_key_checks = 0;
truncate table city;
truncate table cuisine;
truncate table state;
truncate table payment_method;
truncate table permission;
truncate table product;
truncate table restaurant;
truncate table user;
truncate table user_group;
truncate table restaurant_payment_method;
truncate table user_group_user;
truncate table user_group_permission;
-- enable foreign key check
set foreign_key_checks = 1;

insert into cuisine(id, name, created_at) values (1, "Mineira", utc_timestamp);
insert into cuisine(id, name, created_at) values (2, "Baiana", utc_timestamp);
insert into cuisine(id, name, created_at) values (3, "Italiana", utc_timestamp);
insert into cuisine(id, name, created_at) values (4, "Tailandesa", utc_timestamp);
insert into cuisine(id, name, created_at) values (5, "Japonesa", utc_timestamp);

insert into state (id, name, abbreviation, created_at) values (1, "Minas Gerais", "MG", utc_timestamp);
insert into state (id, name, abbreviation, created_at) values (2, "São Paulo", "SP", utc_timestamp);
insert into state (id, name, abbreviation, created_at) values (3, "Ceará", "CE", utc_timestamp);

insert into city (id, name, state_id, created_at) values (1, "Uberlândia", 1, utc_timestamp);
insert into city (id, name, state_id, created_at) values (2, "Belo Horizonte", 1, utc_timestamp);
insert into city (id, name, state_id, created_at) values (3, "São Paulo", 2, utc_timestamp);
insert into city (id, name, state_id, created_at) values (4, "Campinas", 2, utc_timestamp);
insert into city (id, name, state_id, created_at) values (5, "Fortaleza", 3, utc_timestamp);

insert into restaurant(id, name, delivery_fee, cuisine_id,
        address_name, address_number, address_complement, address_neighborhood, address_city_id, created_at)
    values (1, "Maktubs", 2.50, 2, "Rua Teste 1", "1", "Complemento 1", "Bairro 1", 1, utc_timestamp);
insert into restaurant(id, name, delivery_fee, cuisine_id,
        address_name, address_number, address_complement, address_neighborhood, address_city_id, created_at)
    values (2, "Limax", 2.00, 2, "Rua Teste 2", "2", "Complemento 2", "Bairro 2", 1, utc_timestamp);
insert into restaurant(id, name, delivery_fee, cuisine_id,
        address_name, address_number, address_complement, address_neighborhood, address_city_id, created_at)
    values (3, "Espaço Grill", 0, 3, "Rua Teste 3", "3", "Complemento 3", "Bairro 3", 2, utc_timestamp);
insert into restaurant(id, name, delivery_fee, cuisine_id,
        address_name, address_number, address_complement, address_neighborhood, address_city_id, created_at)
    values (4, "Churrascaria Km 7", 5.00, 4, "Rua Teste 4", "4", "Complemento 4", "Bairro 4", 3, utc_timestamp);

insert into payment_method (id, description, created_at) values (1, "Cartão de Crédito", utc_timestamp);
insert into payment_method (id, description, created_at) values (2, "Cartão de Débito", utc_timestamp);
insert into payment_method (id, description, created_at) values (3, "Dinheiro", utc_timestamp);

insert into permission (id, name, description, created_at)
    values (1, "CONSULTAR_COZINHAS", "Permite consultar cozinhas", utc_timestamp);
insert into permission (id, name, description, created_at)
    values (2, "EDITAR_COZINHAS", "Permite editar cozinhas", utc_timestamp);

insert into restaurant_payment_method (restaurant_id, payment_method_id)
    values (1, 1), (1, 2), (1, 3), (2, 3), (3, 2), (3, 3);

insert into product(id, name, description, price, restaurant_id, created_at)
    values (1, "Açaí", "Açaí + Complementos", 14.0, 1, utc_timestamp);
insert into product(id, name, description, price, restaurant_id, created_at)
    values (2, "Esfirra", "Esfirra de frango Catupiry", 2.5, 1, utc_timestamp);
insert into product(id, name, description, price, restaurant_id, created_at)
    values (3, "Bauru", "Bauru de Pizza", 3.0, 2, utc_timestamp);
insert into product(id, name, description, price, restaurant_id, created_at)
    values (4, "Shake", "Shake Mix Crocomenta c/ Negresco", 16.0, 3, utc_timestamp);
insert into product(id, name, description, price, restaurant_id, created_at)
    values (5, "Picanha", "Picanha ao molho Mostrada", 16.0, 4, utc_timestamp);

