public class Post {
  private String platform;
  private String date;
  private String concept;
  private Category category;
  private Status status;
  private int id;


  public Post(int id, String platform, String date, Category category, Status status, String concept) {
    this.platform = platform;
    this.date = date;
    this.concept = concept;
    this.category = category;
    this.status = status;
    this.id = id;
  }
  public String getPlatform() {
    return platform;
  }
  public String getDate() {
    return date;
  }
  public String getConcept() {
    return concept;
  }
  public Category getCategory() {
    return category;
  }
  public Status getStatus() {
    return status;
  }
  public int getId() {
    return id;
  }

  public String toString() {
    return  "Post ID: " + id + " Platform: " + platform + " Date: " + date + " Concept: " + concept + " Category: " +
        category + " Status: " + status;
  }

  public String setPlatform(String platform) {
    this.platform = platform;
    return platform;
  }

  public String setDate(String date) {
    this.date = date;
    return date;
  }

  public String setConcept(String concept) {
    this.concept = concept;
    return concept;
  }

  public String setCategory(Category category) {
    this.category = category;
    return category.toString();
  }

  public String setStatus(Status status) {
    this.status = status;
    return status.toString();
  }

  public String setId(int id) {
    this.id = id;
    return String.valueOf(id);
  }
}
