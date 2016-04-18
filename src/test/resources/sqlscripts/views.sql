-- --------------------------------------------------------
--
-- Structure for view `foods_ingredients`
--
DROP VIEW IF EXISTS `foods_ingredients`;

CREATE VIEW `foods_ingredients` AS select `HCL_food`.`food_id` AS `food_id`,`HCL_food`.`name` AS `Food Name`,`HCL_food`.`price` AS `Sales Price`,`HCL_ingredient`.`ingredient_id` AS `ingredient_id`,`HCL_ingredient`.`name` AS `Ingredient Name`,`HCL_food_ingredient`.`number` AS `Ingredients`,`HCL_ingredient`.`stock` AS `Stock`,`HCL_ingredient`.`purchase_price` AS `Buy Price` from ((`HCL_food` join `HCL_food_ingredient` on((`HCL_food`.`food_id` = `HCL_food_ingredient`.`food_id`))) join `HCL_ingredient` on((`HCL_food_ingredient`.`ingredient_id` = `HCL_ingredient`.`ingredient_id`)));

-- --------------------------------------------------------

--
-- Structure for view `ingredients_in_food`
--
DROP VIEW IF EXISTS `ingredients_in_food`;

CREATE VIEW `ingredients_in_food` AS (select `HCL_food`.`food_id` AS `food_id`,`HCL_food`.`name` AS `food_name`,`HCL_ingredient`.`ingredient_id` AS `ingredient_id`,`HCL_ingredient`.`name` AS `ingredient.name`,`HCL_food_ingredient`.`number` AS `number`,`HCL_ingredient`.`stock` AS `stock` from ((`HCL_food` join `HCL_food_ingredient` on((`HCL_food`.`food_id` = `HCL_food_ingredient`.`food_id`))) join `HCL_ingredient` on((`HCL_food_ingredient`.`ingredient_id` = `HCL_ingredient`.`ingredient_id`))));

-- --------------------------------------------------------

--
-- Structure for view `orders_foods`
--
DROP VIEW IF EXISTS `orders_foods`;

CREATE VIEW `orders_foods` AS select `HCL_order`.`order_id` AS `order_id`,`HCL_order_food`.`food_id` AS `food_id`,`HCL_order_food`.`number` AS `Food Items`,`HCL_food`.`name` AS `Food Name`,`HCL_food`.`price` AS `Sales Price`,`HCL_order`.`delivery_date` AS `delivery_date` from ((`HCL_order` join `HCL_order_food` on((`HCL_order`.`order_id` = `HCL_order_food`.`order_id`))) join `HCL_food` on((`HCL_order_food`.`food_id` = `HCL_food`.`food_id`)));

-- --------------------------------------------------------


--
-- Structure for view `orders_ingredients`
--
DROP VIEW IF EXISTS `orders_ingredients`;

CREATE VIEW `orders_ingredients` AS select `orders_foods`.`food_id` AS `food_id`,`orders_foods`.`Food Name` AS `Food Name`,`orders_foods`.`Sales Price` AS `Sales Price`,`orders_foods`.`order_id` AS `order_id`,`orders_foods`.`Food Items` AS `Food Items`,`orders_foods`.`delivery_date` AS `delivery_date`,`foods_ingredients`.`ingredient_id` AS `ingredient_id`,`foods_ingredients`.`Ingredient Name` AS `Ingredient Name`,`foods_ingredients`.`Ingredients` AS `Ingredients`,`foods_ingredients`.`Stock` AS `Stock`,`foods_ingredients`.`Buy Price` AS `Buy Price` from (`orders_foods` join `foods_ingredients` on(((`orders_foods`.`food_id` = `foods_ingredients`.`food_id`) and (`orders_foods`.`Food Name` = `foods_ingredients`.`Food Name`) and (`orders_foods`.`Sales Price` = `foods_ingredients`.`Sales Price`))));

