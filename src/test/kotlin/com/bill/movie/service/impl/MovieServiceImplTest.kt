package com.bill.movie.service.impl

import com.bill.movies.dto.MovieDto
import com.bill.movies.exception.MovieServiceException
import com.bill.movies.model.Movie
import com.bill.movies.model.Star
import com.bill.movies.repository.MovieRepository
import com.bill.movies.repository.StarRepository
import com.bill.movies.service.impl.MovieServiceImpl
import com.bill.movies.util.ErrorCode
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@ExtendWith(MockitoExtension::class)
class MovieServiceImplTest {

    @Mock
    lateinit var movieRepository: MovieRepository

    @InjectMocks
    lateinit var movieService: MovieServiceImpl

    @Mock
    lateinit var starRepository: StarRepository

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @BeforeEach
    fun setUp() {
    }

    @Test
    fun `createMovie should save movie and return DTO representation`() {

        val movieDto = MovieDto(0, "Inception", "2010-06-13", listOf("Leonardo DiCaprio"))
        val movie = Movie(
            0, "Inception", LocalDate.parse("2010-06-13", formatter), mutableSetOf(
                Star(id = 1, name = "Leonardo DiCaprio")
            )
        )
        val savedMovie = movie.copy(id = 1)
        whenever(
            movieRepository.findByTitleAndReleaseDate(
                movieDto.title,
                LocalDate.parse(movieDto.releaseDate, formatter)
            )
        ).thenReturn(null)
        whenever(starRepository.save(Star(name = "Leonardo DiCaprio"))).thenReturn(
            Star(
                id = 1,
                name = "Leonardo DiCaprio"
            )
        )
        whenever(movieRepository.save(movie)).thenReturn(savedMovie)
        val result = movieService.createMovie(movieDto)
        assertEquals(savedMovie.toMovieDto(), result)
        Mockito.verify(movieRepository, Mockito.times(1)).findByTitleAndReleaseDate(
            movieDto.title,
            LocalDate.parse(movieDto.releaseDate, formatter)
        )
        Mockito.verify(movieRepository, Mockito.times(1)).save(movie)
    }

    @Test
    fun `createMovie should throw exception for duplicate movie`() {

        val movieDto = MovieDto(0, "Inception", "2010-06-13", listOf("Leonardo DiCaprio"))
        val existingMovie = Movie(1, "Inception", LocalDate.parse("2010-06-13", formatter))
        whenever(
            movieRepository.findByTitleAndReleaseDate(
                movieDto.title,
                LocalDate.parse(movieDto.releaseDate, formatter)
            )
        ).thenReturn(existingMovie)
        assertThrows(MovieServiceException::class.java) {
            movieService.createMovie(movieDto)
        }
        Mockito.verify(movieRepository, Mockito.times(1)).findByTitleAndReleaseDate(
            movieDto.title,
            LocalDate.parse(movieDto.releaseDate, formatter)
        )
        Mockito.verify(movieRepository, Mockito.never()).save(Mockito.any(Movie::class.java))
    }

    @Test
    fun `createMovie should throw exception when title is empty`() {
        val movieDto = MovieDto(
            id = 1,
            title = "",
            releaseDate = "2010-06-13",
            stars = listOf("Star1", "Star2")
        )
        val exception = assertThrows<MovieServiceException> {
            movieService.createMovie(movieDto)
        }
        assertEquals("Title is required, it cannot be empty", exception.message)
    }

