import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

    JButton createButton = new JButton("Create");
    JButton editButton = new JButton("Edit");
    JButton deleteButton = new JButton("Delete");

    JButton[] buttons = {createButton, editButton, deleteButton};


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
    createButton.addActionListener(e -> cardLayout.show(cardPanel, "CREATE"));
    editButton.addActionListener(e -> {
      cardPanel.remove(cardPanel.getComponentCount() - 1);
      cardPanel.add(editPanel(), "EDIT");
      cardLayout.show(cardPanel, "EDIT");
    });
    deleteButton.addActionListener(e -> {
      int viewRow = postTable.getSelectedRow();

      if (viewRow == -1) {
        JOptionPane.showMessageDialog(null, "Please select a post from the table to delete!");
        return;
      }
      int modelRow = postTable.convertRowIndexToModel(viewRow);

      int confirm = JOptionPane.showConfirmDialog(frame,
          "Are you sure you want to delete this post?",
          "Confirm Delete",
          JOptionPane.YES_NO_OPTION);

      if (confirm != JOptionPane.YES_OPTION) {
        return;
      }

      ContentPlanner.posts.remove(modelRow);
      tableModel.removeRow(modelRow);
      PostStorage.savePosts(ContentPlanner.posts);
    });


    refreshTable();
    return mainPanel;
  }

  private JPanel buildPostForm(JTextField platformField, JTextField dateField, JTextField conceptField,
                               JComboBox<Category> categoryField, JComboBox<Status> statusField) {
    JPanel form = new JPanel(new GridLayout(5,2,10,10));
    form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40 ));

    form.add(new JLabel("Platform:"));
    form.add(platformField);

    form.add(new JLabel("Date:"));
    form.add(dateField);

    form.add(new JLabel("Concept:"));
    form.add(conceptField);

    form.add(new JLabel("Category:"));
    form.add(categoryField);

    form.add(new JLabel("Status:"));
    form.add(statusField);

    return form;
  }


  private JPanel createPostPanel() {
    JPanel createPanel = new JPanel(new BorderLayout());

    JTextField platformField = new JTextField();
    JTextField dateField = new JTextField();
    JTextField conceptField = new JTextField();

    JComboBox<Category> categoryDropdown = new JComboBox<>(Category.values());
    JComboBox<Status> statusDropdown = new JComboBox<>(Status.values());

    createPanel.add(buildPostForm(platformField, dateField, conceptField, categoryDropdown, statusDropdown),
        BorderLayout.CENTER);

    // Save and Back Buttons
    JButton saveButton = new JButton("Save Post");
    JButton backButton = new JButton("Back");

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(backButton);
    buttonPanel.add(saveButton);

    createPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Save Button Logic
    saveButton.addActionListener(e -> {
      if (isEmpty(platformField.getText()) || isEmpty(dateField.getText()) || isEmpty(conceptField.getText())) {
        JOptionPane.showMessageDialog(frame, "Please fill all the required fields!", "Missing Information", JOptionPane.WARNING_MESSAGE);
      return;
      }

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
      platformField.setText("");
      dateField.setText("");
      conceptField.setText("");
    });

    // Back Button Logic
    backButton.addActionListener(e -> {
      cardLayout.show(cardPanel, "MENU");
    });
    return createPanel;
  }


  private JPanel editPanel() {
    JPanel editPanel = new JPanel(new BorderLayout());

    if (ContentPlanner.posts.isEmpty()) {
      JLabel titleLabel = new JLabel("No posts found", JLabel.CENTER);
      editPanel.add(titleLabel, BorderLayout.CENTER);
      return editPanel;
    }

    // Text fields for editing
    JTextField platformField = new JTextField();
    JTextField dateField = new JTextField();
    JTextField conceptField = new JTextField();

    //Dropdowns for category and status
    JComboBox<Category> categoryDropdown = new JComboBox<>(Category.values());
    JComboBox<Status> statusDropdown = new JComboBox<>(Status.values());

    JComboBox<Post> postDropdown = new JComboBox<>(ContentPlanner.posts.toArray(new Post[0]));

    postDropdown.addActionListener(e -> {
      Post p = (Post) postDropdown.getSelectedItem();
      if (p != null) {
        platformField.setText(p.getPlatform());
        dateField.setText(p.getDate());
        conceptField.setText(p.getConcept());
        categoryDropdown.setSelectedItem(p.getCategory());
        statusDropdown.setSelectedItem(p.getStatus());
      }
    });

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(new JLabel("Select Post: "));
    topPanel.add(postDropdown);

    editPanel.add(topPanel, BorderLayout.NORTH);

    editPanel.add(buildPostForm(platformField, dateField, conceptField, categoryDropdown, statusDropdown),
    BorderLayout.CENTER
    );

    //Save and Back Buttons
    JButton saveButton = new JButton("Save Changes");
    JButton backButton = new JButton("Back");

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(backButton);
    buttonPanel.add(saveButton);

    editPanel.add(buttonPanel, BorderLayout.SOUTH);

    backButton.addActionListener(e -> {
      cardLayout.show(cardPanel, "MENU");
    });

    saveButton.addActionListener(e -> {
     if (isEmpty(platformField.getText()) || isEmpty(dateField.getText()) || isEmpty(conceptField.getText())) {
       JOptionPane.showMessageDialog(frame, "Please fill all the required fields!", "Missing Information", JOptionPane.WARNING_MESSAGE);
       return;
     }

     Post post = (Post) postDropdown.getSelectedItem();
     if (post != null) {
       post.setPlatform(platformField.getText());
       post.setDate(dateField.getText());
       post.setConcept(conceptField.getText());
       post.setCategory((Category) categoryDropdown.getSelectedItem());
       post.setStatus((Status) statusDropdown.getSelectedItem());


       PostStorage.savePosts(ContentPlanner.posts);
       refreshTable();
       JOptionPane.showMessageDialog(frame, "Post saved!");
       platformField.setText("");
       dateField.setText("");
       conceptField.setText("");
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

  private boolean isEmpty(String text) {
    return text == null || text.trim().isEmpty();
  }
}
