/**********************************************************************************************************************/

-- Select the foods AND their ingredients: food_id, ingredient_id, Food Price, Ingredient Price, Food Name, Ingredient Name, Ingredient Amount(in said food), Ingredient Stock, Alergens,
DROP VIEW IF EXISTS foods_ingredients;
CREATE VIEW foods_ingredients AS
  SELECT food_id,HCL_ingredient.ingredient_id,HCL_food.price'Food Price',HCL_ingredient.purchase_price'Ingredient Price',HCL_food.name'Food Name',HCL_ingredient.name'Ingredient Name',
                                              number'Ingredient Amount', stock'Ingredient Stock',nuts,gluten,lactose
  FROM HCL_food NATURAL JOIN HCL_food_ingredient JOIN HCL_ingredient ON(HCL_food_ingredient.ingredient_id=HCL_ingredient.ingredient_id)
  WHERE HCL_food.active=1 AND HCL_food_ingredient.active=1 AND HCL_ingredient.active=1;
-- Select the foods AND their ingredients: food_id, ingredient_id, Food Price, Ingredient Price, Food Name, Ingredient Name, Ingredient Amount(in said food), Ingredient Stock, Alergens,

/**********************************************************************************************************************/

-- Deliveries AND their Food: Order_id, delivery_id, food_id, prices, Food Name, Food Number, adress, postnr, delivery date
DROP VIEW IF EXISTS deliveries_foods;
CREATE VIEW deliveries_foods AS
  SELECT HCL_order.order_id,delivery_id,HCL_food.food_id,HCL_order.price`order_price`,HCL_food.price`food_price`,name,number,adress,postnr,delivery_date
  FROM HCL_deliveries NATURAL JOIN HCL_order JOIN HCL_order_food ON(HCL_order.order_id = HCL_order_food.order_id)
    JOIN HCL_food ON(HCL_order_food.food_id = HCL_food.food_id)
  WHERE HCL_deliveries.active=1 AND HCL_order.active=1 AND HCL_order_food.active=1 AND HCL_food.active=1
        AND HCL_deliveries.delivered=0;
-- Deliveries AND their Food: Order_id, delivery_id, food_id, prices, Food Name, Food Number, adress, postnr, delivery date

/**********************************************************************************************************************/

-- Deliveries, their foods, and their ingredients
DROP VIEW IF EXISTS deliveries_foods_ingredients;
CREATE VIEW deliveries_foods_ingredients AS
  SELECT delivery_id, food_id, ingredient_id,`Food Name`,`Ingredient Name`,deliveries_foods.number`Food Number`,
    `Ingredient Amount`,food_price'Food Price',`Ingredient Price`,`Ingredient Stock`, delivery_date
  FROM deliveries_foods NATURAL JOIN foods_ingredients ORDER BY delivery_date;
-- Deliveries, their foods, and their ingredients

/**********************************************************************************************************************/

-- Ingredients per delivery. AND total ingredients of that type in the delivery, with the stock
DROP VIEW IF EXISTS deliveries_ingredients_total;
CREATE VIEW deliveries_ingredients_total AS
  SELECT delivery_date,delivery_id, ingredient_id,`Ingredient Name`,Sum(`Food Number`*`Ingredient Amount`)'Total Ingredients',`Ingredient Stock`
  FROM deliveries_foods_ingredients GROUP BY delivery_id;
-- Ingredients per delivery. AND total ingredients of that type in the delivery, with the stock

/**********************************************************************************************************************/