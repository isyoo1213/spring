package spring.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

//"127.0.0.1" - 이와 같은 String 타입의 IpPort를 IpPort 타입으로 바로 변환해서 받고 싶은 상황
@Getter
@EqualsAndHashCode
public class IpPort {
    private String ip;
    private int port;

    public IpPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
