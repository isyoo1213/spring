package spring.oop.singleton;

class StatefulService {

    private int price; //상태를 유지하는 필드

    public void order(String name, int price){
        System.out.println("name = " + name + "\nprice = " + price);
        this.price = price;
    }

    public int getPrice(){
        return price;
    }

}
