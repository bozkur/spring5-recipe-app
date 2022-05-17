package guru.springframework.service;

import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositiories.RecipeRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;
    private RecipeServiceImpl recipeService;
    @Mock
    private RecipeCommandToRecipe recipeCommandConverter;
    @Mock
    private RecipeToRecipeCommand recipeConverter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandConverter, recipeConverter);
    }

    @Test
    public void getRecipes() {
        HashSet<Recipe> recipes = new HashSet<>();
        Recipe recipe = new Recipe();
        recipes.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipes);

        Set<Recipe> obtainedRecipes = recipeService.getRecipes();
        assertThat(obtainedRecipes, Matchers.hasSize(1));
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    public void getRecipeById() {
        long id = 1L;
        Recipe recipe = new Recipe();
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));

        Recipe found = recipeService.findById(id);
        assertThat(found, Matchers.equalTo(recipe));
    }

    @Test
    public void deleteRecipe() {
        long id = 2L;

        recipeService.delete(id);

        verify(recipeRepository, times(1)).deleteById(ArgumentMatchers.eq(id));

    }
}