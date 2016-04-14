SET FOREIGN_KEY_CHECKS=0;
INSERT INTO `HCL_customer` (`customer_id`, `customer_name`, `epost`, `tlf`, `active`) VALUES
(1, 'Grandma', 'Grandma@gmail.com', 34567656, 1),
(2, 'Microsoft', 'Microsoft@gmail.com', 1286918572, 1),
(3, 'Ostost', 'Swag@gmail.com', 145678, 1),
(7, 'Jens Kaarud', 'jenskaarud@gmail.com', 12345678, 1),
(8, 'jens', 'jens.com', 1234, 1),
(9, 'JensTest', 'jens', 1234, 1),
(10, 'Magnus', 'Magnus@gmail.com', 234567898, 1),
(11, 'Ola Nordmann', 'ola@norge.no', 12345678, 1),
(12, 'Olav', 'olavh96@gmail.com', 93240605, 1),
(15, 'Test', 'test@gmail.com', 86132139, 0),
(16, 'jensern', 'jens', 1234, 1),
(17, 'Kjell-Sture', 'Sturegutten@hotmail.com', 75584788, 1),
(18, 'Kaffe-Lars', 'coffein_lover@gmail.com', 75584061, 1);

--
-- Dumping data for table `HCL_food`
--

INSERT INTO `HCL_food` (`food_id`, `name`, `price`, `active`) VALUES
(200, 'Indisk Burger', 100, 1),
(201, 'Skinke Burger', 53, 1),
(202, 'Mexikansk Lasagne', 97, 1),
(203, 'Oste Pølse', 67, 1),
(204, 'Kinesisk Burger', 22, 1),
(205, 'Mexikansk Brødskive', 60, 1),
(206, 'Skinke Sandwich', 25, 1),
(207, 'Indisk Brødskive', 96, 1),
(208, 'Fresh Salat', 8, 1),
(209, 'Fresh Kanelsnurr', 52, 1),
(210, 'Kinesisk Brødskive', 32, 1),
(211, 'Mexikansk Burger', 80, 1),
(212, 'Mexikansk Pølse', 22, 1),
(213, 'Paprika Kebab', 90, 1),
(214, 'Skinke Kebab', 50, 1),
(215, 'Fresh Lasagne', 30, 1),
(216, 'Oste Sandwich', 62, 1),
(217, 'Kinesisk Sandwich', 36, 1),
(218, 'Kinesisk Suppe', 99, 1),
(219, 'Mexikansk Kanelsnurr', 53, 1),
(220, 'Plain Pølse', 43, 1),
(221, 'Fresh Taco', 57, 1),
(222, 'Paprika Taco', 66, 1),
(223, 'Indisk Lasagne', 78, 1),
(224, 'Plain Kanelsnurr', 62, 1),
(225, 'Indisk Sandwich', 2, 1),
(226, 'Fresh Sandwich', 52, 1),
(227, 'Kinesisk Taco', 56, 1),
(228, 'Oste Burger', 80, 1),
(229, 'Paprika Salat', 8, 1),
(230, 'Oste Taco', 66, 1),
(231, 'Indisk Salat', 2, 1),
(232, 'Fresh Suppe', 79, 1),
(233, 'Paprika Brødskive', 6, 1),
(234, 'Indisk Kanelsnurr', 35, 1),
(235, 'Fresh Brødskive', 63, 1),
(236, 'Fresh Pølse', 6, 1),
(237, 'Skinke Kanelsnurr', 96, 1),
(238, 'Kinesisk Salat', 65, 1),
(239, 'Plain Sandwich', 97, 1),
(240, 'Oste Lasagne', 43, 1),
(241, 'Kinesisk Lasagne', 74, 1),
(242, 'Oste Brødskive', 19, 1),
(243, 'Kinesisk Kebab', 51, 1),
(244, 'Skinke Pølse', 80, 1),
(245, 'Skinke Lasagne', 27, 1),
(246, 'Skinke Suppe', 5, 1),
(247, 'Paprika Kanelsnurr', 6, 1),
(248, 'Plain Burger', 66, 1),
(249, 'Kinesisk Kanelsnurr', 9, 1),
(250, 'Oste Kanelsnurr', 84, 1),
(251, 'Indisk Taco', 38, 1),
(252, 'Plain Lasagne', 67, 1),
(253, 'Hot Burger', 410, 1),
(254, 'Indisk Burger', 8610, 1),
(255, 'Skinke Burger', 6210, 1),
(256, 'Vanlig Kanelsnurr', 6110, 1),
(257, 'Oste Pølse', 3210, 1),
(258, 'Oste Suppe', 6110, 1),
(259, 'Kinesisk Burger', 7410, 1),
(260, 'Skinke Sandwich', 2510, 1),
(261, 'Oste Salat', 5810, 1),
(262, 'Paprika Suppe', 710, 1),
(263, 'Kinesisk Brødskive', 4210, 1),
(264, 'Paprika Pølse', 6510, 1),
(265, 'Mexikansk Burger', 4710, 1),
(266, 'Skinke Taco', 2110, 1),
(267, 'Mexikansk Pølse', 3010, 1),
(268, 'Paprika Kebab', 4910, 1),
(269, 'Hot Salat', 2410, 1),
(270, 'Skinke Kebab', 7710, 1),
(271, 'Kinesisk Pølse', 2510, 1),
(272, 'Paprika Sandwich', 5110, 1),
(273, 'Oste Sandwich', 5310, 1),
(274, 'Kinesisk Sandwich', 4310, 1),
(275, 'Kinesisk Suppe', 1210, 1),
(276, 'Mexikansk Kanelsnurr', 8110, 1),
(277, 'Hot Brødskive', 8810, 1),
(278, 'Paprika Taco', 6010, 1),
(279, 'Indisk Lasagne', 5310, 1),
(280, 'Vanlig Lasagne', 1910, 1),
(281, 'Indisk Sandwich', 3410, 1),
(282, 'Hot Taco', 6010, 1),
(283, 'Vanlig Suppe', 2810, 1),
(284, 'Skinke Salat', 1210, 1),
(285, 'Hot Sandwich', 6910, 1),
(286, 'Paprika Salat', 8510, 1),
(287, 'Oste Burger', 5510, 1),
(288, 'Indisk Salat JA', 3310, 1),
(289, 'Oste Taco', 2710, 1),
(290, 'Hot Lasagne', 5610, 1),
(291, 'Indisk Pølse', 5010, 1),
(292, 'Vanlig Burger', 4710, 1),
(293, 'Skinke Kanelsnurr', 6710, 1),
(294, 'Vanlig Pølse', 8410, 1),
(295, 'Oste Brødskive', 8310, 1),
(296, 'Mexikansk Salat', 7310, 1),
(297, 'Kinesisk Kebab', 910, 1),
(298, 'Indisk Kebab', 6210, 1),
(299, 'Skinke Pølse', 7410, 1),
(300, 'Paprika Burger', 3510, 1),
(301, 'Skinke Lasagne', 4110, 1),
(302, 'Skinke Brødskive', 410, 1),
(303, 'Mexikansk Kebab', 7310, 1),
(304, 'Paprika Kanelsnurr', 1810, 1),
(305, 'Kinesisk Kanelsnurr', 7510, 1),
(306, 'Oste Kanelsnurr', 8710, 1),
(307, 'Indisk Taco', 5310, 1),
(310, 'JensTest', 100, 1),
(311, 'Test2', 123, 1),
(312, 'Bløtkake', 200, 1),
(313, 'Sirupssnipp', 20, 1),
(314, 'Fiskepudding', 75, 1),
(315, '', 1, 1),
(316, 'Pultost', 0, 1),
(317, 'test', 100, 1),
(319, 'Møsbrømslfse', 75, 1),
(320, 'Julegrøt', 60, 1),
(321, 'Lunsj', 75, 1),
(322, 'Klubb', 60, 0),
(323, 'Møsbrømslfse', 75, 1),
(324, 'Klubb', 60, 0),
(325, 'Møsbrømslfse', 75, 0),
(326, 'Klubb', 60, 0),
(327, 'Møsbrømslfse', 75, 1),
(328, 'Klubb', 60, 0),
(329, 'Møsbrømslfse', 75, 0),
(330, 'Klubb', 60, 0),
(331, 'Møsbrømslfse', 75, 0),
(332, 'Julegrøt', 60, 1),
(333, 'Julegrøt', 60, 1),
(334, 'Julegrøt', 60, 1),
(335, 'Julegrøt', 60, 1),
(336, 'Julegrøt', 60, 1),
(337, 'Klubb', 60, 0),
(338, 'Møsbrømslfse', 75, 0),
(339, 'Julegrøt', 60, 1),
(340, 'Julegrøt', 60, 1),
(341, 'Julegrøt', 60, 1),
(342, 'Julegrøt', 60, 1),
(343, 'Julegrøt', 60, 1),
(344, 'Klubb', 60, 0),
(345, 'Møsbrømslfse', 75, 0),
(346, 'Julegrøt', 60, 1),
(347, 'Bløtkake', 75, 1),
(348, 'Sirupssnipp', 75, 1),
(349, ' ', 1, 1),
(350, 'Bløtkake', 75, 1),
(351, 'Sirupssnipp', 75, 1),
(352, ' ', 1, 1),
(353, 'Bløtkake', 75, 1),
(354, 'Sirupssnipp', 75, 1),
(355, ' ', 1, 1),
(356, 'Bløtkake', 75, 1),
(357, 'Sirupssnipp', 75, 1),
(358, ' ', 1, 1),
(359, 'Bløtkake', 75, 1),
(360, 'Sirupssnipp', 75, 1),
(361, ' ', 1, 1),
(362, 'Bløtkake', 75, 1),
(363, 'Sirupssnipp', 75, 1),
(364, '', 1, 1),
(365, 'Bløtkake', 75, 1),
(366, 'Sirupssnipp', 75, 1),
(367, '', 1, 1),
(368, 'Bløtkake', 75, 1),
(369, 'Sirupssnipp', 75, 1),
(370, '', 1, 1),
(371, 'Bløtkake', 75, 1),
(372, 'Sirupssnipp', 75, 1),
(373, ' ', 1, 1),
(374, 'Bløtkake', 75, 1),
(375, 'Sirupssnipp', 75, 1),
(376, ' ', 1, 1),
(377, 'Bløtkake', 75, 1),
(378, 'Sirupssnipp', 75, 1),
(379, ' ', 1, 0),
(380, 'Bløtkake', 75, 1),
(381, 'Sirupssnipp', 75, 1),
(382, ' ', 1, 1),
(383, 'Bløtkake', 75, 1),
(384, 'Sirupssnipp', 75, 1),
(385, ' ', 1, 1),
(386, 'Bløtkake', 75, 1),
(387, 'Sirupssnipp', 75, 1),
(388, ' ', 1, 1),
(389, 'Bløtkake', 75, 1),
(390, 'Sirupssnipp', 75, 1),
(391, ' ', 1, 1),
(392, 'Bløtkake', 75, 1),
(393, 'Sirupssnipp', 75, 1),
(394, ' ', 1, 1),
(395, 'Klubb', 60, 0),
(396, 'Møsbrømslfse', 75, 0),
(397, 'Julegrøt', 60, 1),
(398, 'Bløtkake', 75, 1),
(399, 'Sirupssnipp', 75, 1),
(400, ' ', 1, 1),
(401, 'Klubb', 60, 0),
(402, 'Møsbrømslfse', 75, 0),
(403, 'Julegrøt', 60, 1),
(404, 'Bløtkake', 75, 1),
(405, 'Sirupssnipp', 75, 1),
(406, ' ', 1, 1),
(407, 'Bløtkake', 75, 1),
(408, 'Sirupssnipp', 75, 1),
(409, '', 1, 1),
(410, 'Bløtkake', 75, 1),
(411, 'Sirupssnipp', 75, 1),
(412, 'Pultost', 0, 1),
(413, 'Klubb', 60, 0),
(414, 'Møsbrømslfse', 75, 0),
(415, 'Julegrøt', 60, 1),
(416, 'Bløtkake', 75, 1),
(417, 'Sirupssnipp', 75, 1),
(418, 'Fiskepudding', 75, 1),
(419, 'Fiskepudding', 75, 1),
(420, 'Klubb', 60, 0),
(421, 'Møsbrømslfse', 75, 0),
(422, 'Julegrøt', 60, 1),
(423, 'Bløtkake', 75, 1),
(424, 'Sirupssnipp', 75, 1),
(425, 'Klubb', 60, 0),
(426, 'Møsbrømslfse', 75, 0),
(427, 'Julegrøt', 60, 1),
(428, 'Bløtkake', 75, 1),
(429, 'Sirupssnipp', 75, 1);

