package spring.oop;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ForLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        ForLombok lombok = new ForLombok();
        lombok.setAge(32);
        int age = lombok.getAge();
        System.out.println("age = " + age);
        System.out.println("lombok = " + lombok);
    }

}
