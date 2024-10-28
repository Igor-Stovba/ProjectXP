package recipebook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

interface RecipeListener {
    void onRecipesUpdated(String[][] recipes);
}

// Класс окна рецептов
public class RecipeWindow extends JFrame implements RecipeListener {
    private DefaultListModel<String> recipeListModel;
    private JList<String> recipeList;
    private JTextArea recipeDescription;
    private JTextField nameField;
    private JTextField descriptionField;
    private final Mongo db = new Mongo();

    private List<String[]> recipes;

    public RecipeWindow() {
        setTitle("Рецепты");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        recipes = new ArrayList<>();

        recipeListModel = new DefaultListModel<>();
        recipeList = new JList<>(recipeListModel);
        recipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        recipeList.addListSelectionListener(e -> {
            int selectedIndex = recipeList.getSelectedIndex();
            if (selectedIndex != -1) {
                recipeDescription.setText(recipes.get(selectedIndex)[1]);
            }
        });

        recipeDescription = new JTextArea();
        recipeDescription.setEditable(false);
        recipeDescription.setLineWrap(true);
        recipeDescription.setWrapStyleWord(true);

        nameField = new JTextField(10);
        descriptionField = new JTextField(20);

        JButton addButton = new JButton("Добавить рецепт");
        addButton.addActionListener(e -> addRecipe());

        JButton deleteButton = new JButton("Удалить по имени");
        deleteButton.addActionListener(e -> deleteRecipe());

        JButton showAllButton = new JButton("Показать все рецепты");
        showAllButton.addActionListener(e -> showAllRecipes());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(new JLabel("Название:"));
        controlPanel.add(nameField);
        controlPanel.add(new JLabel("Описание:"));
        controlPanel.add(descriptionField);
        controlPanel.add(addButton);
        controlPanel.add(deleteButton);
        controlPanel.add(showAllButton);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(recipeList), new JScrollPane(recipeDescription));
        splitPane.setDividerLocation(150);

        add(controlPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void addRecipe() {
        String name = nameField.getText().trim();
        String description = descriptionField.getText().trim();

        db.setData(name, description);

        if (!name.isEmpty() && !description.isEmpty()) {
            recipes.add(new String[] {name, description});
            recipeListModel.addElement(name);
            nameField.setText("");
            descriptionField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Введите название и описание рецепта.");
        }
    }

    private void deleteRecipe() {
        String name = nameField.getText().trim();

        db.delData(name);

        if (!name.isEmpty()) {
            boolean removed = false;
            for (int i = 0; i < recipes.size(); i++) {
                if (recipes.get(i)[0].equalsIgnoreCase(name)) {
                    recipes.remove(i);
                    recipeListModel.remove(i);
                    removed = true;
                    break;
                }
            }
            if (removed) {
                JOptionPane.showMessageDialog(this, "Рецепт \"" + name + "\" удален.");
                nameField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Рецепт с таким именем не найден.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Введите название рецепта для удаления.");
        }
    }

    private void showAllRecipes() {
        StringBuilder allRecipes = new StringBuilder();
        for (String[] recipe : recipes) {
            allRecipes.append(recipe[0]).append(": ").append(recipe[1]).append("\n\n");
        }
        recipeDescription.setText(allRecipes.toString());
    }

    @Override
    public void onRecipesUpdated(String[][] newRecipes) {
        recipeListModel.clear();
        recipes.clear();
        for (String[] recipe : newRecipes) {
            recipes.add(recipe);
            recipeListModel.addElement(recipe[0]);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RecipeWindow window = new RecipeWindow();
            window.setVisible(true);
        });
    }
}
