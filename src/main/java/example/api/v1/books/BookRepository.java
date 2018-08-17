package example.api.v1.books;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

  List<Book> findByAuthor(String author);

  List<Book> findByTitle(String title);

}
