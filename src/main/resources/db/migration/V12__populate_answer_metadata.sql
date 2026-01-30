-- Problem 1: 회원 전체 목록 조회하기 (test_cases 1-5)
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"김철수",25],[2,"이영희",30],[3,"박민수",22]]}' WHERE id = 1;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"홍길동",28]]}' WHERE id = 2;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"Alice",20],[2,"Bob",25],[3,"Charlie",30],[4,"Diana",35],[5,"Eve",40]]}' WHERE id = 3;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[100,"테스트",99]]}' WHERE id = 4;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"A",1],[2,"B",2]]}' WHERE id = 5;

-- Problem 2: 성인 회원 필터링 (test_cases 6-10)
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"김철수",25],[3,"박민수",30]]}' WHERE id = 6;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[2,"김영수",20]]}' WHERE id = 7;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"A",20],[2,"B",21],[3,"C",22]]}' WHERE id = 8;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[3,"성인",25]]}' WHERE id = 9;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"경계값",20]]}' WHERE id = 10;

-- Problem 3: 회원 이름순 정렬 (test_cases 11-15)
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[2,"Alice",30],[3,"Bob",22],[1,"Charlie",25]]}' WHERE id = 11;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[2,"Amy",25],[1,"Zoe",20]]}' WHERE id = 12;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"가",20],[2,"나",25],[3,"다",30]]}' WHERE id = 13;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[2,"Anna",32],[3,"Brian",45],[4,"Chris",23],[1,"David",28]]}' WHERE id = 14;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"Single",100]]}' WHERE id = 15;

-- Problem 4: 고객별 총 주문금액 계산 (test_cases 16-17)
UPDATE test_cases SET answer_metadata = '{"columns":["user_id","name","total_amount"],"rows":[[1,"김철수",300],[2,"이영희",150]]}' WHERE id = 16;
UPDATE test_cases SET answer_metadata = '{"columns":["user_id","name","total_amount"],"rows":[[1,"홍길동",150]]}' WHERE id = 17;

-- Problem 5: 최고가 상품 찾기 (test_cases 18-22)
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","price"],"rows":[[3,"체리",2000]]}' WHERE id = 18;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","price"],"rows":[[2,"노트북",1000000]]}' WHERE id = 19;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","price"],"rows":[[3,"공책",1000]]}' WHERE id = 20;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","price"],"rows":[[1,"유일상품",9999]]}' WHERE id = 21;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","price"],"rows":[[5,"E",500]]}' WHERE id = 22;

-- Problem 6: 카테고리별 상품 통계 (test_cases 23-24)
UPDATE test_cases SET answer_metadata = '{"columns":["category","count"],"rows":[["과일",2],["채소",1]]}' WHERE id = 23;
UPDATE test_cases SET answer_metadata = '{"columns":["category","count"],"rows":[["의류",1],["전자제품",2]]}' WHERE id = 24;

-- Problem 7: 주문 내역 상세 조회 (test_cases 25-26)
UPDATE test_cases SET answer_metadata = '{"columns":["user_id","user_name","order_id","user_id","product"],"rows":[[1,"김철수",1,1,"스마트폰"],[2,"이영희",2,2,"노트북"]]}' WHERE id = 25;
UPDATE test_cases SET answer_metadata = '{"columns":["user_id","user_name","order_id","user_id","product"],"rows":[[1,"홍길동",1,1,"책"],[1,"홍길동",2,1,"펜"]]}' WHERE id = 26;

-- Problem 8: 평균 이상 고객 찾기 (test_cases 27-28)
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[2,"이영희",30]]}' WHERE id = 27;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","age"],"rows":[[1,"A",40]]}' WHERE id = 28;

-- Problem 9: 우수 고객 선별 (test_cases 29-30)
UPDATE test_cases SET answer_metadata = '{"columns":["user_id"],"rows":[[1]]}' WHERE id = 29;
UPDATE test_cases SET answer_metadata = '{"columns":["user_id"],"rows":[[2]]}' WHERE id = 30;

-- Problem 10: 미주문 고객 찾기 (test_cases 31-35)
UPDATE test_cases SET answer_metadata = '{"columns":["id","name"],"rows":[[2,"이영희"],[3,"박민수"]]}' WHERE id = 31;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name"],"rows":[[1,"홍길동"]]}' WHERE id = 32;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name"],"rows":[[1,"A"],[2,"B"],[3,"C"],[4,"D"]]}' WHERE id = 33;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name"],"rows":[[2,"미주문자"]]}' WHERE id = 34;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name"],"rows":[]}' WHERE id = 35;

-- Problem 11: 부서별 급여 순위 (test_cases 36-37)
UPDATE test_cases SET answer_metadata = '{"columns":["name","dept","salary","rank"],"rows":[["이영희","영업",6000,1],["김철수","영업",5000,2],["박민수","개발",7000,1]]}' WHERE id = 36;
UPDATE test_cases SET answer_metadata = '{"columns":["name","dept","salary","rank"],"rows":[["B","인사",4500,1],["A","인사",4000,2]]}' WHERE id = 37;

-- Problem 12: 조직도 탐색 (test_cases 38-39)
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","manager_id"],"rows":[[2,"팀장",1],[3,"개발자",2]]}' WHERE id = 38;
UPDATE test_cases SET answer_metadata = '{"columns":["id","name","manager_id"],"rows":[[2,"본부장",1],[3,"팀장",2],[4,"사원",3]]}' WHERE id = 39;
