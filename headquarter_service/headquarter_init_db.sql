DELETE FROM supplier;
DELETE FROM part;
DELETE FROM product;
DELETE FROM product_part;
DELETE FROM condition;

-- Add suppliers
INSERT INTO supplier (supplier_id, name)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', 'electroStuff.com');
INSERT INTO supplier (supplier_id, name)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', 'CoolMechanics.com');

-- Add Parts
INSERT INTO part (part_id, name, delievery_time)
VALUES ('fa67191c-d4e2-11eb-b8bc-0242ac130003', 'Schraube 3x16', 10);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('fa67169c-d4e2-11eb-b8bc-0242ac130003', 'Schraube 3x18', 15);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b695e8-d66a-11eb-b8bc-0242ac130003', 'Schraube 4x20', 20);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69ff2-d66a-11eb-b8bc-0242ac130003', 'Kühlbox 60cmx60cmx60cm', 100);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b6a0b0-d66a-11eb-b8bc-0242ac130003', 'Kühlbox Deckel 60cmx60cm', 100);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('fa671a02-d4e2-11eb-b8bc-0242ac130003', 'Tür Größe S 100cmx60cm', 100);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('fa671aca-d4e2-11eb-b8bc-0242ac130003', 'Korpus Größe S 100cmx60cmx55cm', 100);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('7f58bfe8-d418-11eb-b8bc-0242ac130003', 'Kühlmotor klein', 190);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b6985e-d66a-11eb-b8bc-0242ac130003', 'Tür Größe M 150cmx60cm', 100);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69944-d66a-11eb-b8bc-0242ac130003', 'Korpus Größe M 150cmx60cmx55cm', 100);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69a0c-d66a-11eb-b8bc-0242ac130003', 'Kühlmotor groß', 200);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69aca-d66a-11eb-b8bc-0242ac130003', 'Tür Größe L 200cmx60cm', 100);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69d4a-d66a-11eb-b8bc-0242ac130003', 'Korpus Größe L 200cmx60cmx55cm', 100);
INSERT INTO part (part_id, name, delievery_time)
VALUES ('10b69e6c-d66a-11eb-b8bc-0242ac130003', 'Korpus Größe XL 200cmx120cmx55cm', 100);

-- Add Product
INSERT INTO product (product_id, name, production_time)
VALUES ('9e9383ca-d855-11eb-b8bc-0242ac130003', 'Kühlbox 60cmx60cmx60cm - Modell: Die Studentenbox', 100);
INSERT INTO product (product_id, name, production_time)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003',
        'Kühlschrank Größe S 100cmx60cmx60cm - Modell: Der Kühlschrank für Singles', 100);
INSERT INTO product (product_id, name, production_time)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003',
        'Kühlschrank Größe M 150cmx60cmx60cm - Modell: Der Kühlschrank für Pärchen', 200);
INSERT INTO product (product_id, name, production_time)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', 'Kühlschrank Größe L 200cmx60cmx60cm - Modell: Für die ganze Familie',
        300);
INSERT INTO product (product_id, name, production_time)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003',
        'Kühlschrank Größe XL 200cmx120cmx60cm - Modell: Aus dem Weg Kleinverdiener', 400);

-- Add Product_Part
INSERT INTO product_part (product_id, part_id, count)
VALUES ('9e9383ca-d855-11eb-b8bc-0242ac130003', 'fa67191c-d4e2-11eb-b8bc-0242ac130003', 19);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('9e9383ca-d855-11eb-b8bc-0242ac130003', '10b69ff2-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('9e9383ca-d855-11eb-b8bc-0242ac130003', '10b6a0b0-d66a-11eb-b8bc-0242ac130003', 1);

INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', 'fa67191c-d4e2-11eb-b8bc-0242ac130003', 17);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', 'fa671a02-d4e2-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', 'fa671aca-d4e2-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('7174a632-d4e3-11eb-b8bc-0242ac130003', '7f58bfe8-d418-11eb-b8bc-0242ac130003', 1);

INSERT INTO product_part (product_id, part_id, count)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003', 'fa67191c-d4e2-11eb-b8bc-0242ac130003', 19);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003', '10b6985e-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003', '10b69944-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('82a637fe-d4e3-11eb-b8bc-0242ac130003', '7f58bfe8-d418-11eb-b8bc-0242ac130003', 1);

INSERT INTO product_part (product_id, part_id, count)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', 'fa67169c-d4e2-11eb-b8bc-0242ac130003', 23);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', '10b69aca-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', '10b69d4a-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('8664853a-d4e3-11eb-b8bc-0242ac130003', '10b69a0c-d66a-11eb-b8bc-0242ac130003', 1);

INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', '10b695e8-d66a-11eb-b8bc-0242ac130003', 38);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', 'fa67169c-d4e2-11eb-b8bc-0242ac130003', 9);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', '10b69e6c-d66a-11eb-b8bc-0242ac130003', 1);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', '10b69aca-d66a-11eb-b8bc-0242ac130003', 2);
INSERT INTO product_part (product_id, part_id, count)
VALUES ('10b69f3e-d66a-11eb-b8bc-0242ac130003', '10b69a0c-d66a-11eb-b8bc-0242ac130003', 1);

-- Add Condition
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', 'fa67191c-d4e2-11eb-b8bc-0242ac130003', 0.10, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', 'fa67169c-d4e2-11eb-b8bc-0242ac130003', 0.12, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', '10b695e8-d66a-11eb-b8bc-0242ac130003', 0.18, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', '7f58bfe8-d418-11eb-b8bc-0242ac130003', 50.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8a34a8fe-d419-11eb-b8bc-0242ac130003', '10b69a0c-d66a-11eb-b8bc-0242ac130003', 79.99, 'EUR');

INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69ff2-d66a-11eb-b8bc-0242ac130003', 19.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b6a0b0-d66a-11eb-b8bc-0242ac130003', 5.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', 'fa671a02-d4e2-11eb-b8bc-0242ac130003', 17.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', 'fa671aca-d4e2-11eb-b8bc-0242ac130003', 59.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b6985e-d66a-11eb-b8bc-0242ac130003', 29.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69944-d66a-11eb-b8bc-0242ac130003', 99.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69aca-d66a-11eb-b8bc-0242ac130003', 59.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69d4a-d66a-11eb-b8bc-0242ac130003', 149.99, 'EUR');
INSERT INTO condition (supplier_id, part_id, price, currency)
VALUES ('8d017b98-d419-11eb-b8bc-0242ac130003', '10b69e6c-d66a-11eb-b8bc-0242ac130003', 329.99, 'EUR');