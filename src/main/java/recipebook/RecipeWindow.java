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
        private DefaultListModel<String> recipeListModel;  // Модель для хранения списка рецептов
        private JList<String> recipeList;  // GUI элемент для отображения списка рецептов
        private JTextArea recipeDescription;  // Поле для отображения описания рецепта

        // Конструктор для создания окна
        public RecipeWindow() {
            setTitle("Рецепты");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Модель и список рецептов
            recipeListModel = new DefaultListModel<>();
            recipeList = new JList<>(recipeListModel);
            recipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Обработчик выбора рецепта из списка
            recipeList.addListSelectionListener(e -> {
                int selectedIndex = recipeList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String description = getRecipeDescription(selectedIndex);
                    recipeDescription.setText(description);
                }
            });

            // Поле для отображения описания рецепта
            recipeDescription = new JTextArea();
            recipeDescription.setEditable(false);
            recipeDescription.setLineWrap(true);
            recipeDescription.setWrapStyleWord(true);

            // Размещение компонентов на форме
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(recipeList), new JScrollPane(recipeDescription));
            splitPane.setDividerLocation(150);
            add(splitPane, BorderLayout.CENTER);
        }

        // Метод интерфейса RecipeListener для обновления списка рецептов
        @Override
        public void onRecipesUpdated(String[][] recipes) {
            recipeListModel.clear();  // Очищаем старые рецепты
            for (String[] recipe : recipes) {
                recipeListModel.addElement(recipe[0]);  // Добавляем названия рецептов в список
            }
        }

        // Получение описания рецепта по индексу
        private String getRecipeDescription(int index) {
            if (index >= 0 && index < recipeListModel.size()) {
                String[][] recipes = getRecipesFromSource();  // Здесь вы бы подгружали рецепты из вашего источника данных
                return recipes[index][1];
            }
            return "Описание не найдено.";
        }

        // Здесь можно подставить свой метод получения данных или подписаться на другие события
        private String[][] getRecipesFromSource() {
            return new String[][] {
                    {"Паста", "Рецепт пасты с томатным соусом и сыром."},
                    {"Салат", "Свежий салат из огурцов и помидоров с заправкой."}
            };
        }

        // Тестовый метод для запуска окна
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                RecipeWindow window = new RecipeWindow();
                window.setVisible(true);

                // Эмуляция обновления списка рецептов
                String[][] recipes = {
                        {"Борщ", "Классический рецепт борща с капустой и свеклой."},
                        {"Плов", "Ароматный плов с курицей и специями."}
                };
                window.onRecipesUpdated(recipes);  // Имитация события обновления рецептов
            });
        }
    }
