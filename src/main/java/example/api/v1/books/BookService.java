package example.api.v1.books;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import example.api.infra.SampleEntityNotFoundException;

@Service
public class BookService {

  private static final Logger log = LoggerFactory.getLogger(BookService.class);

  @Autowired
  private BookRepository bookRepository;

  public BookService() {}

  public Book createBook(Book book) {
    log.debug("Creating book: " + book.toString());
    return bookRepository.save(book);
  }

  public Book getBook(Long id) {
    log.debug("Getting book: " + id);
    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
      return book.get();
    } else {
      throw new SampleEntityNotFoundException("Book not found: " + id);
    }
  }

  public Book updateBook(Book book) {
    log.debug("Updating book: " + book.toString());

    Optional<Book> existingBook = bookRepository.findById(book.getId());

    if (existingBook.isPresent()) {
      return bookRepository.save(book);
    } else {
      throw new SampleEntityNotFoundException("Book not found: " + book.getId());
    }

  }

  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public void deleteBook(Long id) {
    try {
      bookRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new SampleEntityNotFoundException("Book not found: " + id);
    }
  }
}
