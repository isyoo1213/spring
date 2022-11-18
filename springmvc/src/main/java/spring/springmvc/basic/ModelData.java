package spring.springmvc.basic;

import lombok.Data;

@Data//@Getter @Setter @ToString @EqualsAndHashCode @RequiredArgsConstructor 이렇게 4개의 어노테이션을 지원
public class ModelData {
    private String username;
    private int age;
}