-- --------------------------------------------------------
--
-- Structure for view `ingredients_to_buy`
--
DROP VIEW IF EXISTS `ingredients_to_buy`;

CREATE VIEW `ingredients_to_buy` AS select `orders_ingredients`.`delivery_date` AS `delivery_date`,`orders_ingredients`.`ingredient_id` AS `ingredient_id`,`orders_ingredients`.`Ingredient Name` AS `Ingredient Name`,sum((`orders_ingredients`.`Food Items` * `orders_ingredients`.`Ingredients`)) AS `To Buy`,`orders_ingredients`.`Stock` AS `Stock` from `orders_ingredients` group by `orders_ingredients`.`order_id`,`orders_ingredients`.`ingredient_id`;

-- --------------------------------------------------------
--
-- Structure for view `ingredients_to_buy_minus_stock`
--
DROP VIEW IF EXISTS `ingredients_to_buy_minus_stock`;

CREATE VIEW `ingredients_to_buy_minus_stock` AS select `ingredients_to_buy`.`delivery_date` AS `delivery_date`,`ingredients_to_buy`.`ingredient_id` AS `ingredient_id`,`ingredients_to_buy`.`Ingredient Name` AS `Ingredient Name`,`ingredients_to_buy`.`To Buy` AS `To Buy`,`ingredients_to_buy`.`Stock` AS `Stock`,(`ingredients_to_buy`.`To Buy` - `ingredients_to_buy`.`Stock`) AS `To Buy - Stock` from `ingredients_to_buy` where (`ingredients_to_buy`.`To Buy` > 0);

-- --------------------------------------------------------

--
-- Structure for view `ingredients_to_buy_summed`
--
DROP VIEW IF EXISTS `ingredients_to_buy_summed`;

CREATE VIEW `ingredients_to_buy_summed` AS select `ingredients_to_buy_minus_stock`.`delivery_date` AS `delivery_date`,`ingredients_to_buy_minus_stock`.`ingredient_id` AS `ingredient_id`,`ingredients_to_buy_minus_stock`.`Ingredient Name` AS `Ingredient Name`,sum(`ingredients_to_buy_minus_stock`.`To Buy`) AS `Total to buy this date`,`ingredients_to_buy_minus_stock`.`Stock` AS `Stock` from `ingredients_to_buy_minus_stock` group by `ingredients_to_buy_minus_stock`.`ingredient_id`,`ingredients_to_buy_minus_stock`.`delivery_date`;

-- --------------------------------------------------------

--
-- Structure for view `ingredient_to_buy_summed`
--
DROP VIEW IF EXISTS `ingredient_to_buy_summed`;

CREATE VIEW `ingredient_to_buy_summed` AS select `ingredients_to_buy_minus_stock`.`delivery_date` AS `delivery_date`,`ingredients_to_buy_minus_stock`.`ingredient_id` AS `ingredient_id`,`ingredients_to_buy_minus_stock`.`Ingredient Name` AS `Ingredient Name`,sum(`ingredients_to_buy_minus_stock`.`To Buy`) AS `Total to buy this date`,`ingredients_to_buy_minus_stock`.`Stock` AS `Stock` from `ingredients_to_buy_minus_stock` group by `ingredients_to_buy_minus_stock`.`ingredient_id`,`ingredients_to_buy_minus_stock`.`delivery_date`;

-- --------------------------------------------------------


--
-- Structure for view `ingredients_to_buy_over_zero`
--
DROP VIEW IF EXISTS `ingredients_to_buy_over_zero`;

CREATE VIEW `ingredients_to_buy_over_zero` AS select `ingredients_to_buy_minus_stock`.`delivery_date` AS `delivery_date`,`ingredients_to_buy_minus_stock`.`ingredient_id` AS `Ingredient ID`,`ingredients_to_buy_minus_stock`.`Ingredient Name` AS `Ingredient Name`,`ingredients_to_buy_minus_stock`.`To Buy - Stock` AS `To Buy`,`ingredients_to_buy_minus_stock`.`Stock` AS `Stock` from `ingredients_to_buy_minus_stock` where (`ingredients_to_buy_minus_stock`.`To Buy - Stock` > 0);

