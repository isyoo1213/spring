package spring.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //들어온 Http Request message의 Start Line 정보 가져오기
        //출력문구 드래그 한 후 Ctrl + Alt + Shift + T -> Method Extracting by Refactoring
        printStartLine(request);

        //Header 정보 가져오기
        printHeaders(request);

        //편리한 Header 정보 가져오기
        printHeaderUtils(request);

        //기타 정보 가져오기
        printEtc(request);
    }

    //들어온 Http Request message의 Start Line 정보 가져오기
    private void printStartLine(HttpServletRequest request) {
        System.out.println("--- REQUEST-LINE - start ---");
        System.out.println("request.getMethod() = " + request.getMethod()); //GET
        System.out.println("request.getProtocol() = " + request.getProtocol()); //HTTP/1.1
        System.out.println("request.getScheme() = " + request.getScheme()); //http
        // http://localhost:8080/request-header
        System.out.println("request.getRequestURL() = " + request.getRequestURL());
        // /request-header
        System.out.println("request.getRequestURI() = " + request.getRequestURI());
        //username=hi
        System.out.println("request.getQueryString() = " +
                request.getQueryString());
        System.out.println("request.isSecure() = " + request.isSecure()); //https 사용 유무
        System.out.println("--- REQUEST-LINE - end ---");
        System.out.println();
    }

    //Header 정보 가져오기
    private void printHeaders(HttpServletRequest request) {
        System.out.println("--- Headers - start ---");
 //방법 1. 예전 방법
         Enumeration<String> headerNames = request.getHeaderNames();
         while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
         }

/* //방법 2. 최근 방법 by Iterator
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName -> System.out.println(headerName + ":" + request.getHeader(headerName)));
*/
        System.out.println("--- Headers - end ---");
        System.out.println();
    }

    //Header 편리한 조회
    private void printHeaderUtils(HttpServletRequest request) {
        System.out.println("--- Header 편의 조회 start ---");
        System.out.println("[Host 편의 조회]");
        System.out.println("request.getServerName() = " +
                request.getServerName()); //Host 헤더
        System.out.println("request.getServerPort() = " +
                request.getServerPort()); //Host 헤더
        System.out.println();
        System.out.println("[Accept-Language 편의 조회]");
        request.getLocales().asIterator()
                .forEachRemaining(locale -> System.out.println("locale = " +
                        locale));
        System.out.println("request.getLocale() = " + request.getLocale()); //웹브라우저가 설정한 언어 우선순위가 가장 높은 것을 꺼냄
        System.out.println();
        System.out.println("[cookie 편의 조회]");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }
        System.out.println();
        System.out.println("[Content 편의 조회]"); //일반적으로는 GET 메서드로 보내므로 null 등 세팅이 안잡힘
        System.out.println("request.getContentType() = " +
                request.getContentType());
        System.out.println("request.getContentLength() = " +request.getContentLength());
        System.out.println("request.getCharacterEncoding() = " +
                request.getCharacterEncoding());
        System.out.println("--- Header 편의 조회 end ---");
        System.out.println();
    }

    //기타 정보 - 정확히는 Http Request에서 가져오는 정보가 아닌 연결된 네트워크에서 가져오는 정보(Remote) + 내가 구축한 서버 정보(Local)
    private void printEtc(HttpServletRequest request) {
         System.out.println("--- 기타 조회 start ---");
         System.out.println("[Remote 정보]");
         System.out.println("request.getRemoteHost() = " +
                 request.getRemoteHost()); //
         System.out.println("request.getRemoteAddr() = " +
                 request.getRemoteAddr()); //
         System.out.println("request.getRemotePort() = " +
                 request.getRemotePort()); //
         System.out.println();
         System.out.println("[Local 정보]");
         System.out.println("request.getLocalName() = " +
                 request.getLocalName()); //
         System.out.println("request.getLocalAddr() = " +
                 request.getLocalAddr()); //
         System.out.println("request.getLocalPort() = " +
                 request.getLocalPort()); //
         System.out.println("--- 기타 조회 end ---");
         System.out.println();
    }
}
