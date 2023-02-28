package com.bill.movies.service.impl

import com.bill.movies.dto.MovieDto
import com.bill.movies.dto.toMovie
import com.bill.movies.exception.MovieServiceException
import com.bill.movies.model.Star
import com.bill.movies.repository.MovieRepository
import com.bill.movies.repository.StarRepository
import com.bill.movies.service.MovieService
import com.bill.movies.util.ErrorCode
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional


@Service
class MovieServiceImpl(private val movieRepository: MovieRepository, private val starRepository: StarRepository) : MovieService {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @Transactional
    override fun createMovie(movieDto: MovieDto): MovieDto {
        validateMovieDto(movieDto)

        // Check if movie with same title and release date already exists
        if (movieRepository.findByTitleAndReleaseDate(movieDto.title, LocalDate.parse(movieDto.releaseDate, formatter)) != null) {
            throw MovieServiceException(
                ErrorCode.DUPLICATE_MOVIE,
                ErrorCode.DUPLICATE_MOVIE.getMessage(
                    movieDto.title,
                    movieDto.releaseDate.toString()
                )
            )
        }

        // Create movie object from DTO
        val movie = movieDto.toMovie(starRepository)

        // Save movie and return DTO representation
        return movieRepository.save(movie).toMovieDto()
    }

    @Transactional
    override fun getMovie(id: Long): MovieDto {
        val movie = movieRepository.findById(id)
            .orElseThrow {
                throw MovieServiceException(
                    ErrorCode.MOVIE_NOT_FOUND,
                    ErrorCode.MOVIE_NOT_FOUND.getMessage(id)
                )
            }
        return movie.toMovieDto()
    }

    @Transactional
    override fun updateMovie(id: Long, movieDto: MovieDto): MovieDto {
        validateMovieDto(movieDto)

        // Check if movie with same title and release date already exists
        val existingMovie = movieRepository.findByTitleAndReleaseDate(movieDto.title, LocalDate.parse(movieDto.releaseDate, formatter))
        if (existingMovie != null && existingMovie.id != id) {
            throw MovieServiceException(
                ErrorCode.DUPLICATE_MOVIE,
                ErrorCode.DUPLICATE_MOVIE.getMessage(movieDto.title, LocalDate.parse(movieDto.releaseDate, formatter).toString())
            )
        }

        val movie = movieRepository.findById(id)
            .orElseThrow {
                throw MovieServiceException(
                    ErrorCode.MOVIE_NOT_FOUND,
                    ErrorCode.MOVIE_NOT_FOUND.getMessage(id)
                )
            }

        // Update movie fields
        movie.title = movieDto.title
        movie.releaseDate = LocalDate.parse(movieDto.releaseDate, formatter)

        // Save the unique list of stars for the movie
        val stars = movieDto.stars.map { starName ->
            starRepository.findByName(starName) ?: starRepository.save(Star(name = starName))
        }.toMutableSet()

        if (stars.size != movieDto.stars.size) {
            throw MovieServiceException(
                ErrorCode.DUPLICATE_STAR,
                ErrorCode.DUPLICATE_STAR.getMessage()
            )
        }

        // Save movie and return DTO representation
        movie.stars = stars
        return movieRepository.save(movie).toMovieDto()
    }

    @Transactional
    override fun deleteMovie(id: Long) {
        if (!movieRepository.existsById(id)) {
            throw MovieServiceException(
                ErrorCode.MOVIE_NOT_FOUND,
                ErrorCode.MOVIE_NOT_FOUND.getMessage(id)
            )
        }
        movieRepository.deleteById(id)
    }

    private fun validateMovieDto(movieDto: MovieDto) {
        require(movieDto.title.isNotBlank()) {
            throw MovieServiceException(
                ErrorCode.MISSING_TITLE,
                ErrorCode.MISSING_TITLE.getMessage()
            )
        }
        require(movieDto.releaseDate.isNotEmpty()) {
            throw MovieServiceException(
                ErrorCode.MISSING_RELEASE_DATE,
                ErrorCode.MISSING_RELEASE_DATE.getMessage()
            )
        }
        require(movieDto.stars.isNotEmpty()) {
            throw MovieServiceException(
                ErrorCode.MISSING_STAR,
                ErrorCode.MISSING_STAR.getMessage()
            )
        }
        require(HashSet<String>(movieDto.stars).size == movieDto.stars.size) {
            throw MovieServiceException(
                ErrorCode.DUPLICATE_STAR,
                ErrorCode.DUPLICATE_STAR.getMessage()
            )
        }
    }
}