--
-- Dumping data for table `HCL_food_ingredient`
--

INSERT INTO `HCL_food_ingredient` (`food_id`, `ingredient_id`, `number`, `active`) VALUES
(200, 1, 10, 1),
(200, 2, 100, 1),
(200, 7, 5, 1),
(200, 10, 10, 1),
(201, 1, 1, 1),
(201, 2, 1, 1),
(201, 3, 1, 1),
(201, 4, 5, 1),
(201, 6, 50, 1),
(202, 1, 10, 1),
(203, 1, 5, 1),
(204, 3, 4, 1),
(205, 1, 10, 1),
(205, 3, 5, 1),
(206, 3, 10, 1),
(211, 3, 10, 1),
(216, 1, 3, 1),
(230, 1, 5, 1),
(235, 1, 10, 1),
(307, 1, 3, 1),
(307, 4, 2, 1),
(317, 1, 1000, 1);

--
-- Dumping data for table `HCL_ingredient`
--

INSERT INTO `HCL_ingredient` (`ingredient_id`, `name`, `stock`, `purchase_price`, `nuts`, `gluten`, `lactose`, `other`, `purchase_date`, `expiration_date`, `active`) VALUES
(1, 'Cheese', 2, 100, b'0', b'0', b'1', 'Dropped it on the floor', '2001-01-02', '2005-01-01', 1),
(2, 'Ham', 2, 200, b'0', b'0', b'0', 'Meat', '2016-01-02', '2016-04-05', 1),
(3, 'Bread - Fine', 5, 1, b'0', b'1', b'0', 'Cardboard', '1866-01-01', '2020-01-01', 1),
(4, 'Bread - Normal', 10, 10, b'0', b'1', b'0', 'Fresh', '2016-02-25', '2016-02-27', 1),
(6, 'test', 100, 100, b'1', b'0', b'1', 'test', '2003-02-02', '2005-04-04', 1),
(7, 'Salat', 1, 10, b'0', b'0', b'0', '', '2016-04-11', '2016-05-01', 0),
(8, 'test1', 100, 10, b'0', b'0', b'0', 'testingtesting', '2016-01-01', '2017-01-01', 0),
(9, 'test2', 0, 0, b'0', b'0', b'0', '', '2016-01-01', '2017-02-02', 0),
(10, 'test', 100, 100, b'0', b'0', b'0', '', '2016-04-13', '2017-05-13', 0);