-- --------------------------------------------------------

--
-- Structure for view `ingredients_to_buy_summed_minus_stock`
--
DROP VIEW IF EXISTS `ingredients_to_buy_summed_minus_stock`;

CREATE VIEW `ingredients_to_buy_summed_minus_stock` AS select `ingredient_to_buy_summed`.`delivery_date` AS `delivery_date`,`ingredient_to_buy_summed`.`ingredient_id` AS `ingredient_id`,`ingredient_to_buy_summed`.`Ingredient Name` AS `Ingredient Name`,(`ingredient_to_buy_summed`.`Total to buy this date` - `ingredient_to_buy_summed`.`Stock`) AS `Total minus Stock` from `ingredient_to_buy_summed`;

-- --------------------------------------------------------

--
-- Structure for view `ingredients_to_buy_summed_minus_stock_in_range`
--
DROP VIEW IF EXISTS `ingredients_to_buy_summed_minus_stock_in_range`;

CREATE VIEW `ingredients_to_buy_summed_minus_stock_in_range` AS select `ingredients_to_buy_summed_minus_stock`.`delivery_date` AS `delivery_date`,`ingredients_to_buy_summed_minus_stock`.`ingredient_id` AS `ingredient_id`,`ingredients_to_buy_summed_minus_stock`.`Ingredient Name` AS `Ingredient Name`,`ingredients_to_buy_summed_minus_stock`.`Total minus Stock` AS `Total minus Stock` from `ingredients_to_buy_summed_minus_stock` where ((`ingredients_to_buy_summed_minus_stock`.`Total minus Stock` > 0) and (`ingredients_to_buy_summed_minus_stock`.`delivery_date` between curdate() and (curdate() + interval 365 day)));

-- --------------------------------------------------------

--
-- Structure for view `orders_dates_ingredients`
--
DROP VIEW IF EXISTS `orders_dates_ingredients`;

CREATE VIEW `orders_dates_ingredients` AS select `orders_ingredients`.`delivery_date` AS `delivery_date`,`orders_ingredients`.`ingredient_id` AS `ingredient_id`,`orders_ingredients`.`Ingredient Name` AS `Ingredient Name`,(`orders_ingredients`.`Food Items` * `orders_ingredients`.`Ingredients`) AS `Amount` from `orders_ingredients`;

-- --------------------------------------------------------

--
-- Structure for view `orders_within_date`
--
DROP VIEW IF EXISTS `orders_within_date`;

CREATE VIEW `orders_within_date` AS select `HCL_order`.`order_id` AS `order_id` from `HCL_order` where (`HCL_order`.`delivery_date` between curdate() and (curdate() + interval 365 day));

-- --------------------------------------------------------

--
-- Structure for view `orders_ingredients_within_date`
--
DROP VIEW IF EXISTS `orders_ingredients_within_date`;

CREATE VIEW `orders_ingredients_within_date` AS select `orders_ingredients`.`food_id` AS `food_id`,`orders_ingredients`.`Food Name` AS `Food Name`,`orders_ingredients`.`Sales Price` AS `Sales Price`,`orders_ingredients`.`order_id` AS `order_id`,`orders_ingredients`.`Food Items` AS `Food Items`,`orders_ingredients`.`ingredient_id` AS `ingredient_id`,`orders_ingredients`.`Ingredient Name` AS `Ingredient Name`,`orders_ingredients`.`Ingredients` AS `Ingredients`,`orders_ingredients`.`Stock` AS `Stock`,`orders_ingredients`.`Buy Price` AS `Buy Price` from `orders_ingredients` where `orders_ingredients`.`order_id` in (select `orders_within_date`.`order_id` from `orders_within_date`);

-- --------------------------------------------------------