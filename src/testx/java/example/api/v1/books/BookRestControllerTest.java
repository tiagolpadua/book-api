package example.api.v1.books;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import example.Application;
import example.BaseTestRestController;

// https://spring.io/guides/tutorials/bookmarks/ -> Testing a REST Service

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class BookRestControllerTest extends BaseTestRestController {
  private Book book;

  @Autowired
  private BookRepository bookRepository;

  @Before
  public void setup() throws Exception {
    super.setup();

    bookRepository.deleteAllInBatch();

    book = bookRepository.save(new Book(null, "Alice's Adventures in Wonderland",
        "Charles Lutwidge Dodgson",
        "Tells of a girl named Alice falling through a rabbit hole into a fantasy world populated by peculiar, anthropomorphic creatures"));
  }

  @Test
  public void createAValidBook() throws Exception {
    String validBookJson = json(new Book(null, "Journey to the Center of the Earth", "Jules Verne",
        "Journey to the Center of the Earth is an 1864 science fiction novel by Jules Verne."));

    this.mockMvc.perform(post("/api/v1/books").contentType(contentType).content(validBookJson))
        .andExpect(status().isCreated());
  }

  @Test
  public void createAInvalidBook() throws Exception {
    String invalidBookJson = json(new Book(null, "Jou", "Jules Verne",
        "Journey to the Center of the Earth is an 1864 science fiction novel by Jules Verne."));

    this.mockMvc.perform(post("/api/v1/books").contentType(contentType).content(invalidBookJson))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void getAllBooks() throws Exception {
    mockMvc.perform(get("/api/v1/books")).andExpect(status().isOk())
        .andExpect(content().contentType(contentType)).andExpect(jsonPath("$.data", hasSize(1)))
        .andExpect(jsonPath("$.data[0].id", greaterThan(0)))
        .andExpect(jsonPath("$.data[0].title", is(book.getTitle())))
        .andExpect(jsonPath("$.data[0].author", is(book.getAuthor())))
        .andExpect(jsonPath("$.data[0].description", is(book.getDescription())));
  }

  @Test
  public void getAnExistingBook() throws Exception {
    mockMvc.perform(get("/api/v1/books/" + book.getId())).andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.data.id", greaterThan(0)))
        .andExpect(jsonPath("$.data.title", is(book.getTitle())))
        .andExpect(jsonPath("$.data.author", is(book.getAuthor())))
        .andExpect(jsonPath("$.data.description", is(book.getDescription())));
  }

  @Test
  public void getAnInexistingBook() throws Exception {
    mockMvc.perform(get("/api/v1/books/" + (book.getId() + 1))).andExpect(status().isNotFound());
  }

  @Test
  public void updateValidBook() throws Exception {
    String validBookJson =
        json(new Book(book.getId(), "Journey to the Center of the Earth", "Jules Verne",
            "Journey to the Center of the Earth is an 1864 science fiction novel by Jules Verne."));

    this.mockMvc
        .perform(put("/api/v1/books/" + book.getId()).contentType(contentType).content(validBookJson))
        .andExpect(status().isOk());
    // .andReturn();
    // System.out.println("-------------------");
    // System.out.println(resp.getResponse().getContentAsString());
    // System.out.println("-------------------");

  }

  @Test
  public void updateValidBookAndInvalidID() throws Exception {
    String validBookJson =
        json(new Book(book.getId() + 1, "Journey to the Center of the Earth", "Jules Verne",
            "Journey to the Center of the Earth is an 1864 science fiction novel by Jules Verne."));

    this.mockMvc
        .perform(
            put("/api/v1/books/" + book.getId() + 1).contentType(contentType).content(validBookJson))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void updateValidBookAndNonMatchingID() throws Exception {
    String validBookJson =
        json(new Book(book.getId(), "Journey to the Center of the Earth", "Jules Verne",
            "Journey to the Center of the Earth is an 1864 science fiction novel by Jules Verne."));

    this.mockMvc
        .perform(
            put("/api/v1/books/" + (book.getId() + 1)).contentType(contentType).content(validBookJson))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void updateInvalidBook() throws Exception {
    String validBookJson = json(new Book(book.getId(), "Jou", "Jules Verne",
        "Journey to the Center of the Earth is an 1864 science fiction novel by Jules Verne."));

    this.mockMvc
        .perform(put("/api/v1/books/" + book.getId()).contentType(contentType).content(validBookJson))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void deleteExistingBook() throws Exception {
    this.mockMvc.perform(delete("/api/v1/books/" + book.getId())).andExpect(status().isOk());
  }

  @Test
  public void deleteNonExistingBook() throws Exception {
    this.mockMvc.perform(delete("/api/v1/books/" + (book.getId() + 1)))
        .andExpect(status().isNotFound());
  }

}
