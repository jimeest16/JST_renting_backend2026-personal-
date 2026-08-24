package cr.ac.ucr.paraiso.dsw4.renting.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {
   @RequestMapping(value="/", method=RequestMethod.GET)
   public String welcome(){
       return "welcome"; // buscar el recurso web, es el nombre del html , desde templates 
   // ciclo de crear un url
   // local host, puerto 888, renting y / coln ese / ya funciona 
    }
}