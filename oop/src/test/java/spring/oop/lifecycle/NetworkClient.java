package spring.oop.lifecycle;

class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
        connect();
        call("초기화 연결 메시지");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect(){
        System.out.println("connection: " + url);
    }

    public void call(String message){
        System.out.println("call: " + url + "\nmessage = " + message);
    }

    //서비스 종료시 호출
    public void disconnection(){
        System.out.println("close: " + url);
    }
}
