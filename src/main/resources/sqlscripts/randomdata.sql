-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 26, 2016 at 04:34 PM
-- Server version: 5.5.47-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.16

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `olavhus`
--

--
-- Dumping data for table `HCL_customer`
--

INSERT INTO `HCL_customer` (`customer_id`, `customer_name`, `epost`, `tlf`, `active`) VALUES
  (1, 'Jens', 'jens', 1234, 0),
  (2, 'Per Gustav', 'Per@Gustav.no', 67879587, 0),
  (3, 'Elisabeth II', 'Elisabeth@gov.england.co.uk', 1, 1),
  (6, 'Jens Stoltenberg', 'Jenseman@regjeringen.no', 76675879, 1),
  (22, 'Olav Reppe Husby', 'OlavH96@gmail.com', 93240605, 1),
  (88, 'Bill Gates', 'bill@microsoft.com', 1111, 1),
  (89, 'Lulu', 'epost', 12345678, 0),
  (90, 'Lulu', 'epost', 12345678, 0),
  (91, 'Regine', 'anna@hotmail.com', 75584788, 0),
  (92, 'Kari', 'kari@hotmail.com', 75583478, 1),
  (93, 'Regine', 'anna@hotmail.com', 75584788, 0),
  (94, 'Regine', 'anna@hotmail.com', 75584788, 0),
  (103, 'Regine', 'anna@hotmail.com', 75584788, 0),
  (109, 'Lulu', 'epost', 12345678, 1),
  (111, 'Trinemor', 'Trinemor@hotmail.com', 75584788, 1),
  (124, 'Regine', 'anna@hotmail.com', 75584788, 0),
  (125, 'Regine', 'anna@hotmail.com', 75584788, 1);

--
-- Dumping data for table `HCL_deliveries`
--

INSERT INTO `HCL_deliveries` (`delivery_id`, `order_id`, `delivery_date`, `completed`, `delivered`, `active`) VALUES
  (7, 65, '2016-07-04', 0, 0, 1),
  (8, 65, '2016-07-25', 0, 0, 1),
  (9, 65, '2016-08-15', 0, 0, 1),
  (10, 65, '2016-09-05', 0, 0, 1),
  (11, 65, '2016-09-26', 0, 0, 1),
  (12, 65, '2016-10-17', 0, 0, 1),
  (13, 65, '2016-11-07', 0, 0, 1),
  (14, 65, '2016-11-28', 0, 0, 1),
  (15, 65, '2016-12-19', 0, 0, 1),
  (16, 65, '2017-01-09', 0, 0, 1),
  (17, 65, '2017-01-30', 0, 0, 1),
  (22, 4, '2016-04-27', 0, 0, 1),
  (23, 75, '2016-05-01', 0, 0, 1),
  (24, 75, '2016-05-08', 0, 0, 1),
  (25, 75, '2016-05-15', 0, 0, 1),
  (26, 75, '2016-05-22', 0, 0, 1),
  (27, 75, '2016-05-29', 0, 0, 1),
  (28, 75, '2016-06-05', 0, 0, 1),
  (29, 75, '2016-06-12', 0, 0, 1),
  (30, 75, '2016-06-19', 0, 0, 1),
  (31, 75, '2016-06-26', 0, 0, 1),
  (37, 16, '2016-04-30', 0, 0, 1),
  (38, 16, '2016-05-07', 0, 0, 1),
  (39, 16, '2016-05-14', 0, 0, 1),
  (40, 16, '2016-05-21', 0, 0, 1),
  (41, 16, '2016-04-29', 0, 0, 1),
  (42, 16, '2016-05-13', 0, 0, 1),
  (43, 76, '2016-01-08', 0, 0, 1),
  (44, 76, '2016-02-05', 0, 0, 1),
  (45, 76, '2016-03-04', 0, 0, 1),
  (46, 76, '2016-04-01', 0, 0, 1),
  (47, 76, '2016-04-29', 0, 0, 1),
  (48, 76, '2016-05-27', 0, 0, 1),
  (49, 76, '2016-06-24', 0, 0, 1),
  (50, 76, '2016-07-22', 0, 0, 1),
  (51, 76, '2016-08-19', 0, 0, 1),
  (52, 76, '2016-09-16', 0, 0, 1),
  (53, 76, '2016-10-14', 0, 0, 1),
  (54, 76, '2016-11-11', 0, 0, 1),
  (55, 16, '2016-04-28', 0, 0, 1),
  (56, 16, '2016-05-05', 0, 0, 1),
  (57, 16, '2016-05-12', 0, 0, 1),
  (58, 16, '2016-05-19', 0, 0, 1),
  (59, 16, '2016-05-26', 0, 0, 1);

