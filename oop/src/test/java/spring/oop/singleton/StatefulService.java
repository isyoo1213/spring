package spring.oop.singleton;

class StatefulService {

//    private int price; //상태를 유지하는 필드


//    public void order(String name, int price){
//        System.out.println("name = " + name + "\nprice = " + price);
//        this.price = price;
//    }

    /**
     * '반환값'을 통해 order() 메서드의 price parameter 대한 의존성을 제거 --> non-stateful
     * @param name
     * @param price
     * @return
     */
    public int order(String name, int price){
        System.out.println("name = " + name + "\nprice = " + price);
        return price;
    }

//    public int getPrice(){
//        return price;
//    }

}
