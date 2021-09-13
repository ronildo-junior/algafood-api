-- disable foreign key check
set foreign_key_checks = 0;
truncate table `city`;
truncate table `cuisine`;
truncate table `state`;
truncate table `payment_method`;
truncate table `permission`;
truncate table `product`;
truncate table `product_photo`;
truncate table `restaurant`;
truncate table `user`;
truncate table `user_group`;
truncate table `restaurant_payment_method`;
truncate table `user_group_user`;
truncate table `user_group_permission`;
truncate table `restaurant_manager`;
truncate table `order`;
truncate table `order_item`;
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

insert into product(id, name, description, price, restaurant_id, created_at, active)
    values (1, "Açaí", "Açaí + Complementos", 14.0, 1, utc_timestamp, 0);
insert into product(id, name, description, price, restaurant_id, created_at)
    values (2, "Esfirra", "Esfirra de frango Catupiry", 2.5, 1, utc_timestamp);
insert into product(id, name, description, price, restaurant_id, created_at)
    values (3, "Bauru", "Bauru de Pizza", 3.0, 2, utc_timestamp);
insert into product(id, name, description, price, restaurant_id, created_at)
    values (4, "Shake", "Shake Mix Crocomenta c/ Negresco", 16.0, 3, utc_timestamp);
insert into product(id, name, description, price, restaurant_id, created_at)
    values (5, "Picanha", "Picanha ao molho Mostrada", 16.0, 4, utc_timestamp);
insert into user_group (id, name, created_at) values
    (1, 'Gerente', utc_timestamp), (2, 'Vendedor', utc_timestamp), (3, 'Secretária', utc_timestamp),
    (4, 'Cadastrador', utc_timestamp), (5, 'Administrador', utc_timestamp);
insert into `user` (id, name, email, password, created_at) values
    (1, 'João da Silva', 'joao.ger@algafood.com', '123', utc_timestamp),
    (2, 'Maria Joaquina', 'maria.vnd@algafood.com', '123', utc_timestamp),
    (3, 'José Souza', 'jose.aux@algafood.com', '123', utc_timestamp),
    (4, 'Sebastião Martins', 'sebastiao.cad@algafood.com', '123', utc_timestamp);

insert into user_group_permission (user_group_id, permission_id) values
    (1, 1), (1, 2), (2, 1), (2, 2), (3, 1);

insert into user_group_user(user_id, user_group_id) values (1, 1), (1, 2), (2, 2);

insert into restaurant_manager (restaurant_id, user_id) values (1, 3), (3, 4);

insert into `order` (id, code, restaurant_id, customer_id, payment_method_id, address_city_id, address_postal_code,
    address_name, address_number, address_complement, address_neighborhood,
    status, created_at, subtotal, delivery_fee, total)
values (1, '933a9cec-53cd-48ac-8bf8-2538bb9941f7', 1, 1, 1, 1, '38400-000', 'Rua Floriano Peixoto', '500', 'Apto 801', 'Brasil',
    'CREATED', utc_timestamp, 298.90, 10, 308.90);

insert into `order_item` (id, `order_id`, product_id, amount, price, total, notes, created_at)
values (1, 1, 1, 1, 78.9, 78.9, null, utc_timestamp);
insert into `order_item` (id, `order_id`, product_id, amount, price, total, notes, created_at)
values (2, 1, 2, 2, 110, 220, 'Menos picante, por favor', utc_timestamp);

insert into `order` (id, code, restaurant_id, customer_id, payment_method_id, address_city_id, address_postal_code,
        address_name, address_number, address_complement, address_neighborhood,
        status, created_at, subtotal, delivery_fee, total)
values (2, '5d9ac98b-2cc2-472c-b099-1be4ed0f6994', 4, 1, 2, 1, '38400-111', 'Rua Acre', '300', 'Casa 2', 'Centro',
    'CREATED', utc_timestamp, 79, 0, 79);

insert into `order_item` (id, `order_id`, product_id, amount, price, total, notes, created_at)
values (3, 2, 5, 1, 79, 79, 'Ao ponto', utc_timestamp);