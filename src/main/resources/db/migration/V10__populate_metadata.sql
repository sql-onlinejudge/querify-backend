-- Problem 1: users (id INT PRIMARY KEY, name VARCHAR(100), age INT)
UPDATE problems SET schema_metadata = '{"tables":[{"name":"users","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]},{"name":"age","type":"INT","constraints":[]}]}]}' WHERE id = 1;

-- Problem 2: users (id INT PRIMARY KEY, name VARCHAR(100), age INT)
UPDATE problems SET schema_metadata = '{"tables":[{"name":"users","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]},{"name":"age","type":"INT","constraints":[]}]}]}' WHERE id = 2;

-- Problem 3: users (id INT PRIMARY KEY, name VARCHAR(100), age INT)
UPDATE problems SET schema_metadata = '{"tables":[{"name":"users","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]},{"name":"age","type":"INT","constraints":[]}]}]}' WHERE id = 3;

-- Problem 4: users + orders
UPDATE problems SET schema_metadata = '{"tables":[{"name":"users","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]}]},{"name":"orders","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"user_id","type":"INT","constraints":[]},{"name":"amount","type":"INT","constraints":[]}]}]}' WHERE id = 4;

-- Problem 5: products (id INT PRIMARY KEY, name VARCHAR(100), price INT)
UPDATE problems SET schema_metadata = '{"tables":[{"name":"products","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]},{"name":"price","type":"INT","constraints":[]}]}]}' WHERE id = 5;

-- Problem 6: products (id INT PRIMARY KEY, name VARCHAR(100), category VARCHAR(50))
UPDATE problems SET schema_metadata = '{"tables":[{"name":"products","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]},{"name":"category","type":"VARCHAR(50)","constraints":[]}]}]}' WHERE id = 6;

-- Problem 7: users + orders (product)
UPDATE problems SET schema_metadata = '{"tables":[{"name":"users","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]}]},{"name":"orders","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"user_id","type":"INT","constraints":[]},{"name":"product","type":"VARCHAR(100)","constraints":[]}]}]}' WHERE id = 7;

-- Problem 8: users (id INT PRIMARY KEY, name VARCHAR(100), age INT)
UPDATE problems SET schema_metadata = '{"tables":[{"name":"users","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]},{"name":"age","type":"INT","constraints":[]}]}]}' WHERE id = 8;

-- Problem 9: orders (id INT PRIMARY KEY, user_id INT, amount INT)
UPDATE problems SET schema_metadata = '{"tables":[{"name":"orders","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"user_id","type":"INT","constraints":[]},{"name":"amount","type":"INT","constraints":[]}]}]}' WHERE id = 9;

-- Problem 10: users + orders
UPDATE problems SET schema_metadata = '{"tables":[{"name":"users","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]}]},{"name":"orders","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"user_id","type":"INT","constraints":[]}]}]}' WHERE id = 10;

-- Problem 11: employees (id INT PRIMARY KEY, name VARCHAR(100), dept VARCHAR(50), salary INT)
UPDATE problems SET schema_metadata = '{"tables":[{"name":"employees","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]},{"name":"dept","type":"VARCHAR(50)","constraints":[]},{"name":"salary","type":"INT","constraints":[]}]}]}' WHERE id = 11;

-- Problem 12: employees (id INT PRIMARY KEY, name VARCHAR(100), manager_id INT)
UPDATE problems SET schema_metadata = '{"tables":[{"name":"employees","columns":[{"name":"id","type":"INT","constraints":["PRIMARY","KEY"]},{"name":"name","type":"VARCHAR(100)","constraints":[]},{"name":"manager_id","type":"INT","constraints":[]}]}]}' WHERE id = 12;

-- Test Cases init_metadata (sample for first few)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"col0":1,"col1":"김철수","col2":25},{"col0":2,"col1":"이영희","col2":30},{"col0":3,"col1":"박민수","col2":22}]}]}' WHERE id = 1;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"col0":1,"col1":"홍길동","col2":28}]}]}' WHERE id = 2;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"col0":1,"col1":"Alice","col2":20},{"col0":2,"col1":"Bob","col2":25},{"col0":3,"col1":"Charlie","col2":30},{"col0":4,"col1":"Diana","col2":35},{"col0":5,"col1":"Eve","col2":40}]}]}' WHERE id = 3;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"col0":100,"col1":"테스트","col2":99}]}]}' WHERE id = 4;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"col0":1,"col1":"A","col2":1},{"col0":2,"col1":"B","col2":2}]}]}' WHERE id = 5;
