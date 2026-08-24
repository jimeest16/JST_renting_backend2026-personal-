package cr.ac.ucr.paraiso.dsw4.renting.data;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import cr.ac.ucr.paraiso.dsw4.renting.domain.Pelicula;

@SpringBootTest
public class PeliculaDataTest {

    @Autowired
    private PeliculaData peliculaData;

    @Autowired
    private DataSource dataSource; // pa|ra probar la conexión con la base de datos

    @Autowired
    private JdbcTemplate jdbcTemplate; // para ejecutar consultas SQL directamente en la base de datos durante las pruebas


    // inserta datos
    @Test
    @DisplayName("Given existing movie with existing title and genre, when searching by title or genre, then return the movie")
    @Transactional

    // cargar los datos de prueba antes de ejecutar el test, y limpiar después
    @Sql(
        scripts = "/insert_peliculas_con_actores.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void givenExistingMovieWithExistingTitleAndGenreThenReturnMovie() {

        // Arrange
        String title = "Story";
        String genre = "Drama";

        // Act
        List<Pelicula> peliculas =
                peliculaData.findMoviesByTitleOrGenre(title, genre);

        // Assert
        assertNotNull(
                peliculas,
                "No hay películas encontradas con el título o género"
        );

        assertTrue(
                !peliculas.isEmpty(),
                "La lista de películas está vacía"
        );

        String expectedTitle = "Story";
        String expectedGenre = "Drama";

        assertTrue(
                peliculas.stream().anyMatch(p ->
                        p.getTitulo().contains(expectedTitle)
                        || p.getGenero().getNombreGenero().contains(expectedGenre)
                ),
                "No se encontró una película con el título o género esperado"
        );
    }

       @Test
@Sql(
        scripts = "/insert_peliculas_con_actores.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void hayDatosEnLaTablaGenero() {
        String sql = "SELECT * FROM Genero";
        List<Map<String,Object>> filas = jdbcTemplate.queryForList(sql);

        for( Map<String,Object> fila: filas){
            System.out.println("Fila: " + fila);
        }
        // si no pasa, 
        assertFalse(filas.isEmpty(), "No hay datos en la tabla Genero");

    }

    @Test
@Sql(
        scripts = "/insert_peliculas_con_actores.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void hayDatosEnLaTablaPelicula() {
        String sql = "SELECT * FROM Pelicula";
        List<Map<String,Object>> filas = jdbcTemplate.queryForList(sql);

        for( Map<String,Object> fila: filas){
            System.out.println("Fila: " + fila);
        }
        // si no pasa, 
        assertFalse(filas.isEmpty(), "No hay datos en la tabla Pelicula");

    }
// conexión con la base de datos
@Test
void testDataBaseConnection() throws Exception {
    try (var connection = dataSource.getConnection()) {
        assertNotNull(
            connection,
            "La conexión con la base de datos no pudo establecerse"
        );

        System.out.println("CONEXIÓN EXITOSA");
        System.out.println("Servidor: " + connection.getMetaData().getURL());
        System.out.println("Base de datos: " + connection.getCatalog());
        System.out.println("Usuario: " + connection.getMetaData().getUserName());
        System.out.println("================================");
    }
}
}
