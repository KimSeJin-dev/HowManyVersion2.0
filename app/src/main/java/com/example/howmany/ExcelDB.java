package com.example.howmany;



//사람들 출입정보 들어있는 class
public class ExcelDB {

        private String name; // 이름
        private String major; // 학과
        private String phone_num; // 핸드폰번호
        private String student_num; // 학번
        private String email; // 이메일
        private String reserve_product; // 월

        public ExcelDB(){}

        public ExcelDB(String name, String major, String phone_num, String student_num, String email,String reserve_product){
           this.name = name;
            this.major = major;
            this.phone_num = phone_num;
            this.student_num = student_num;
            this.email = email;
            this.reserve_product = reserve_product;

        }

        public String getName() {return name;}
        public String getMajor() { return major;}
        public String getPhone_num(){return phone_num;}
        public String getStudent_num(){return student_num;}
        public String getEmail(){return email;}
        public String getReserve_product(){return reserve_product;}

}
