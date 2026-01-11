import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class PostStorage {

  private static final File postFile = new File("postFile.txt");

  public static ArrayList<Post> loadPosts() {
    ArrayList<Post> posts = new ArrayList<>();
    int maxId = 0;

    try (Scanner reader = new Scanner(postFile)) {
      while (reader.hasNextLine()) {
        String line = reader.nextLine();
        String[] split = line.split("\\|");
        int id = Integer.parseInt(split[0]);
        String platform = split[1];
        String date = split[2];
        Category category = Category.valueOf(split[3]);
        Status status = Status.valueOf(split[4]);
        String concept = split[5];

        Post post = new Post(id, platform, date, category, status, concept);
        posts.add(post);
        if (post.getId() > maxId) {
          maxId = post.getId();
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    }
    ContentPlanner.nextId = maxId + 1;
    return posts;
  }


  public static void savePosts(ArrayList<Post> posts) {
    try (PrintWriter writer = new PrintWriter(postFile)) {
      for (Post post : posts) {
       String textLine = post.getId() + "|" + post.getPlatform() + "|" + post.getDate() + "|" +
           post.getCategory() + "|" + post.getStatus() + "|" + post.getConcept();
       writer.println(textLine);
      }
    } catch (FileNotFoundException e) {
        System.out.println("Error while saving post");
        e.printStackTrace();
      }
    }


  }
