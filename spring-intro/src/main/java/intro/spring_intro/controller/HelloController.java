package intro.spring_intro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";
        /*
        컨트롤러에서 리턴 값으로 문자를 반환하면 뷰 리졸버( `viewResolver` )가 화면을 찾아서 처리한다.
        - 스프링 부트 템플릿엔진 기본 viewName 매핑
        - `resources:templates/` +{ViewName}+ `.html`
         */
    }

    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody
    /*
    `@ResponseBody` 를 사용하면 뷰 리졸버( `viewResolver` )를 사용하지 않음
     대신에 HTTP의 BODY에 문자 내용을 직접 반환(HTML BODY TAG를 말하는 것이 아님)
     */
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name;
    }

    @GetMapping("hello-api")
    @ResponseBody
    /*
    `@ResponseBody` 를 사용하고, 객체를 반환하면 객체가 JSON으로 변환됨
     */
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello(name);
        return hello;
    }

    static class Hello {
        private String name;

        public Hello(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
