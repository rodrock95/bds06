package com.devsuperior.movieflix.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {
		Optional<Movie> obj = repository.findById(id);
		//optional é uma abordagem para evitar trabalhar com valores nulos
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found")); //obtém objeto dentro do optional
		return new MovieDTO(entity, entity.getGenre());
	}

	@Transactional(readOnly = true)
	public Page<MovieDTO> findAllPaged(Long genreId, Pageable pageable) {
		 Genre genre = (genreId == 0) ? null : genreRepository.getOne(genreId);
	     Page<Movie> page = repository.find(genre, pageable);
	     return page.map(x -> new MovieDTO(x, x.getGenre()));
	}
}

