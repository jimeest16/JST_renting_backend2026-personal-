package cr.ac.ucr.paraiso.dsw4.renting.data;

import java.sql.ResultSet; // tablas de datos, filas y columnas
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cr.ac.ucr.paraiso.dsw4.renting.domain.Actor;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Genero;
import cr.ac.ucr.paraiso.dsw4.renting.domain.Pelicula;

@Repository // repository signiifca que esta clase trabaja con acceso a datos, en este caso
            // con la base de datos
public class PeliculaData {

    @Autowired // inyección de dependencias, Spring se encarga de crear e inyectar el objeto
               // JdbcTemplate en esta clase

    private JdbcTemplate jdbcTemplate;
    // es una herramienta de Spring para trabajar con la base de datos mediante
    // JDBC,
    // que es la forma en que Java se comunica con bases de datos relacionales.
    // El JdbcTemplate simplifica el proceso de ejecutar consultas SQL y manejar
    // resultados.

    public List<Pelicula> findMoviesByTitleOrGenre(String title, String genre) {

        String sqlSelect = """
                SELECT
                    p.pelicula_id,
                    p.titulo,
                    p.genero_id,
                    g.nombre_genero,
                    p.subtitulada,
                    p.estreno,
                    pa.actor_id,
                    a.nombre_actor,
                    a.apellidos_actor
                FROM Pelicula p
                INNER JOIN Genero g
                    ON p.genero_id = g.genero_id
                LEFT JOIN PeliculaActor pa
                    ON p.pelicula_id = pa.pelicula_id
                LEFT JOIN Actor a
                    ON pa.actor_id = a.actor_id
                WHERE (
                    LOWER(p.titulo) LIKE ?
                    OR LOWER(g.nombre_genero) LIKE ?
                )
                """;

        // LEFT JOIN PeliculaActor y LEFT JOIN Actor: son "left" porque una película
        // puede no tener actores registrados (o aún no asignados) y aun así debe
        // aparecer en el resultado, con las columnas de actor en NULL.
        String titleLike = title == null || title.isBlank()
                ? "%" // si el título es nulo o vacío, se reemplaza por % que en SQL significa "cualquier cosa"
                : "%" + title.toLowerCase().trim() + "%";

        String genreLike = genre == null || genre.isBlank()
                ? "%"
                : "%" + genre.toLowerCase().trim() + "%";

        // si titulo o genero viene en nulo, se reemplaza por % que en SQL significa
        // "cualquier cosa", para que no filtre por ese campo.
        // si viene con textp se convierte a minúscula y se le agregan % al inicio y al
        // final para que busque coincidencias parciales.

        // sustitye los signos de interrogación en la consulta SQL con los valores de
        // titleLike y genreLike, y luego ejecuta la consulta.
        return jdbcTemplate.query(
                sqlSelect,
                new PeliculaExtractor(),
                titleLike,
                genreLike);
    }
}

class PeliculaExtractor implements ResultSetExtractor<List<Pelicula>> {

    @Override
    public List<Pelicula> extractData(ResultSet rs)
            throws SQLException, DataAccessException {

        Map<Integer, Pelicula> map = new HashMap<>();
        Pelicula pelicula = null; // se crea un objeto Pelicula para almacenar temporalmente los datos de cada película mientras se recorre el ResultSet.
        while (rs.next()) { // recorre cada fila del ResultSet, que contiene los resultados de la consulta SQL.

            // Obtener el ID de la película
            int peliculaId = rs.getInt("pelicula_id"); 

            // Buscar si la película ya existe en el Map
            pelicula = map.get(peliculaId);

            // Si no existe, crearla
            if (pelicula == null) {

                pelicula = new Pelicula(); // se crea un nuevo objeto Pelicula para almacenar los datos de la película actual.

                pelicula.setPeliculaId(peliculaId); // donde inicia a llenar los datos de la película con los valores obtenidos del ResultSet.

                pelicula.setTitulo(
                        rs.getString("titulo"));

                // Crear y asignar género
                Genero genero = new Genero();

                genero.setGeneroId(
                        rs.getInt("genero_id"));

                genero.setNombreGenero(
                        rs.getString("nombre_genero"));

                pelicula.setGenero(genero);

                pelicula.setSubtitulada(
                        rs.getBoolean("subtitulada"));

                pelicula.setEstreno(
                        rs.getBoolean("estreno"));

                // Guardar la película en el Map
                map.put(peliculaId, pelicula);  // ek objeto pelicula al salir del while viene con la información de la película y sus actores, y se guarda en el Map con su ID como clave.
            }

            // Obtener actor
            int actorId = rs.getInt("actor_id");

            // Verificar que el actor no sea NULL
            if (!rs.wasNull()) { // si la fila actual tiene un actor asociado (es decir, el actor_id no es NULL), 
            // se crea un objeto Actor y se llena con los datos del ResultSet. Luego, se agrega a la lista de actores de la película actual.

                Actor actor = new Actor();

                actor.setActorId(actorId);

                actor.setNombreActor(
                        rs.getString("nombre_actor"));

                actor.setApellidosActor(
                        rs.getString("apellidos_actor"));

                // Agregar actor a la película
                pelicula.getActores().add(actor);
            }
        }

        // Convertir el Map en una lista de películas
        return new ArrayList<>(map.values()); 
    }
}