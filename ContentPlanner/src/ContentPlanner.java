import java.util.Scanner;
import java.util.ArrayList;

public class ContentPlanner {

  static ArrayList<Post> posts = new ArrayList<Post>(); // creates an array object that stores values of type Post
  static Scanner scanner = new Scanner(System.in); // this creates a scanner object
  static int nextId = 1;

  public static void main(String[] args) {
    System.out.println("Enter name: ");
    String name = scanner.nextLine(); // this reads user input
    System.out.println("Welcome to your social media content planner " + name + "!");

    String selectedMenuOption = "";
    posts = PostStorage.loadPosts();

    while (!selectedMenuOption.equals("5")) {
      selectedMenuOption = menu();
      switch (selectedMenuOption) {
        case "1":
          if (posts.isEmpty()) {
            System.out.println("You don't have any posts yet!");
          } else {
            for (Post post : posts) {
              System.out.println("PostID: " + post.getId() + " " + post.toString());
            }
          }
          break;
        case "2":
          createNewPost();
          break;
        case "3":
          editPost();
          break;
        case "4":
          deletePost();
          break;
        case "5":
          break;
        default:
          System.out.println("Invalid selection.");
      }
    }
      System.out.println("Glad I could be of help! :)");
    }

  private static String menu() {
    System.out.println("Menu: ");
    System.out.println("1. Review Current Plan");
    System.out.println("2. Create New Post");
    System.out.println("3. Edit Existing Post");
    System.out.println("4. Delete Existing Post");
    System.out.println("5. Exit");
    System.out.println("Please chose a menu option :)");
    String menuOption = scanner.nextLine();
    return menuOption;
  }

  private static void createNewPost() {
    System.out.println("What platform is this post meant for?");
    String platform = scanner.nextLine();
    System.out.println("What date should this be scheduled for?");
    String date = scanner.nextLine();
    System.out.println("What is the post concept?");
    String concept = scanner.nextLine();
    System.out.println("What category does this post fall under? Please enter a number :)");
    System.out.println("1: Lifestyle");
    System.out.println("2: Food");
    System.out.println("3: Get Ready With Me");
    System.out.println("4: Other");
    int category_choice = scanner.nextInt();
    Category category;
    switch (category_choice){
      case 1:
        category = Category.LIFESTYLE;
        break;
      case 2:
        category = Category.FOOD;
        break;
      case 3:
        category = Category.GET_READY_WITH_ME;
        break;
      case 4:
        category = Category.OTHER;
      default:
        System.out.println("Invalid category choice.");
        category = Category.OTHER;
    }
    System.out.println("What is status of the post? Please enter a number :)");
    System.out.println("1: Idea");
    System.out.println("2: Filming");
    System.out.println("3: Editing");
    System.out.println("4: Completed");
    int status_choice = scanner.nextInt();
    Status status;
    switch (status_choice){
      case 1:
        status = Status.IDEA;
        break;
      case 2:
        status = Status.FILMING;
        break;
      case 3:
        status = Status.EDITING;
        break;
      case 4:
          status = Status.COMPLETED;
          break;
       default:
          System.out.println("Invalid status choice.");
          status = Status.IDEA;
    }

    if (platform != null && date != null && concept != null) {
      Post post = new Post(nextId++, platform, date, category, status, concept);
      posts.add(post);
      PostStorage.savePosts(posts);
      System.out.println("Post idea created!");
    } else {
      if (platform == null) {
        System.out.println("Please enter a valid platform");
      }
      if (date == null) {
        System.out.println("Please enter a valid date");
      }
      if (concept == null) {
        System.out.println("Please enter a valid concept");
      }
    }
  }

  public static void editPost() {
    System.out.println("Which post are you looking to edit? Please enter the post ID: ");
    int postId = scanner.nextInt();
    scanner.nextLine();
    Post postInQuestion = findPostByID(postId);
    if (postInQuestion == null) {
      System.out.println("Post does not exist.");
      return;
    }

    System.out.println(postInQuestion.toString());
    System.out.println("What do you want to edit?");
    System.out.println("1: Platform");
    System.out.println("2: Date");
    System.out.println("3: Concept");
    System.out.println("4: Status");
    System.out.println("5: Idea");
    String editPost = scanner.nextLine();

    if (editPost.contains("1")) {
      System.out.println("Please enter new desired platform: ");
      String platform = scanner.nextLine();
      postInQuestion.setPlatform(platform);
    } else if (editPost.contains("2")) {
      System.out.println("Please enter new desired date: ");
      String date = scanner.nextLine();
      postInQuestion.setDate(date);
    } else if (editPost.contains("3")) {
      System.out.println("Please enter new desired concept: ");
      String concept = scanner.nextLine();
      postInQuestion.setConcept(concept);
    } else if (editPost.contains("4")) {
      System.out.println("Please enter new desired status: ");
      System.out.println("1: Idea");
      System.out.println("2: Filming");
      System.out.println("3: Editing");
      System.out.println("4: Completed");
      String status_choice = scanner.nextLine();
      Status status;
      switch (status_choice){
        case "1":
          status = Status.IDEA;
          break;
        case "2":
          status = Status.FILMING;
          break;
        case "3":
          status = Status.EDITING;
          break;
        case "4":
          status = Status.COMPLETED;
          break;
        default:
          System.out.println("Invalid status choice.");
          status = Status.IDEA;
      } postInQuestion.setStatus(status);

    } else if (editPost.contains("5")) {
      System.out.println("Please enter new desired category: ");
      System.out.println("1: Lifestyle");
      System.out.println("2: Food");
      System.out.println("3: Get Ready With Me");
      System.out.println("4: Other");
      String category_choice = scanner.nextLine();
      Category category;
      switch (category_choice){
        case "1":
          category = Category.LIFESTYLE;
          break;
        case "2":
          category = Category.FOOD;
          break;
        case "3":
          category = Category.GET_READY_WITH_ME;
          break;
        case "4":
          category = Category.OTHER;
        default:
          System.out.println("Invalid category choice.");
          category = Category.OTHER;
      } postInQuestion.setCategory(category);
    }
    PostStorage.savePosts(posts);
  }

  public static void deletePost() {
    System.out.println("Which post are you looking to delete?");
    int postId = scanner.nextInt();
    scanner.nextLine();

    Post postInQuestion = findPostByID(postId);
    if (postInQuestion == null) {
      System.out.println("Post does not exist.");
      return;
    }
    posts.remove(postId);
    System.out.println(postInQuestion);
    PostStorage.savePosts(posts);
    System.out.println("Post idea deleted!");
  }

  private static Post findPostByID(int ID) {
    for (Post post : posts) {
      if (post.getId() == ID) {
        return post;
      }
    }
    return null;
  }
}