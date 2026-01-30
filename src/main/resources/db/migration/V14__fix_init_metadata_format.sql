-- Fix test_cases 1-5 init_metadata to use proper column names
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"김철수","age":25},{"id":2,"name":"이영희","age":30},{"id":3,"name":"박민수","age":22}]}]}' WHERE id = 1;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"홍길동","age":28}]}]}' WHERE id = 2;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"Alice","age":20},{"id":2,"name":"Bob","age":25},{"id":3,"name":"Charlie","age":30},{"id":4,"name":"Diana","age":35},{"id":5,"name":"Eve","age":40}]}]}' WHERE id = 3;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":100,"name":"테스트","age":99}]}]}' WHERE id = 4;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"A","age":1},{"id":2,"name":"B","age":2}]}]}' WHERE id = 5;
