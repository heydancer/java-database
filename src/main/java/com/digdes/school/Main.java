package com.digdes.school;

public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter javaSchoolStarter = new JavaSchoolStarter();

        try {
            javaSchoolStarter
                    .execute("INSERT VALUES 'lastName'='Федоров', 'id'=1, 'age'=65, 'cost'=10.0,'active'=true");
            javaSchoolStarter
                    .execute("INSERT VALUES 'lastName'='Иванов', 'id'=2, 'age'=43, 'cost'=113.0, 'active'=true");
            javaSchoolStarter
                    .execute("INSERT VALUES 'lastName'='Устьянцев', 'id'=3, 'age'=28,'cost'=5.0, 'active'=false");
            javaSchoolStarter
                    .execute("INSERT VALUES 'lastName'='Плотникова', 'id' = 4, 'age'=27, 'cost'=123.0, 'active'=false");

            System.out.println("---------------------Выводим всех пользователей----------------------");

            System.out.println(javaSchoolStarter.execute("SELECT") + "\n");

            System.out.println("--------Выводим пользователей где фамилия Федоров или(и) id=4--------");

            System.out.println(javaSchoolStarter
                    .execute("SELECT WHERE 'lastName' = 'Федоров' OR 'id' = 4") + "\n");

            System.out.println("--Выводим пользователей где фамилия Иванов и возраст 43 или(и) id=1--");

            System.out.println(javaSchoolStarter
                    .execute("SELECT WHERE 'lastName' = 'Иванов' AND 'age' = 43 OR 'id' = 1") + "\n");

            System.out.println("----Выводим пользоватей где id=4 или(и) фамилия содержит 'янцев'-----");

            System.out.println(javaSchoolStarter
                    .execute("SELECT WHERE 'id' = 4 OR 'lastName' LIKE %янцев%") + "\n");

            System.out.println("----------Обновляем поле cost у пользователей чей age = 43-----------");

            javaSchoolStarter.execute("UPDATE VALUES 'cost' = 10000.0 WHERE 'age' = 43");

            System.out.println(javaSchoolStarter.execute("SELECT WHERE 'age' = 43") + "\n");

            System.out.println("--------------------Удаляем строки где age > 28----------------------");

            javaSchoolStarter.execute("DELETE WHERE 'age' > 28");

            System.out.println(javaSchoolStarter.execute("SELECT"));

            System.out.println("----------------------Удаляем таблицу--------------------------------");

            javaSchoolStarter.execute("DELETE");

            System.out.println("---------------------Получаем пустую таблицу-------------------------");

            System.out.println(javaSchoolStarter.execute("SELECT"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}