
-- INSERT INTO `HCL_customer` (`customer_id`, `customer_name`, `epost`, `tlf`, `active`) VALUES


--
-- Dumping data for table `HCL_deliveries`
--


--
-- Dumping data for table `HCL_food`
--

-- INSERT INTO `HCL_food` (`food_id`, `name`, `price`, `active`) VALUES


--
-- Dumping data for table `HCL_food_ingredient`
--

-- INSERT INTO `HCL_food_ingredient` (`food_id`, `ingredient_id`, `number`, `active`) VALUES


--
-- Dumping data for table `HCL_ingredient`
--

-- INSERT INTO `HCL_ingredient` (`ingredient_id`, `name`, `stock`, `purchase_price`, `nuts`, `gluten`, `lactose`, `other`, `purchase_date`, `expiration_date`, `active`) VALUES


--
-- Dumping data for table `HCL_order`
--


--
-- Dumping data for table `HCL_order_food`
--

-- INSERT INTO `HCL_order_food` (`order_id`, `food_id`, `number`, `active`) VALUES


--
-- Dumping data for table `HCL_user`
--

INSERT INTO `HCL_user` (`user_id`, `user_name`, `user_role`, `user_salt`, `user_pass`, `user_firstname`, `user_lastname`, `user_email`, `user_tlf`, `user_adress`, `user_postnr`, `user_start`, `active`) VALUES
  (DEFAULT , 'olavhus', 0, 'U8nqJS5MW2o=', 'a2lJ8psbJJWch/5wBIWBY/KoG8Q=', 'Olav', 'Husby', 'OlavH96@gmail.com', 93240605, 'Bøkveien 11A', 7059, '2016-03-16', 1),
  (DEFAULT, 'bjornhaf', 0, 'HB7RkSzak8Q=', '7XTMzI6Ib5wvXBUUtzMpjSr4Vnc=', 'Bjørn', 'Hafeld', 'BjørnHafeld@gmail.com', 12345678, 'Borti Sjægget', 1000, '2011-02-02', 1),
  (DEFAULT, 'trineols', 0, 'vvx+BLVq4Do=', 'ep/9fK1A40vwtn33/dSEz+CnRN8=', 'Trine', 'Olsen', 'trineliseolsen@outlook.com', 65678, 'Atmed Elgan', 20, '2014-02-02', 1),
  (DEFAULT, 'jensern', 0, 'wiA7nXBVg4I=', 'TsPyO8W6wBda3Fl4EawyuYNzBGk=', 'Jens Tobias', 'Kaarud', 'jenskaarud@gmail.com', NULL, 'Norge', 8400, NULL, 1),
  (DEFAULT, 'Testuser', 1, 'G6hgQBIMy1M=', 'TuZbz4WEvlmr682XEZtX8mlYESM=', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1),
  (DEFAULT, 'newUserTest', 0, 'OQm2KErOaDQ=', '6t6mBXkNuEg71Feagy6TV+Ly7RY=', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0),
  (DEFAULT, 'newUserTest2', 0, '/JsfXhbNZXA=', '3mHn02Lvg0gmTE+gZFeioa0lzOk=', 'test', 'tester', 'artbart', 12345678, 'lol', 1234, '2016-04-20', 0),
  (DEFAULT, 'quinky', 1, 'vm9Od5T6bPk=', 'gxdI1i4W+4nmcjrWpM8Or/Xtzoc=', 'test', 'tester', '', NULL, '', NULL, '2016-01-31', 1);
