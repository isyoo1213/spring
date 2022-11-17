package spring.springmvc.basic.requestmapping;

import org.springframework.web.bind.annotation.*;

@RestController("/mapping")
public class MappingClassController {

    @GetMapping("/users")
    public String getUsers(){
        return "get users";
    }

    @PostMapping("/users") //url맞추기 위해 복수 허용
    public String addUser(){
        return "post user";
    }

    @GetMapping("/users/{userId}")
    public String findUser(@PathVariable String userId){
        return "get userId  = " + userId;
    }

    @PatchMapping("/users/{userId}")
    public String updateUser(@PathVariable String userId){
        return "update userId  = " + userId;
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable String userId){
        return "delete userId  = " + userId;
    }

}