--
-- Dumping data for table `HCL_order`
--

INSERT INTO `HCL_order` (`order_id`, `customer_id`, `price`, `adress`, `postnr`, `order_date`, `delivery_date`, `delivered`, `active`) VALUES
(2, 2, 10, 'My place yo', '1111', '2016-04-01', '2016-04-30', b'0', 1),
(4, 9, 100, 'Ostehaug', '1911', '2015-01-01', '2016-04-11', b'0', 1),
(7, 8, 100, 'jens', '1234', '2016-02-01', '2016-04-30', b'1', 1),
(8, 3, 100, 'Skolen', '1234', '2001-10-01', '2016-04-20', b'0', 0),
(9, 1, 100, 'Bøkveien11A', '0001', '2016-04-04', '2016-04-20', b'0', 0),
(10, 10, 100, 'Halla', '8400', '2016-01-01', '2016-05-01', b'0', 1),
(11, 8, 200, 'paamoterommet', '7104', '2004-02-01', '2016-02-01', b'1', 1);

--
-- Dumping data for table `HCL_order_food`
--

INSERT INTO `HCL_order_food` (`order_id`, `food_id`, `number`, `active`) VALUES
(2, 200, 5, 1),
(7, 200, 3, 1),
(7, 202, 1, 1),
(8, 200, 5, 1),
(8, 307, 3, 1),
(9, 200, 3, 1),
(9, 202, 1, 1),
(9, 203, 1, 1),
(9, 317, 10, 1),
(10, 200, 5, 1);

