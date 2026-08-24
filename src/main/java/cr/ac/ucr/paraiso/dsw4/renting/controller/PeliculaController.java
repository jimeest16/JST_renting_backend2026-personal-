package cr.ac.ucr.paraiso.dsw4.renting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cr.ac.ucr.paraiso.dsw4.renting.bussiness.PeliculaBussiness;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Pelicula;

@Controller // controlador de manejo MVC
public class PeliculaController {

    @Autowired // inyeccion de dependencias
    private PeliculaBussiness peliculaBussiness;

    @RequestMapping(value = "/findMovies", method = RequestMethod.GET)
    public String iniciar(Model mdel) {
        return "findMovies";
    }

    @RequestMapping(value = "/findMovies", method = RequestMethod.POST)
    public String findMovies(Model model, @RequestParam("titulo") String titulo,
            @RequestParam("genero") String genero) {
        
                
        List<Pelicula> peliculas = peliculaBussiness.findMoviesByTitleOrGenre(titulo, genero);        
        model.addAttribute("peliculas", peliculas);
        return "findMovies";
    }
}
