package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositiories.RecipeRepository;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
//DataJPATest yerine SpringBootTest kullanmamizin nedeni servislerin ayaga kaldirilmasi için gerekli Context'e sahip olmamasi JP
@SpringBootTest
public class RecipeServiceIT {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeCommandToRecipe recipeCommandToRecipe;
    @Autowired
    private RecipeToRecipeCommand recipeToRecipeCommand;

    @Transactional
    @Test
    public void testSaveOfDescription() {
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe recipe = recipes.iterator().next();
        RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
        String description = "Hayat güzel mi?";
        recipeCommand.setDescription(description);

        RecipeCommand saveRecipeCommand = recipeService.saveRecipeCommand(recipeCommand);

        assertThat(saveRecipeCommand.getDescription(), Matchers.equalTo(description));
        assertThat(saveRecipeCommand.getId(), Matchers.equalTo(recipe.getId()));
        assertThat(saveRecipeCommand.getCategories(), Matchers.hasSize(recipe.getCategories().size()));
        assertThat(saveRecipeCommand.getIngredients(), Matchers.hasSize(recipe.getIngredients().size()));

    }

}