--
-- Dumping data for table `HCL_order_package`
--

INSERT INTO `HCL_order_package` (`order_id`, `package_id`, `number`, `active`) VALUES
(2, 1, 0, 1),
(2, 2, 5, 1);

--
-- Dumping data for table `HCL_package`
--

INSERT INTO `HCL_package` (`package_id`, `name`, `price`, `active`) VALUES
(1, 'A lot of sandwiches', 25, 1),
(2, 'Soup-Package', 1000, 1),
(3, 'Soup-Package', 1000, 1),
(4, 'Ice Tea', 100, 1);

--
-- Dumping data for table `HCL_package_food`
--

INSERT INTO `HCL_package_food` (`package_id`, `food_id`, `number`, `active`) VALUES
(1, 2, 5, 1),
(2, 218, 5, 1),
(2, 232, 3, 1),
(2, 246, 5, 1),
(2, 258, 5, 1),
(2, 262, 5, 1),
(2, 275, 5, 1),
(2, 283, 5, 1);

--
-- Dumping data for table `HCL_subscription`
--

INSERT INTO `HCL_subscription` (`order_id`, `active`) VALUES
(4, 1);

--
-- Dumping data for table `HCL_subscription_date`
--

INSERT INTO `HCL_subscription_date` (`date_id`, `order_id`, `dato`, `active`) VALUES
(1, 4, '2011-04-03', 1),
(2, 4, '2011-04-03', 1);

