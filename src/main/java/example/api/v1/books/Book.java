package example.api.v1.books;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotNull
  @Size(min = 5, message = "Title should have at least 5 characters")
  private String title;

  @NotNull
  @Size(min = 2, message = "Author should have atleast 2 characters")
  private String author;

  @NotNull
  @Size(min = 5, message = "Description should have at least 5 characters")
  @Size(max = 100, message = "Description should have at max 100 characters")
  private String description;

  public Book() {}

  public Book(Long id, String title, String author, String description) {
    super();
    this.id = id;
    this.title = title;
    this.author = author;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Book [id=" + id + ", title=" + title + ", author=" + author + ", description="
        + description + "]";
  }

}
