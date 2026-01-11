import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ContentPlannerGUI {

  JFrame frame = new JFrame("Content Planner");
  JPanel panel = new JPanel();
  JPanel editPanel = new JPanel();

  JButton reviewCurrentPlanButton = new JButton("Review Current Plan");
  JButton createNewPostButton = new JButton("Create New Post");
  JButton editButton = new JButton("Edit Existing Post");
  JButton deleteButton = new JButton("Delete Existing Post");


  JLabel title = new JLabel("Welcome to Your Social Media Content Planner!");


  public ContentPlannerGUI() {
    setupMainFrame();
    setupMainMenuButtons();
  }

  public static void main(String[] args) {
    new ContentPlannerGUI();
  }


  private void setupMainFrame() {
    panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    panel.setBackground(new Color (248, 200, 220));
    editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.LINE_AXIS ));
    frame.setSize(1000, 1000);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setBackground(Color.GRAY);


    title.setFont(new Font("Times New Roman", Font.BOLD, 30));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(title);
    panel.add(Box.createRigidArea(new Dimension(0,200)));

    frame.add(panel);
    frame.setVisible(true);
  }

  private void setupMainMenuButtons() {

    Dimension buttonSize = new Dimension(250, 50);

    JButton [] buttons = {
        reviewCurrentPlanButton,
        createNewPostButton,
        editButton,
        deleteButton
    };

    for (JButton button : buttons) {
      button.setAlignmentX(Component.CENTER_ALIGNMENT);
      button.setPreferredSize(buttonSize);
      button.setFont(new Font("Arial", Font.PLAIN, 15));

      panel.add(button);
      panel.add(Box.createRigidArea(new Dimension(0,20)));
    }



    reviewCurrentPlanButton.addActionListener(e -> showCurrentPlan());
    createNewPostButton.addActionListener(e -> createNewPostDialog());
    editButton.addActionListener(e -> editPostDialog());
    deleteButton.addActionListener(e -> deletePostDialog());
  }

  private void showCurrentPlan() {
    PostStorage.loadPosts();
    if (ContentPlanner.posts.isEmpty()) {
      JOptionPane.showMessageDialog(frame, "You don't have any posts yet!");
      return;
    }
    StringBuilder displayText = new StringBuilder();
    for (Post post : ContentPlanner.posts) {
      displayText.append(post.toString()).append("\n");
    }
    JOptionPane.showMessageDialog(frame, displayText.toString());
  }

  private void createNewPostDialog() {
    String platform = JOptionPane.showInputDialog(frame, "Enter Platform: ");
    String date = JOptionPane.showInputDialog(frame, "Enter Date: ");
    String concept = JOptionPane.showInputDialog(frame, "Enter Post Concept: ");
    String categoryChoice = JOptionPane.showInputDialog(frame, "Category (1. Lifestyle, " +
        "2. Food, 3. Get Ready With Me, 4. Other");
    String statusChoice = JOptionPane.showInputDialog(frame, "Enter Status: (1. Idea, " +
        "2. Filming, 3. Editing, 4. Completed ");


    Category category = convertCategoryFromChoice(categoryChoice);
    Status status = convertStatusFromChoice(statusChoice);

    Post post = new Post(ContentPlanner.nextId++, platform, date, category, status, concept);
    ContentPlanner.posts.add(post);
    PostStorage.savePosts(ContentPlanner.posts);
    JOptionPane.showMessageDialog(frame, "Post created!");
  }

  private void editPostDialog() {
    if (ContentPlanner.posts.isEmpty()) {
      JOptionPane.showMessageDialog(frame, "You don't have any posts yet!");
      return;
    }

    // Create a new dialog for editing
    JDialog editDialog = new JDialog(frame, "Edit Post", true);
    JPanel editPanel = new JPanel();
    editPanel.setLayout(new GridLayout(6, 2, 5, 5));

    // Dropdown to select post
    JComboBox<Post> postDropdown = new JComboBox<>();
    for (Post post : ContentPlanner.posts) {
      postDropdown.addItem(post);
    }
    editPanel.add(new JLabel("Select Post: "));
    editPanel.add(postDropdown);

    // Text fields for editing
    JTextField platformField = new JTextField();
    JTextField dateField = new JTextField();
    JTextField conceptField = new JTextField();

    editPanel.add(new JLabel("Platform: "));
    editPanel.add(platformField);

    editPanel.add(new JLabel("Date: "));
    editPanel.add(dateField);

    editPanel.add(new JLabel("Concept: "));
    editPanel.add(conceptField);

    //Dropdowns for category and status
    JComboBox<Category> categoryDropdown = new JComboBox<>(Category.values());
    editPanel.add(new JLabel("Category: "));
    editPanel.add(categoryDropdown);

    JComboBox<Status> statusDropdown = new JComboBox<>(Status.values());
    editPanel.add(new JLabel("Status: "));
    editPanel.add(statusDropdown);

    // Save button
    JButton saveButton = new JButton("Save Changes");
    editPanel.add(saveButton);

    // Add panel to dialog
    editDialog.add(editPanel);
    editDialog.pack();
    editDialog.setLocationRelativeTo(frame);

    // Populate fields when a post is selected
    postDropdown.addActionListener(ev -> {
      Post selectedPost = (Post) postDropdown.getSelectedItem();
      if (selectedPost != null) {
        platformField.setText(selectedPost.getPlatform());
        dateField.setText(selectedPost.getDate());
        conceptField.setText(selectedPost.getConcept());
        categoryDropdown.setSelectedItem(selectedPost.getCategory());
        statusDropdown.setSelectedItem(selectedPost.getStatus());
      }
    });

    // Save changes
    saveButton.addActionListener(ev -> {
      Post selectedPost = (Post) postDropdown.getSelectedItem();
      if (selectedPost != null) {
        selectedPost.setPlatform(platformField.getText());
        selectedPost.setDate(dateField.getText());
        selectedPost.setConcept(conceptField.getText());
        selectedPost.setStatus((Status) statusDropdown.getSelectedItem());
        selectedPost.setCategory((Category) categoryDropdown.getSelectedItem());

        // Save changes to file
        PostStorage.savePosts(ContentPlanner.posts);
        JOptionPane.showMessageDialog(frame, "Post updated!");
        editDialog.dispose();
      }
    });
    editDialog.setVisible(true);
  }

  private void deletePostDialog() {

  }

  private Category convertCategoryFromChoice(String categoryChoice) {
    switch (categoryChoice) {
      case "1":
        return Category.LIFESTYLE;
      case "2":
        return Category.FOOD;
      case "3":
        return Category.GET_READY_WITH_ME;
      case "4":
        return Category.OTHER;
      default:
        return Category.OTHER;
    }
  }

  private Status convertStatusFromChoice(String statusChoice) {
    switch (statusChoice) {
      case "1":
        return Status.IDEA;
      case "2":
        return Status.FILMING;
      case "3":
        return Status.EDITING;
      case "4":
        return Status.COMPLETED;
      default:
        return Status.IDEA;
    }
  }
}