--
-- Dumping data for table `HCL_food`
--

INSERT INTO `HCL_food` (`food_id`, `name`, `price`, `active`) VALUES
  (1, 'Double Diabetes Burger', 100, 1),
  (16, 'Healthy Salad', 20, 1),
  (17, 'Enchiladas', 40, 1),
  (18, 'Mexican Pizza', 110, 1),
  (44, 'Pasta', 100, 1),
  (45, 'Grøt', 60, 0),
  (46, 'kake', 60, 0),
  (47, 'muffins', 60, 0),
  (48, 'kake', 60, 0),
  (51, 'Grøt', 60, 0),
  (54, 'kake', 60, 0),
  (55, 'muffins', 60, 0),
  (59, 'kake', 60, 0);

--
-- Dumping data for table `HCL_food_ingredient`
--

INSERT INTO `HCL_food_ingredient` (`food_id`, `ingredient_id`, `number`, `active`) VALUES
  (1, 11, 6, 0),
  (1, 12, 2, 1),
  (1, 13, 2, 1),
  (1, 14, 5, 1),
  (1, 16, 4, 1),
  (16, 11, 2, 1),
  (16, 12, 5, 1),
  (16, 20, 5, 1),
  (16, 21, 5, 1),
  (16, 22, 4, 1),
  (16, 23, 5, 1),
  (17, 11, 6, 1),
  (17, 12, 2, 1),
  (17, 14, 10, 1),
  (17, 16, 2, 1),
  (17, 20, 5, 1),
  (17, 22, 2, 1),
  (17, 24, 1, 1),
  (18, 11, 10, 1),
  (18, 14, 10, 1),
  (18, 20, 5, 1),
  (44, 11, 10, 1),
  (44, 16, 10, 1),
  (46, 71, 20, 0);

--
-- Dumping data for table `HCL_ingredient`
--

INSERT INTO `HCL_ingredient` (`ingredient_id`, `name`, `stock`, `purchase_price`, `nuts`, `gluten`, `lactose`, `other`, `purchase_date`, `expiration_date`, `active`) VALUES
  (9, 'Osteskive', 10, 5, b'0', b'0', b'1', 'Jarlsberg', '2016-04-25', '2016-08-01', 0),
  (10, 'Tomatskive', 5, 2, b'0', b'0', b'0', '', '2016-04-25', '2016-05-01', 0),
  (11, 'Slice of Cheese', 20, 2, b'1', b'0', b'1', 'Jarlsberg', '2016-04-25', '2016-06-01', 1),
  (12, 'Salad', 10, 10, b'0', b'0', b'0', '', '2016-04-25', '2016-08-01', 1),
  (13, 'Hamburgerbun', 10, 3, b'0', b'1', b'0', '', '2016-04-25', '2016-05-01', 1),
  (14, 'Salsa', 20, 1, b'0', b'0', b'0', '', '2016-04-25', '2016-05-01', 1),
  (15, 'Ham', 2, 30, b'0', b'0', b'0', '', '2016-04-25', '2016-06-01', 1),
  (16, 'Baconstrip', 10, 3, b'1', b'0', b'0', '', '2016-04-25', '2016-06-06', 1),
  (20, 'Red Onions', 20, 2, b'0', b'0', b'0', '', '2016-04-25', '2016-07-01', 1),
  (21, 'Mayonnaise', 15, 1, b'0', b'0', b'1', '', '2016-04-25', '2016-05-01', 1),
  (22, 'Olive Oil', 5, 3, b'0', b'0', b'0', '', '2016-04-25', '2019-09-01', 1),
  (23, 'Melons', 15, 1, b'0', b'1', b'1', 'Water', '2016-04-25', '2016-04-29', 1),
  (24, 'Tortilla', 9, 2, b'0', b'1', b'0', '', '2016-04-25', '2016-08-01', 1),
  (70, 'mandel', 5, 56, b'0', b'0', b'1', 'kun en', '2016-04-04', '2017-05-06', 0),
  (71, 'kakestrø', 5, 56, b'0', b'0', b'1', 'æsj', '2016-04-04', '2017-05-06', 0),
  (72, 'muffinsstrø', 5, 56, b'0', b'0', b'1', 'æsj', '2016-04-04', '2017-05-06', 0);

--
-- Dumping data for table `HCL_order`
--

