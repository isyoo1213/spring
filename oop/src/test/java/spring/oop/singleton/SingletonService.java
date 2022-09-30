package spring.oop.singleton;

public class SingletonService {

    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    private SingletonService(){ //생성자를 private으로 선언해 외부에서 객체생성할 수 없도록 제한
    }

    public void logic(){
        System.out.println("싱글톤 객체 로직 호출");
    }

}
