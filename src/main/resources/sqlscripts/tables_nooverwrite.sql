SET FOREIGN_KEY_CHECKS=1;
-- --------------------------------------------------------

--
-- Table structure for table `HCL_customer`
--

-- DROP TABLE `HCL_customer`;
CREATE TABLE IF NOT EXISTS `HCL_customer` (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(50) DEFAULT NULL,
  `epost` varchar(50) DEFAULT NULL,
  `tlf` int(11) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_food`
--

-- DROP TABLE `HCL_food`;
CREATE TABLE IF NOT EXISTS `HCL_food` (
  `food_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`food_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_food_ingredient`
--

-- DROP TABLE `HCL_food_ingredient`;
CREATE TABLE IF NOT EXISTS `HCL_food_ingredient` (
  `food_id` int(11) NOT NULL DEFAULT '0',
  `ingredient_id` int(11) NOT NULL DEFAULT '0',
  `number` int(11) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`food_id`,`ingredient_id`),
  KEY `ingredient_id` (`ingredient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_ingredient`
--

-- DROP TABLE `HCL_ingredient`;
CREATE TABLE IF NOT EXISTS `HCL_ingredient` (
  `ingredient_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `purchase_price` int(11) DEFAULT NULL,
  `nuts` bit(1) DEFAULT NULL,
  `gluten` bit(1) DEFAULT NULL,
  `lactose` bit(1) DEFAULT NULL,
  `other` varchar(100) DEFAULT NULL,
  `purchase_date` date DEFAULT NULL,
  `expiration_date` date DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ingredient_id`),
  CONSTRAINT `HCL_food_ingredient_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `HCL_food` (`food_id`) ON UPDATE CASCADE,
  CONSTRAINT `HCL_food_ingredient_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `HCL_ingredient` (`ingredient_id`) ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_order`
--

-- DROP TABLE `HCL_order`;
CREATE TABLE IF NOT EXISTS `HCL_order` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL,
  `price` int(11) DEFAULT NULL,
  `adress` varchar(50) DEFAULT NULL,
  `postnr` varchar(50) DEFAULT NULL,
  `order_date` date DEFAULT NULL,
  `delivery_date` date DEFAULT NULL,
  `delivered` bit(1) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`order_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `HCL_order_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `HCL_customer` (`customer_id`) ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_order_food`
--

-- DROP TABLE `HCL_order_food`;
CREATE TABLE IF NOT EXISTS `HCL_order_food` (
  `order_id` int(11) NOT NULL DEFAULT '0',
  `food_id` int(11) NOT NULL DEFAULT '0',
  `number` int(11) DEFAULT '1',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`order_id`,`food_id`),
  KEY `food_id` (`food_id`),
  CONSTRAINT `HCL_order_food_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `HCL_order` (`order_id`) ON UPDATE CASCADE,
  CONSTRAINT `HCL_order_food_ibfk_2` FOREIGN KEY (`food_id`) REFERENCES `HCL_food` (`food_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_order_package`
--

-- DROP TABLE `HCL_order_package`;
CREATE TABLE IF NOT EXISTS `HCL_order_package` (
  `order_id` int(11) NOT NULL DEFAULT '0',
  `package_id` int(11) NOT NULL DEFAULT '0',
  `number` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`order_id`,`package_id`),
  KEY `package_id` (`package_id`),
  CONSTRAINT `HCL_order_package_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `HCL_order` (`order_id`) ON UPDATE CASCADE,
  CONSTRAINT `HCL_order_package_ibfk_2` FOREIGN KEY (`package_id`) REFERENCES `HCL_package` (`package_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_package`
--

-- DROP TABLE `HCL_package`;
CREATE TABLE IF NOT EXISTS `HCL_package` (
  `package_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`package_id`),
  CONSTRAINT `HCL_package_food_ibfk_1` FOREIGN KEY (`package_id`) REFERENCES `HCL_package` (`package_id`) ON UPDATE CASCADE,
  CONSTRAINT `HCL_package_food_ibfk_2` FOREIGN KEY (`food_id`) REFERENCES `HCL_food` (`food_id`) ON UPDATE CASCADE
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_package_food`
--

-- DROP TABLE `HCL_package_food`;
CREATE TABLE IF NOT EXISTS `HCL_package_food` (
  `package_id` int(11) NOT NULL DEFAULT '0',
  `food_id` int(11) NOT NULL DEFAULT '0',
  `number` int(11) DEFAULT '1',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`food_id`,`package_id`),
  KEY `package_id` (`package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_subscription`
--

-- DROP TABLE `HCL_subscription`;
CREATE TABLE IF NOT EXISTS `HCL_subscription` (
  `order_id` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`order_id`),
  CONSTRAINT `HCL_subscription_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `HCL_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_subscription_date`
--

-- DROP TABLE `HCL_subscription_date`;
CREATE TABLE IF NOT EXISTS `HCL_subscription_date` (
  `date_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `dato` date NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`date_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `HCL_subscription_date_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `HCL_subscription` (`order_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `HCL_users`
--

-- DROP TABLE `HCL_users`;
CREATE TABLE IF NOT EXISTS `HCL_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `user_role` int(11) NOT NULL,
  `user_salt` varchar(50) NOT NULL,
  `user_pass` varchar(50) NOT NULL,
  `user_firstname` varchar(50) DEFAULT NULL,
  `user_lastname` varchar(50) DEFAULT NULL,
  `user_email` varchar(50) DEFAULT NULL,
  `user_tlf` int(11) DEFAULT NULL,
  `user_adress` varchar(50) DEFAULT NULL,
  `user_postnr` int(11) DEFAULT NULL,
  `user_start` date DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

-- --------------------------------------------------------
SET FOREIGN_KEY_CHECKS=1;