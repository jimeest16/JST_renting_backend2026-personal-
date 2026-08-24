package cr.ac.ucr.paraiso.dsw4.renting.domain;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PeliculaTest {

    @Test
   public void whenPeliculaIsCreatedWithAllParametersConstructor_thenGeneroIsNotNull() {
        
        // Arrange -> inicializan las pruebas, preconfiguraciones con datos de prueba
        Genero genero = new Genero(1, "Acción");
        Actor actor1 = new Actor(1, "Tom", "Cruise");
        List<Actor> actores = new ArrayList<>();
        actores.add(actor1);
        

        // Act -> Ejecuta la acción o llama a la función del código que quieres probar.
        // Act -> Aquí llamas al constructor con todos los parámetros
        Pelicula pelicula = new Pelicula();

        // Assert -> Comprueba si el resultado obtenido es igual al resultado esperado
        Assertions.assertNotNull(pelicula.getGenero(), "El género de la película no debería ser nulo");
       
    }
}