INSERT INTO `HCL_order` (`order_id`, `customer_id`, `price`, `adress`, `postnr`, `active`, `order_date`) VALUES
  (1, 1, 100, 'jens', 1234, 0, '2016-04-25'),
  (2, 3, 50, 'Buckingham Palace', 1234, 1, '2016-04-25'),
  (4, 6, 200, 'Frimurerlogen 1', 7030, 1, '2016-04-25'),
  (16, 22, 200, 'Bøkveien 11A', 7059, 1, '2016-04-25'),
  (17, 3, 1000, 'NTNU - IIE', 7031, 1, '2016-04-25'),
  (50, 88, 100, 'Microsoft St.', 124, 1, '2016-04-26'),
  (51, 89, 123, 'Elgeseter', 7030, 0, '2017-02-02'),
  (52, 90, 123, 'Elgeseter', 7030, 0, '2017-02-02'),
  (53, 91, 123, 'Lade', 7030, 0, '2017-02-02'),
  (56, 93, 123, 'Lade', 7030, 0, '2017-02-02'),
  (65, 109, 123, 'Elgeseter', 7030, 0, '2017-02-02'),
  (73, 124, 123, 'Lade', 7030, 0, '2017-02-02'),
  (74, 125, 123, 'Lade', 7030, 0, '2017-02-02'),
  (75, 92, 300, 'Downing Street 10', 7036, 1, '2016-04-26'),
  (76, 111, 100, 'Leafroad 4', 7055, 1, '2016-04-26');

--
-- Dumping data for table `HCL_order_food`
--

INSERT INTO `HCL_order_food` (`order_id`, `food_id`, `number`, `active`) VALUES
  (2, 1, 10, 1),
  (2, 16, 1, 1),
  (4, 16, 20, 1),
  (4, 17, 5, 1),
  (16, 17, 1, 1),
  (16, 18, 1, 1),
  (17, 16, 1, 1),
  (17, 17, 10, 1),
  (17, 18, 10, 1),
  (50, 44, 10, 1),
  (75, 17, 4, 1),
  (75, 18, 2, 1),
  (76, 16, 1, 1),
  (76, 44, 1, 1);

--
-- Dumping data for table `HCL_user`
--

INSERT INTO `HCL_user` (`user_id`, `user_name`, `user_role`, `user_salt`, `user_pass`, `user_firstname`, `user_lastname`, `user_email`, `user_tlf`, `user_adress`, `user_postnr`, `user_start`, `active`) VALUES

  (2, 'Salesperson', 1, 'Y+MUNsmiWQw=', 'UNpXCDZHptFrv64/UUTFMmGcfSI=', 'Sales', 'McSaleson', 'Sales@LegitSales.ng', 747283674, 'Salesville 92', 7044, '2016-04-25', 1),
  (3, 'Chef', 2, 'PMDfpMMXw+o=', 'Or6zN3nSa9g9CJkOAkDFxIdX1aE=', 'Jerome «Chef»', 'McElroy', 'JeromeMcElroy@southpark.com', 93561224, 'The canteen 99', 7099, '2016-04-25', 1),
  (4, 'Driver', 3, '62fSOkd5hVw=', 'aPz/rSJrsDxhLJtk+9kzDWBKL8s=', 'Driver', 'McZoom', 'Driver@Gottagofast.com', 94671235, 'Company Parkinglot 87', 7030, '2016-04-25', 1),
  (6, 'Morten', 1, 'r7gdep0pYn0=', 'iVxGe3hL6Eu30U8Ii14RuwtvXV8=', 'Morten', 'Mortensen', 'viking@skuspiller.no', 90291876, 'TronheimTeather 1', 7054, '2016-04-25', 0),
  (8, 'Kris', 1, 'uZbTqVXS/C8=', 'UGCIDVorYjildFsTv9Kw4vpMJqw=', 'Kris', 'Kristensen', 'kris@hist.no', 98853785, 'Ranheim', 7021, '2016-04-25', 1),
  (9, 'Kristine', 1, 'qdYf+NLxmXg=', 'xTXiPYseX+0QCtMJGNk3gxPC8as=', 'Kristine', 'Kristindottir', 'starwars@fans.com', 95342976, 'Bananveien 5', 7100, '2016-04-25', 1),
  (10, 'Mai', 1, 'QGY8ptV2WPs=', 'dv0ABSeF6Q3nRDKJcBLEBC3AsWE=', 'Mai', 'Maaned', 'maimaaned@jorda.no', 94407831, 'Stratosveien 2', 7010, '2016-04-25', 1),
  (11, 'Per', 1, 'fHljeb3FlWI=', 'hT9AudpTYeX0QJGVpib8BoamzCc=', 'Per', 'Person', 'ukjent@identitet.no', 91820333, 'TG lot 23', 7052, '2016-04-25', 1);
SET FOREIGN_KEY_CHECKS=1;
