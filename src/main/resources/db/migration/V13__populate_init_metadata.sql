-- Problem 2: 성인 회원 필터링 (test_cases 6-10)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"김철수","age":25},{"id":2,"name":"이영희","age":18},{"id":3,"name":"박민수","age":30}]}]}' WHERE id = 6;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"홍길동","age":19},{"id":2,"name":"김영수","age":20}]}]}' WHERE id = 7;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"A","age":20},{"id":2,"name":"B","age":21},{"id":3,"name":"C","age":22}]}]}' WHERE id = 8;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"미성년자","age":15},{"id":2,"name":"청소년","age":17},{"id":3,"name":"성인","age":25}]}]}' WHERE id = 9;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"경계값","age":20}]}]}' WHERE id = 10;

-- Problem 3: 회원 이름순 정렬 (test_cases 11-15)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"Charlie","age":25},{"id":2,"name":"Alice","age":30},{"id":3,"name":"Bob","age":22}]}]}' WHERE id = 11;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"Zoe","age":20},{"id":2,"name":"Amy","age":25}]}]}' WHERE id = 12;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"가","age":20},{"id":2,"name":"나","age":25},{"id":3,"name":"다","age":30}]}]}' WHERE id = 13;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"David","age":28},{"id":2,"name":"Anna","age":32},{"id":3,"name":"Brian","age":45},{"id":4,"name":"Chris","age":23}]}]}' WHERE id = 14;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"Single","age":100}]}]}' WHERE id = 15;

-- Problem 4: 고객별 총 주문금액 계산 (test_cases 16-17)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"김철수"},{"id":2,"name":"이영희"}]},{"table":"orders","rows":[{"id":1,"user_id":1,"amount":100},{"id":2,"user_id":1,"amount":200},{"id":3,"user_id":2,"amount":150}]}]}' WHERE id = 16;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"홍길동"}]},{"table":"orders","rows":[{"id":1,"user_id":1,"amount":50},{"id":2,"user_id":1,"amount":50},{"id":3,"user_id":1,"amount":50}]}]}' WHERE id = 17;

-- Problem 5: 최고가 상품 찾기 (test_cases 18-22)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"products","rows":[{"id":1,"name":"사과","price":1000},{"id":2,"name":"바나나","price":500},{"id":3,"name":"체리","price":2000}]}]}' WHERE id = 18;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"products","rows":[{"id":1,"name":"스마트폰","price":500000},{"id":2,"name":"노트북","price":1000000}]}]}' WHERE id = 19;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"products","rows":[{"id":1,"name":"연필","price":500},{"id":2,"name":"지우개","price":300},{"id":3,"name":"공책","price":1000},{"id":4,"name":"볼펜","price":800}]}]}' WHERE id = 20;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"products","rows":[{"id":1,"name":"유일상품","price":9999}]}]}' WHERE id = 21;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"products","rows":[{"id":1,"name":"A","price":100},{"id":2,"name":"B","price":200},{"id":3,"name":"C","price":300},{"id":4,"name":"D","price":400},{"id":5,"name":"E","price":500}]}]}' WHERE id = 22;

-- Problem 6: 카테고리별 상품 통계 (test_cases 23-24)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"products","rows":[{"id":1,"name":"사과","category":"과일"},{"id":2,"name":"바나나","category":"과일"},{"id":3,"name":"당근","category":"채소"}]}]}' WHERE id = 23;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"products","rows":[{"id":1,"name":"스마트폰","category":"전자제품"},{"id":2,"name":"노트북","category":"전자제품"},{"id":3,"name":"셔츠","category":"의류"}]}]}' WHERE id = 24;

-- Problem 7: 주문 내역 상세 조회 (test_cases 25-26)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"김철수"},{"id":2,"name":"이영희"}]},{"table":"orders","rows":[{"id":1,"user_id":1,"product":"스마트폰"},{"id":2,"user_id":2,"product":"노트북"}]}]}' WHERE id = 25;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"홍길동"}]},{"table":"orders","rows":[{"id":1,"user_id":1,"product":"책"},{"id":2,"user_id":1,"product":"펜"}]}]}' WHERE id = 26;

-- Problem 8: 평균 이상 고객 찾기 (test_cases 27-28)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"김철수","age":25},{"id":2,"name":"이영희","age":30},{"id":3,"name":"박민수","age":20}]}]}' WHERE id = 27;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"A","age":40},{"id":2,"name":"B","age":20},{"id":3,"name":"C","age":30}]}]}' WHERE id = 28;

-- Problem 9: 우수 고객 선별 (test_cases 29-30)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"orders","rows":[{"id":1,"user_id":1,"amount":100},{"id":2,"user_id":1,"amount":200},{"id":3,"user_id":2,"amount":150}]}]}' WHERE id = 29;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"orders","rows":[{"id":1,"user_id":1,"amount":100},{"id":2,"user_id":2,"amount":200},{"id":3,"user_id":2,"amount":150},{"id":4,"user_id":2,"amount":100}]}]}' WHERE id = 30;

-- Problem 10: 미주문 고객 찾기 (test_cases 31-35)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"김철수"},{"id":2,"name":"이영희"},{"id":3,"name":"박민수"}]},{"table":"orders","rows":[{"id":1,"user_id":1},{"id":2,"user_id":1}]}]}' WHERE id = 31;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"홍길동"},{"id":2,"name":"김영수"}]},{"table":"orders","rows":[{"id":1,"user_id":2}]}]}' WHERE id = 32;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"A"},{"id":2,"name":"B"},{"id":3,"name":"C"},{"id":4,"name":"D"}]},{"table":"orders","rows":[]}]}' WHERE id = 33;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"주문자"},{"id":2,"name":"미주문자"}]},{"table":"orders","rows":[{"id":1,"user_id":1},{"id":2,"user_id":1},{"id":3,"user_id":1}]}]}' WHERE id = 34;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"users","rows":[{"id":1,"name":"Solo"}]},{"table":"orders","rows":[{"id":1,"user_id":1}]}]}' WHERE id = 35;

-- Problem 11: 부서별 급여 순위 (test_cases 36-37)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"employees","rows":[{"id":1,"name":"김철수","dept":"영업","salary":5000},{"id":2,"name":"이영희","dept":"영업","salary":6000},{"id":3,"name":"박민수","dept":"개발","salary":7000}]}]}' WHERE id = 36;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"employees","rows":[{"id":1,"name":"A","dept":"인사","salary":4000},{"id":2,"name":"B","dept":"인사","salary":4500}]}]}' WHERE id = 37;

-- Problem 12: 조직도 탐색 (test_cases 38-39)
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"employees","rows":[{"id":1,"name":"CEO","manager_id":null},{"id":2,"name":"팀장","manager_id":1},{"id":3,"name":"개발자","manager_id":2}]}]}' WHERE id = 38;
UPDATE test_cases SET init_metadata = '{"statements":[{"table":"employees","rows":[{"id":1,"name":"대표","manager_id":null},{"id":2,"name":"본부장","manager_id":1},{"id":3,"name":"팀장","manager_id":2},{"id":4,"name":"사원","manager_id":3}]}]}' WHERE id = 39;