--
-- Dumping data for table `HCL_users`
--

INSERT INTO `HCL_users` (`user_id`, `user_name`, `user_role`, `user_salt`, `user_pass`, `user_firstname`, `user_lastname`, `user_email`, `user_tlf`, `user_adress`, `user_postnr`, `user_start`, `active`) VALUES
(1, 'olavhus', 0, 'U8nqJS5MW2o=', 'a2lJ8psbJJWch/5wBIWBY/KoG8Q=', 'Olav', 'Husby', 'OlavH96@gmail.com', 93240605, 'Bøkveien 11A', 7059, '2016-03-16', 1),
(8, 'bjornhaf', 0, 'HB7RkSzak8Q=', '7XTMzI6Ib5wvXBUUtzMpjSr4Vnc=', 'Bjørn', 'Hafeld', 'BjørnHafeld@gmail.com', 12345678, 'Borti Sjægget', 1000, '2011-02-02', 1),
(9, 'trineols', 0, 'vvx+BLVq4Do=', 'ep/9fK1A40vwtn33/dSEz+CnRN8=', 'Trine', 'Olsen', 'trineliseolsen@outlook.com', 65678, 'Atmed Elgan', 20, '2014-02-02', 1),
(13, 'jensern', 0, 'wiA7nXBVg4I=', 'TsPyO8W6wBda3Fl4EawyuYNzBGk=', 'Jens Tobias', 'Kaarud', 'jenskaarud@gmail.com', NULL, 'Norge', 8400, NULL, 1);
SET FOREIGN_KEY_CHECKS=1;
