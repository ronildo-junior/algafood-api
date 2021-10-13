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
    (1, 'Gerente', utc_timestamp),
    (2, 'Vendedor', utc_timestamp),
    (3, 'Secretária', utc_timestamp),
    (4, 'Cadastrador', utc_timestamp),
    (5, 'Administrador', utc_timestamp);

insert into `user` (id, name, email, password, created_at) values
    (1, 'João da Silva', 'joao.ger@algafood.com', '$2a$12$7y5e.HiMFegfH/A3bT8YBuDRl/sSC8lMUEAnqP7bYhAAgrtoSFAl6', utc_timestamp),
    (2, 'Maria Joaquina', 'maria.vnd@algafood.com', '$2a$12$7y5e.HiMFegfH/A3bT8YBuDRl/sSC8lMUEAnqP7bYhAAgrtoSFAl6', utc_timestamp),
    (3, 'José Souza', 'jose.aux@algafood.com', '$2a$12$7y5e.HiMFegfH/A3bT8YBuDRl/sSC8lMUEAnqP7bYhAAgrtoSFAl6', utc_timestamp),
    (4, 'Sebastião Martins', 'sebastiao.cad@algafood.com', '$2a$12$7y5e.HiMFegfH/A3bT8YBuDRl/sSC8lMUEAnqP7bYhAAgrtoSFAl6', utc_timestamp),
    (5, 'Administrador', 'admin@algafood.com', '$2a$12$7y5e.HiMFegfH/A3bT8YBuDRl/sSC8lMUEAnqP7bYhAAgrtoSFAl6', utc_timestamp);

insert into user_group_user(user_id, user_group_id) values (1, 1), (1, 2), (2, 2), (5, 5);

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

insert into permission (id, name, description)
values (01, 'CREATE_CITIES', 'Create Cities'),
       (02, 'CREATE_CUISINES', 'Create Cuisines'),
       (03, 'CREATE_ORDERS', 'Create Orders'),
       (04, 'CREATE_PAYMENT_METHODS', 'Create Payment Methods'),
       (05, 'CREATE_PERMISSIONS', 'Create Permissions'),
       (06, 'CREATE_PRODUCTS', 'Create Products'),
       (07, 'CREATE_RESTAURANTS', 'Create Restaurants'),
       (08, 'CREATE_STATES', 'Create States'),
       (09, 'CREATE_USERS', 'Create Users'),
       (10, 'CREATE_USER_GROUPS', 'Create User Groups');
insert into permission (id, name, description)
values (11, 'EDIT_CITIES', 'Edit Cities'),
       (12, 'EDIT_CUISINES', 'Edit Cuisines'),
       (13, 'EDIT_ORDERS', 'Edit Orders'),
       (14, 'EDIT_PAYMENT_METHODS', 'Edit Payment Methods'),
       (15, 'EDIT_PERMISSIONS', 'Edit Permissions'),
       (16, 'EDIT_PRODUCTS', 'Edit Products'),
       (17, 'EDIT_RESTAURANTS', 'Edit Restaurants'),
       (18, 'EDIT_STATES', 'Edit States'),
       (19, 'EDIT_USERS', 'Edit Users'),
       (20, 'EDIT_USER_GROUPS', 'Edit User Groups');
insert into permission (id, name, description)
values (21, 'DELETE_CITIES', 'Delete Cities'),
       (22, 'DELETE_CUISINES', 'Delete Cuisines'),
       (23, 'DELETE_ORDERS', 'Delete Orders'),
       (24, 'DELETE_PAYMENT_METHODS', 'Delete Payment Methods'),
       (25, 'DELETE_PERMISSIONS', 'Delete Permissions'),
       (26, 'DELETE_PRODUCTS', 'Delete Products'),
       (27, 'DELETE_RESTAURANTS', 'Delete Restaurants'),
       (28, 'DELETE_STATES', 'Delete States'),
       (29, 'DELETE_USERS', 'Delete Users'),
       (30, 'DELETE_USER_GROUPS', 'Delete User Groups');
insert into permission (id, name, description)
values (31, 'READ_ORDERS', 'Read Orders'),
       (32, 'READ_PERMISSIONS', 'Read Permissions'),
       (33, 'READ_USERS', 'Read Users'),
       (34, 'READ_USER_GROUPS', 'Read User Groups'),
       (35, 'READ_DAILY_SALES', 'Read Daily Sales');

insert into user_group_permission (user_group_id, permission_id)
values (01, 01), (01, 02), (01, 03), (01, 04), (01, 06), (01, 08),
       (01, 13), (01, 14), (01, 16), (01, 17),
       (01, 23), (01, 24), (01, 26),
       (01, 31), (01, 32), (01, 33), (01, 34), (01, 35);
insert into user_group_permission (user_group_id, permission_id)
values (02, 03), (02, 04), (02, 06), (02, 13), (02, 14), (02, 16);
insert into user_group_permission (user_group_id, permission_id)
values (03, 13), (03, 16), (03, 31), (03, 35);
insert into user_group_permission (user_group_id, permission_id)
values (04, 03), (04, 04), (04, 06), (04, 13), (04, 14), (04, 16), (04, 17);
insert into user_group_permission (user_group_id, permission_id)
select 05, id
from permission;