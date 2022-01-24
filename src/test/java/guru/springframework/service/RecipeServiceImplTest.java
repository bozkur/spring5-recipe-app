package guru.springframework.service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositiories.RecipeRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getRecipes() {
        RecipeServiceImpl recipeService = new RecipeServiceImpl(recipeRepository);
        HashSet<Recipe> recipes = new HashSet<>();
        Recipe recipe = new Recipe();
        recipes.add(recipe);

        when(recipeRepository.findAll()).thenReturn(recipes);

        Set<Recipe> obtainedRecipes = recipeService.getRecipes();
        assertThat(obtainedRecipes, Matchers.hasSize(1));
        verify(recipeRepository, times(1)).findAll();
    }
}