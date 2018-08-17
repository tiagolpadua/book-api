package example.api.v1.books;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import example.api.infra.MessageType;
import example.api.infra.ServiceMessage;
import example.api.infra.ServiceResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/books")
@Api(value = "Books")
public class BookRestController {

  @Autowired
  private BookService bookService;

  @PostMapping
  @ApiOperation(value = "Cria um livro", notes = "Os dados do livro devem ser informados",
      response = Book.class)
  public ResponseEntity<ServiceResponse<Book>> createBook(@RequestBody @Valid Book book) {
    book = bookService.createBook(book);

    HttpHeaders headers = new HttpHeaders();

    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(book.getId()).toUri();
    headers.setLocation(location);

    ServiceMessage message = new ServiceMessage("Book successfuly created!");

    return new ResponseEntity<>(new ServiceResponse<>(book, message), headers, HttpStatus.CREATED);
  }

  // http://localhost:8080/api/v1/books/1
  @ApiOperation(value = "Detalha um livro pelo ID", notes = "Um ID válido deve ser informado",
      response = Book.class)
  @GetMapping("/{id}")
  public ResponseEntity<ServiceResponse<Book>> getBook(@PathVariable Long id) {
    return ResponseEntity.ok(new ServiceResponse<>(bookService.getBook(id)));
  }

  @GetMapping("/validate/{id}")
  public ResponseEntity<ServiceResponse<Boolean>> validate1Book(@PathVariable Long id) {

    ParameterizedTypeReference<ServiceResponse<Book>> typeRef =
        new ParameterizedTypeReference<ServiceResponse<Book>>() {};

    ServiceResponse<Book> srBook;
    RestTemplate restTemplate = new RestTemplate();
    System.out.println("app-2 - Will validate book: " + id);
    try {
      ResponseEntity<ServiceResponse<Book>> responseEntity = restTemplate.exchange(
          "http://localhost:8080/app-1/api/v1/books/" + id, HttpMethod.GET, null, typeRef);
      srBook = responseEntity.getBody();
      System.out.println("-----------------------------------------");
      System.out.println(srBook.getData().toString());
      System.out.println("-----------------------------------------");

    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().value() == 404) {
        return ResponseEntity.ok(new ServiceResponse<>(false));
      } else {
        throw new RuntimeException(e);
      }
    }

    if (srBook.getData().getId() == id) {
      return ResponseEntity.ok(new ServiceResponse<>(true));
    } else {
      throw new RuntimeException("Wrong id in book:" + srBook.getData().toString());
    }

  }

//  @SuppressWarnings("unchecked")
//  @GetMapping("/validate/{id}")
//  public ResponseEntity<ServiceResponse<Boolean>> validateBook(@PathVariable Long id) {
//    ServiceResponse<Book> srBook;
//    RestTemplate restTemplate = new RestTemplate();
//    System.out.println("app-2 - Will validate book: " + id);
//    try {
//      srBook = restTemplate.getForObject("http://localhost:8080/app-1/api/v1/books/" + id,
//          ServiceResponse.class);
//    } catch (HttpClientErrorException e) {
//      if (e.getStatusCode().value() == 404) {
//        return ResponseEntity.ok(new ServiceResponse<>(false));
//      } else {
//        throw new RuntimeException(e);
//      }
//    }
//
//    if (srBook.getData().getId() == id) {
//      return ResponseEntity.ok(new ServiceResponse<>(true));
//    } else {
//      throw new RuntimeException("Wrong id in book:" + srBook.getData().toString());
//    }
//
//  }

  // http://localhost:8080/api/v1/books
  @GetMapping
  @ApiOperation(value = "Lista os livros existentes", response = Book.class)
  public ServiceResponse<List<Book>> listBooks() {
    return new ServiceResponse<>(bookService.getAllBooks());
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Altera os dados do livro informado",
      notes = "Um ID válido deve ser informado", response = Book.class)
  public ResponseEntity<ServiceResponse<Book>> updateBook(@PathVariable Long id,
      @Valid @RequestBody Book book) {
    if (!book.getId().equals(id)) {
      return new ResponseEntity<ServiceResponse<Book>>(
          new ServiceResponse<>(null,
              new ServiceMessage(MessageType.ERROR,
                  "URL ID: '" + id + "'doesn't match book ID: '" + book.getId() + "'.")),
          HttpStatus.BAD_REQUEST);
    }

    ServiceMessage message = new ServiceMessage("Book successfuly updated!");

    return new ResponseEntity<ServiceResponse<Book>>(
        new ServiceResponse<>(bookService.updateBook(book), message), HttpStatus.OK);

  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Apaga um livro pelo ID", notes = "Um ID válido deve ser informado",
      response = Book.class)
  public ResponseEntity<ServiceResponse<Void>> deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    ServiceMessage message = new ServiceMessage("Book sucessfully deleted!");
    return new ResponseEntity<>(new ServiceResponse<>(message), HttpStatus.OK);
  }

}
