package br.com.restwithspringbootandjavaerudio.repositories;

import br.com.restwithspringbootandjavaerudio.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
}