    @Test
    fun `createMovie should throw exception when release date is empty`() {
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            releaseDate = "",
            stars = listOf("Star1", "Star2")
        )
        val exception = assertThrows<MovieServiceException> {
            movieService.createMovie(movieDto)
        }
        assertEquals("Release date is required", exception.message)
    }

    @Test
    fun `createMovie should throw exception when star list is empty`() {
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            releaseDate = "2010-06-13",
            stars = emptyList()
        )
        val exception = assertThrows<MovieServiceException> {
            movieService.createMovie(movieDto)
        }
        assertEquals("At least one star is required", exception.message)
    }

    @Test
    fun `createMovie should throw exception when movie with same title and release date already exists`() {

        val existingMovie = Movie(
            id = 1,
            title = "Test Movie",
            releaseDate = LocalDate.now(),
            stars = mutableSetOf()
        )
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            releaseDate = "2010-06-13",
            stars = listOf("Star1", "Star2")
        )
        whenever(
            movieRepository.findByTitleAndReleaseDate(
                movieDto.title,
                LocalDate.parse(movieDto.releaseDate, formatter)
            )
        )
            .thenReturn(existingMovie)
        val exception = assertThrows<MovieServiceException> {
            movieService.createMovie(movieDto)
        }
        assertEquals(
            "Movie with title:'Test Movie' and release_date:'2010-06-13' already exists",
            exception.message
        )
    }

    @Test
    fun `getMovie should return MovieDto when movie with given id exists`() {
        val movie = Movie(
            id = 1,
            title = "The Shawshank Redemption",
            releaseDate = LocalDate.parse("1994-10-14", formatter),
            stars = mutableSetOf(
                Star(id = 1, name = "Tim Robbins"),
                Star(id = 2, name = "Morgan Freeman")
            )
        )
        whenever(movieRepository.findById(1)).thenReturn(Optional.of(movie))
        val result = movieService.getMovie(1)
        assertEquals(1, result.id)
        assertEquals("The Shawshank Redemption", result.title)
        assertEquals("1994-10-14", result.releaseDate)
        assertEquals(listOf("Tim Robbins", "Morgan Freeman"), result.stars)
    }

    @Test
    fun `getMovie should throw MovieServiceException when movie with given id does not exist`() {
        whenever(movieRepository.findById(1)).thenReturn(Optional.empty())
        val exception = assertThrows(MovieServiceException::class.java) {
            movieService.getMovie(1)
        }
        assertEquals(ErrorCode.MOVIE_NOT_FOUND, exception.errorCode)
    }

    @Test
    fun `updateMovie should update movie and return DTO`() {
        val id = 1L
        val movieDto = MovieDto(
            id = id,
            title = "The Shawshank Redemption",
            releaseDate = "1994-10-14",
            stars = listOf("Tim Robbins", "Morgan Freeman")
        )
        val movie = Movie(
            id = id,
            title = "Old Title",
            releaseDate = LocalDate.parse("1994-10-14", formatter),
            stars = mutableSetOf(
                Star(id = 1, name = "Tim Robbins"),
                Star(id = 2, name = "Morgan Freeman")
            )
        )
        val updatedMovie = movie.copy(
            title = movieDto.title,
            releaseDate = LocalDate.parse(movieDto.releaseDate, formatter),
            stars = mutableSetOf(
                Star(id = 1, name = "Tim Robbins"),
                Star(id = 2, name = "Morgan Freeman")
            )
        )
        val savedMovie = updatedMovie.copy(id = id)
        val savedMovieDto = savedMovie.toMovieDto()
        val star1 = Star(id = 1, name = "Tim Robbins")
        val star2 = Star(id = 2, name = "Morgan Freeman")

        whenever(movieRepository.findById(1)).thenReturn(Optional.of(savedMovie))
        whenever(
            movieRepository.findByTitleAndReleaseDate(
                movieDto.title,
                LocalDate.parse(movieDto.releaseDate, formatter)
            )
        ).thenReturn(movie)
        whenever(starRepository.findByName("Tim Robbins")).thenReturn(star1)
        whenever(starRepository.findByName("Morgan Freeman")).thenReturn(star2)
        whenever(movieRepository.save(updatedMovie)).thenReturn(savedMovie)

        val result = movieService.updateMovie(id, movieDto)

        assertEquals(savedMovieDto, result)
        Mockito.verify(movieRepository, Mockito.times(1)).findById(id)
        Mockito.verify(movieRepository, Mockito.times(1)).save(any<Movie>())
        Mockito.verify(movieRepository, Mockito.times(1)).findByTitleAndReleaseDate(
            movieDto.title,
            LocalDate.parse(movieDto.releaseDate, formatter)
        )
        Mockito.verify(starRepository, Mockito.times(1)).findByName("Tim Robbins")
        Mockito.verify(starRepository, Mockito.times(1)).findByName("Morgan Freeman")
        Mockito.verify(starRepository, Mockito.times(0)).findByName("Some other star name")
    }

    @Test
    fun `updateMovie should throw MOVIE_NOT_FOUND error when movie with given id does not exist`() {
        val movieDto = MovieDto(
            title = "The Godfather",
            releaseDate = "1972-03-24",
            stars = listOf("Marlon Brando", "Al Pacino")
        )

        whenever(movieRepository.findById(1L)).thenReturn(Optional.empty())

        assertThrows<MovieServiceException> {
            movieService.updateMovie(1L, movieDto)
        }.also {
            assertEquals(ErrorCode.MOVIE_NOT_FOUND, it.errorCode)
            assertEquals("Movie with id 1 not found", it.message)
        }
    }

    @Test
    fun `updateMovie should throw DUPLICATE_MOVIE error when a movie with the same title and release date already exists`() {
        val existingMovie = Movie(
            id = 2L,
            title = "The Godfather",
            releaseDate = LocalDate.parse("1972-03-24", formatter),
            stars = mutableSetOf(
                Star(name = "Marlon Brando"),
                Star(name = "Al Pacino")
            )
        )

        val movieDto = MovieDto(
            title = "The Godfather",
            releaseDate = "1972-03-24",
            stars = listOf("Marlon Brando", "Al Pacino")
        )

        whenever(
            movieRepository.findByTitleAndReleaseDate(
                movieDto.title,
                LocalDate.parse(movieDto.releaseDate, formatter)
            )
        ).thenReturn(existingMovie)

        assertThrows<MovieServiceException> {
            movieService.updateMovie(1L, movieDto)
        }.also {
            assertEquals(ErrorCode.DUPLICATE_MOVIE, it.errorCode)
            assertEquals(
                "Movie with title:'The Godfather' and release_date:'1972-03-24' already exists",
                it.message
            )
        }
    }

    @Test
    fun `updateMovie should throw DUPLICATE_STAR error when movieDto contains duplicate stars`() {
        val movieDto = MovieDto(
            title = "The Godfather",
            releaseDate = "1972-03-24",
            stars = listOf("Marlon Brando", "Al Pacino", "Marlon Brando")
        )
        assertThrows<MovieServiceException> {
            movieService.updateMovie(1L, movieDto)
        }.also {
            assertEquals(ErrorCode.DUPLICATE_STAR, it.errorCode)
            assertEquals("Star list contains duplicate stars", it.message)
        }
    }

    @Test
    fun `deleteMovie should throw MOVIE_NOT_FOUND error when movie with given id does not exist`() {
        val id = 1L
        whenever(movieRepository.existsById(id)).thenReturn(false)
        val exception = assertThrows<MovieServiceException> { movieService.deleteMovie(id) }
        assertEquals(ErrorCode.MOVIE_NOT_FOUND, exception.errorCode)
        assertEquals("Movie with id 1 not found", exception.message)
    }

    @Test
    fun `deleteMovie should delete movie with given id when it exists`() {
        // Arrange
        val id = 1L
        whenever(movieRepository.existsById(id)).thenReturn(true)
        movieService.deleteMovie(id)
        Mockito.verify(movieRepository, Mockito.times(1)).deleteById(id)
    }
}
