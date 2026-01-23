import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ContentPlannerGUI {

  DefaultTableModel tableModel;
  JTable postTable;

  CardLayout cardLayout;
  JPanel cardPanel;

  JFrame frame = new JFrame("Content Planner");

  JLabel title = new JLabel("Welcome to Your Social Media Content Planner!");


  public ContentPlannerGUI() {
    ContentPlanner.posts = PostStorage.loadPosts();
    setupMainFrame();

  }

  public static void main(String[] args) {
    new ContentPlannerGUI();
  }


  private void setupMainFrame() {

    if (!ContentPlanner.posts.isEmpty()) {
      ContentPlanner.nextId =
          ContentPlanner.posts.get(ContentPlanner.posts.size() - 1).getId() + 1;
    }

    cardLayout = new CardLayout();
    cardPanel = new JPanel(cardLayout);

    cardPanel.add(menuPanel(), "MENU");
    cardPanel.add(createPostPanel(), "CREATE");
    cardPanel.add(editPanel(), "EDIT");

    frame.setSize(1000, 1000);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(cardPanel);
    frame.setVisible(true);

    cardLayout.show(cardPanel, "MENU");
    refreshTable();
  }


  private JPanel menuPanel() {

    //Main Menu Panel
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    mainPanel.setBackground(new Color(248, 200, 220));


    // Title Panel
    JPanel titlePanel = new JPanel();
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
    titlePanel.setBackground(new Color(248, 200, 220));

    titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    Font titleFont = new Font("Serif", Font.BOLD, 100);

    JLabel content = new JLabel("Content");
    JLabel planner = new JLabel("Planner");

    JLabel[] titles = {content, planner};

    for (JLabel title : titles) {
      title.setFont(titleFont);
      title.setAlignmentX(Component.CENTER_ALIGNMENT);
      titlePanel.add(title);

      titlePanel.add(Box.createRigidArea(new Dimension(0, 40)));
    }

    // Add Title Panel to Main Panel
    mainPanel.add(titlePanel, BorderLayout.NORTH);


    // Buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setBackground(new Color(248, 200, 220));

    JButton reviewButton = new JButton("Review");
    JButton createButton = new JButton("Create");
    JButton editButton = new JButton("Edit");
    JButton deleteButton = new JButton("Delete");

    JButton[] buttons = {reviewButton, createButton, editButton, deleteButton};


    for (JButton button : buttons) {
      buttonPanel.add(button);
      buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
    }

    mainPanel.add(buttonPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));


    //Table
    String[] columns = {"ID", "Platform", "Date", "Concept", "Category", "Status"};
    tableModel = new DefaultTableModel(columns, 0);
    postTable = new JTable(tableModel);

    JScrollPane scrollPane = new JScrollPane(postTable);
    scrollPane.setPreferredSize(new Dimension(900, 200));
    scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

    mainPanel.add(scrollPane);


    // Button Actions
    reviewButton.addActionListener(e -> showCurrentPlan());
    createButton.addActionListener(e -> cardLayout.show(cardPanel, "CREATE"));
    editButton.addActionListener(e -> {
      cardPanel.remove(cardPanel.getComponentCount() - 1);
      cardPanel.add(editPanel(), "EDIT");
      cardLayout.show(cardPanel, "EDIT");
    });
    deleteButton.addActionListener(e -> deletePostDialog());
    refreshTable();
    return mainPanel;
  }


  private JPanel createPostPanel() {
    JPanel createPanel = new JPanel();
    createPanel.setLayout(new GridLayout(7, 2, 10, 10));

    JTextField platformField = new JTextField();
    JTextField dateField = new JTextField();
    JTextField conceptField = new JTextField();

    JComboBox<Category> categoryDropdown = new JComboBox<>(Category.values());
    JComboBox<Status> statusDropdown = new JComboBox<>(Status.values());

    createPanel.add(new JLabel("Platform:"));
    createPanel.add(platformField);

    createPanel.add(new JLabel("Date:"));
    createPanel.add(dateField);

    createPanel.add(new JLabel("Concept:"));
    createPanel.add(conceptField);

    createPanel.add(new JLabel("Category:"));
    createPanel.add(categoryDropdown);

    createPanel.add(new JLabel("Status:"));
    createPanel.add(statusDropdown);

    // Save and Back Buttons
    JButton saveButton = new JButton("Save Post");
    JButton backButton = new JButton("Back");

    createPanel.add(backButton);
    createPanel.add(saveButton);

    // Save Button Logic
    saveButton.addActionListener(e -> {
      Post post = new Post(
          ContentPlanner.nextId++,
          platformField.getText(),
          dateField.getText(),
          (Category) categoryDropdown.getSelectedItem(),
          (Status) statusDropdown.getSelectedItem(),
          conceptField.getText()
      );
      ContentPlanner.posts.add(post);
      PostStorage.savePosts(ContentPlanner.posts);

      refreshTable();
      cardLayout.show(cardPanel, "MENU");
    });

    // Back Button Logic
    backButton.addActionListener(e -> {
      cardLayout.show(cardPanel, "MENU");
    });
    return createPanel;
  }


  private JPanel editPanel() {
    JPanel editPanel = new JPanel();
    editPanel.setLayout(new GridLayout(2, 1, 10, 10));
    editPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Back button
    JButton backButton = new JButton("Back");
    editPanel.add(backButton);

    backButton.addActionListener(e -> {
      cardLayout.show(cardPanel, "MENU");
    });

    if (ContentPlanner.posts.isEmpty()) {
      JLabel titleLabel = new JLabel("No posts found", JLabel.CENTER);

      editPanel.add(titleLabel);
      editPanel.add(backButton);

      return editPanel;
    }

    editPanel.setLayout(new GridLayout(8, 2, 10, 10));

    // Dropdown to select post to edit
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

    // Save Button
    JButton saveButton = new JButton("Save Post");

    editPanel.add(saveButton);

    // Populate fields when a post is selected
    postDropdown.addActionListener(e -> {
      Post selectedPost = (Post) postDropdown.getSelectedItem();
      if (selectedPost != null) {
        platformField.setText(selectedPost.getPlatform());
        dateField.setText(selectedPost.getDate());
        conceptField.setText(selectedPost.getConcept());
        categoryDropdown.setSelectedItem(selectedPost.getCategory());
        statusDropdown.setSelectedItem(selectedPost.getStatus());
      }
    });

    // Save button
    saveButton.addActionListener(e -> {
      Post selectedPost = (Post) postDropdown.getSelectedItem();
      if (selectedPost != null) {
        selectedPost.setPlatform(platformField.getText());
        selectedPost.setDate(dateField.getText());
        selectedPost.setConcept(conceptField.getText());
        selectedPost.setCategory((Category) categoryDropdown.getSelectedItem());
        selectedPost.setStatus((Status) statusDropdown.getSelectedItem());

        PostStorage.savePosts(ContentPlanner.posts);
        refreshTable();
        JOptionPane.showMessageDialog(frame, "Post Saved");
      }
    });

    return editPanel;
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

  private void refreshTable() {
    if (tableModel == null) {
      return;
    }
      tableModel.setRowCount(0);

      for (Post post : ContentPlanner.posts) {
        tableModel.addRow(new Object[] {
            post.getId(),
            post.getPlatform(),
            post.getDate(),
            post.getConcept(),
            post.getCategory(),
            post.getStatus(),
        });
      }
    }
  }
