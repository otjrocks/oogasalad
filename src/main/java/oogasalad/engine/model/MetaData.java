package oogasalad.engine.model;

/**
 * The {@code MetaData} class represents metadata information for an entity, including a title, an
 * author, and a description.
 *
 * @author Will He.
 */
public class MetaData {

  private String title;
  private String author;
  private String description;

  /**
   * Gets the author of the entity.
   *
   * @return the author's name
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Sets the author of the entity.
   *
   * @param author the author's name
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Gets the title of the entity.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the entity.
   *
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the description of the entity.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the entity.
   *
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
