insert into cuisine(name) values ("Mineira")
insert into cuisine(name) values ("Baiana")
insert into cuisine(name) values ("Italiana")
insert into cuisine(name) values ("Tailandesa")
insert into cuisine(name) values ("Japonesa")

insert into restaurant(name, delivery_fee, cuisine_id) values ("Maktubs", 2.50, 2);
insert into restaurant(name, delivery_fee, cuisine_id) values ("Limax", 2.00, 2);
insert into restaurant(name, delivery_fee, cuisine_id) values ("Espaço Grill", 0, 3);
insert into restaurant(name, delivery_fee, cuisine_id) values ("Tozzo", 5.00, 4);

insert into state (id, name, abbreviation) values (1, "Minas Gerais", "MG");
insert into state (id, name, abbreviation) values (2, "São Paulo", "SP");
insert into state (id, name, abbreviation) values (3, "Ceará", "CE");

insert into city (id, name, state_id) values (1, "Uberlândia", 1);
insert into city (id, name, state_id) values (2, "Belo Horizonte", 1);
insert into city (id, name, state_id) values (3, "São Paulo", 2);
insert into city (id, name, state_id) values (4, "Campinas", 2);
insert into city (id, name, state_id) values (5, "Fortaleza", 3);

insert into payment_method (id, description) values (1, "Cartão de Crédito");
insert into payment_method (id, description) values (2, "Cartão de Débito");
insert into payment_method (id, description) values (3, "Dinheiro");

insert into permission (id, name, description) values (1, "CONSULTAR_COZINHAS", "Permite consultar cozinhas");
insert into permission (id, name, description) values (2, "EDITAR_COZINHAS", "Permite editar cozinhas");
