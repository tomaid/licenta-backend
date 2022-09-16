package com.licenta.licenta.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class index {
    @RequestMapping ("/")
    void handleFoo(HttpServletResponse response) throws IOException {
        response.sendRedirect("/web/index.html");
    }

}
