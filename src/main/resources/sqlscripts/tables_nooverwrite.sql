-- --------------------------------------------------------
SET FOREIGN_KEY_CHECKS=0;
-- --------------------------------------------------------
--
-- TABLE CREATION WITHOUT OVERWRITE
--

-- DROP TABLE IF EXISTS HCL_customer;
CREATE TABLE IF NOT EXISTS HCL_customer
(
  customer_id INT(11) PRIMARY KEY NOT NULL,
  customer_name VARCHAR(50),
  epost VARCHAR(50),
  tlf INT(11),
  active TINYINT(1) DEFAULT '1' NOT NULL
);

-- DROP TABLE IF EXISTS HCL_deliveries;
CREATE TABLE IF NOT EXISTS HCL_deliveries
(
  delivery_id INT(11) PRIMARY KEY NOT NULL,
  order_id INT(11) NOT NULL,
  delivery_date DATE NOT NULL,
  completed TINYINT(1) DEFAULT '0' NOT NULL,
  delivered TINYINT(4) DEFAULT '0' NOT NULL,
  active TINYINT(4) DEFAULT '1' NOT NULL
);

-- DROP TABLE IF EXISTS HCL_food;
CREATE TABLE IF NOT EXISTS HCL_food
(
  food_id INT(11) PRIMARY KEY NOT NULL,
  name VARCHAR(50),
  price INT(11),
  active TINYINT(1) DEFAULT '1' NOT NULL
);

-- DROP TABLE IF EXISTS HCL_food_ingredient;
CREATE TABLE IF NOT EXISTS HCL_food_ingredient
(
  food_id INT(11) DEFAULT '0' NOT NULL,
  ingredient_id INT(11) DEFAULT '0' NOT NULL,
  number INT(11),
  active TINYINT(1) DEFAULT '1' NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (food_id, ingredient_id)
);

-- DROP TABLE IF EXISTS HCL_ingredient;
CREATE TABLE IF NOT EXISTS HCL_ingredient
(
  ingredient_id INT(11) PRIMARY KEY NOT NULL,
  name VARCHAR(50),
  stock INT(11),
  purchase_price INT(11),
  nuts BIT(1),
  gluten BIT(1),
  lactose BIT(1),
  other VARCHAR(100),
  purchase_date DATE,
  expiration_date DATE,
  active TINYINT(1) DEFAULT '1' NOT NULL
);

-- DROP TABLE IF EXISTS HCL_order;
CREATE TABLE IF NOT EXISTS HCL_order
(
  order_id INT(11) PRIMARY KEY NOT NULL,
  customer_id INT(11) NOT NULL,
  price INT(11),
  adress VARCHAR(50),
  postnr VARCHAR(50),
  active TINYINT(1) DEFAULT '1' NOT NULL,
  order_date DATE NOT NULL
);

-- DROP TABLE IF EXISTS HCL_order_food;
CREATE TABLE IF NOT EXISTS HCL_order_food
(
  order_id INT(11) DEFAULT '0' NOT NULL,
  food_id INT(11) DEFAULT '0' NOT NULL,
  number INT(11) DEFAULT '1',
  active TINYINT(1) DEFAULT '1' NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (order_id, food_id)
);

-- DROP TABLE IF EXISTS HCL_users;
CREATE TABLE IF NOT EXISTS HCL_user
(
  user_id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user_name VARCHAR(50) NOT NULL UNIQUE,
  user_role INT(11) NOT NULL,
  user_salt VARCHAR(50) NOT NULL,
  user_pass VARCHAR(50) NOT NULL,
  user_firstname VARCHAR(50),
  user_lastname VARCHAR(50),
  user_email VARCHAR(50),
  user_tlf INT(11),
  user_adress VARCHAR(50),
  user_postnr INT(11),
  user_start DATE,
  active TINYINT(1) DEFAULT '1' NOT NULL
);

-- --------------------------------------------------------
--
-- ADDING FOREIGN KEYS
--

ALTER TABLE HCL_deliveries ADD FOREIGN KEY (order_id) REFERENCES HCL_order (order_id);
ALTER TABLE HCL_food_ingredient ADD FOREIGN KEY (food_id) REFERENCES HCL_food (food_id);
ALTER TABLE HCL_food_ingredient ADD FOREIGN KEY (ingredient_id) REFERENCES HCL_ingredient (ingredient_id);
ALTER TABLE HCL_order ADD FOREIGN KEY (customer_id) REFERENCES HCL_customer (customer_id);
ALTER TABLE HCL_order_food ADD FOREIGN KEY (order_id) REFERENCES HCL_order (order_id);
ALTER TABLE HCL_order_food ADD FOREIGN KEY (food_id) REFERENCES HCL_food (food_id);

-- --------------------------------------------------------
--
-- ADDING FIRST TIME USER
--

INSERT INTO  HCL_user (
  `user_id` ,
  `user_name` ,
  `user_role` ,
  `user_salt` ,
  `user_pass` ,
  `active`
)
VALUES (DEFAULT , 'admin', '0', 'IOdABDSNfAA=', 'I+D2C2RKkFjw1UxOEbd5k8BdYkE=', '1');

-- --------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 1;
-- --------------------------------------------------------