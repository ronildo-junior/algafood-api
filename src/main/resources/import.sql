insert into cuisine(name) values ("Mineira");
insert into cuisine(name) values ("Baiana");
insert into cuisine(name) values ("Italiana");
insert into cuisine(name) values ("Tailandesa");
insert into cuisine(name) values ("Japonesa");

insert into state (id, name, abbreviation) values (1, "Minas Gerais", "MG");
insert into state (id, name, abbreviation) values (2, "São Paulo", "SP");
insert into state (id, name, abbreviation) values (3, "Ceará", "CE");

insert into city (id, name, state_id) values (1, "Uberlândia", 1);
insert into city (id, name, state_id) values (2, "Belo Horizonte", 1);
insert into city (id, name, state_id) values (3, "São Paulo", 2);
insert into city (id, name, state_id) values (4, "Campinas", 2);
insert into city (id, name, state_id) values (5, "Fortaleza", 3);

insert into restaurant(name, delivery_fee, cuisine_id,
        address_name, address_number, address_complement, address_neighborhood, city_id)
    values ("Maktubs", 2.50, 2, "Rua Teste 1", "1", "Complemento 1", "Bairro 1", 1);
insert into restaurant(name, delivery_fee, cuisine_id,
        address_name, address_number, address_complement, address_neighborhood, city_id)
    values ("Limax", 2.00, 2, "Rua Teste 2", "2", "Complemento 2", "Bairro 2", 1);
insert into restaurant(name, delivery_fee, cuisine_id,
        address_name, address_number, address_complement, address_neighborhood, city_id)
    values ("Espaço Grill", 0, 3, "Rua Teste 3", "3", "Complemento 3", "Bairro 3", 2);
insert into restaurant(name, delivery_fee, cuisine_id,
        address_name, address_number, address_complement, address_neighborhood, city_id)
    values ("Churrascaria Km 7", 5.00, 4, "Rua Teste 4", "4", "Complemento 4", "Bairro 4", 3);

insert into payment_method (id, description) values (1, "Cartão de Crédito");
insert into payment_method (id, description) values (2, "Cartão de Débito");
insert into payment_method (id, description) values (3, "Dinheiro");

insert into permission (id, name, description) values (1, "CONSULTAR_COZINHAS", "Permite consultar cozinhas");
insert into permission (id, name, description) values (2, "EDITAR_COZINHAS", "Permite editar cozinhas");

insert into restaurant_payment_method (restaurant_id, payment_method_id)
    values (1, 1), (1, 2), (1, 3), (2, 3), (3, 2), (3, 3);
