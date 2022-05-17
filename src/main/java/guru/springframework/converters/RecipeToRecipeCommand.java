package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Category;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    private final NotesToNotesCommand notesConverter;
    private final CategoryToCategoryCommand categoryConverter;
    private final IngredientToIngredientCommand ingredientConverter;

    public RecipeToRecipeCommand(NotesToNotesCommand notesConverter, CategoryToCategoryCommand categoryConverter, IngredientToIngredientCommand ingredientConverter) {
        this.notesConverter = notesConverter;
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe recipe) {
        if(recipe == null) {
            return null;
        }
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipe.getId());
        recipeCommand.setDescription(recipe.getDescription());
        recipeCommand.setDifficulty(recipe.getDifficulty());
        recipeCommand.setPrepTime(recipe.getPrepTime());
        recipeCommand.setCookTime(recipe.getCookTime());
        recipeCommand.setServings(recipe.getServings());
        recipeCommand.setUrl(recipe.getUrl());
        recipeCommand.setDirections(recipe.getDirections());
        recipeCommand.setNotes(notesConverter.convert(recipe.getNotes()));
        recipeCommand.setSource(recipe.getSource());
        Set<Category> categories = recipe.getCategories();
        if(categories != null) {
            Set<CategoryCommand> categoryCommands = categories.stream().map(categoryConverter::convert).collect(Collectors.toSet());
            recipeCommand.setCategories(categoryCommands);
        }
        Set<Ingredient> ingredients = recipe.getIngredients();
        ingredients.stream().forEach(i -> {if (i.getRecipe() == null) {
          System.out.println("Recipe is null");
        } else
            System.out.println(i.getRecipe().getId());
        });
        if(ingredients != null && !ingredients.isEmpty()) {
            Set<IngredientCommand> ingredientCommands = ingredients.stream().map(ingredientConverter::convert).collect(Collectors.toSet());
            recipeCommand.setIngredients(ingredientCommands);
        }
        recipeCommand.setDifficulty(recipe.getDifficulty());
        recipeCommand.setImage(recipe.getImage());
        return recipeCommand;
    }
}
