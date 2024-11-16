package com.mealmap.mealmap_backend_api.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {


    @Value("${application.baseurl}")
    private String baseUrl;

    @GetMapping("/")
    public String rootEndpoint() {


        String swaggerUrl = baseUrl+"/swagger-ui/index.html";

        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>Welcome to MealMap API</title>" +
                "<style>" +
                "  body { font-family: Arial, sans-serif; background-color: #f4f4f9; color: #333; text-align: center; padding: 50px; }" +
                "  h1 { color: #5f6368; }" +
                "  p { font-size: 1.2em; color: #777; }" +
                "  a { color: #007BFF; text-decoration: none; font-weight: bold; }" +
                "  a:hover { text-decoration: underline; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "  <h1>Welcome to the MealMap API!</h1>" +
                "  <p>Please visit the appropriate endpoint for further functionality.</p>" +
                "  <p>Swagger API documentation can be accessed at: </p>" +
                "  <a href=\"" + swaggerUrl + "\" target=\"_blank\">Swagger UI</a>" +
                "</body>" +
                "</html>";
    }
}

