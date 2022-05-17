package guru.springframework.controller;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngredientsControllerTest {

    @Mock
    private RecipeService recipeService;
    @Mock
    private IngredientService ingredientService;
    @Mock
    private UnitOfMeasureService uomService;
    private IngredientsController controller;
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new IngredientsController(recipeService, ingredientService, uomService);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldListIngredients() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId(1L);
        recipe.setIngredients(new HashSet<>());
        when(recipeService.findCommandById(1L)).thenReturn(recipe);
        mvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredients"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ingredient/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
    }

    @Test
    void shouldShowIngredient() throws Exception {
        IngredientCommand ingredient = new IngredientCommand();
        ingredient.setId(2L);
        ingredient.setRecipeId(1L);
        when(ingredientService.findByRecipeIdAndIngredientId(ingredient.getRecipeId(),
                ingredient.getId())).thenReturn(ingredient);

        mvc.perform(MockMvcRequestBuilders.get("/recipe/1/ingredient/2/show"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ingredient/show"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient"));

        verify(ingredientService).findByRecipeIdAndIngredientId(ingredient.getRecipeId(), ingredient.getId());
    }

    @Test
    void shouldUpdateRecipientIngredient() throws Exception {

        long recipeId = 1L;
        long id = 2L;

        IngredientCommand command = new IngredientCommand();
        when(ingredientService.findByRecipeIdAndIngredientId(recipeId, id)).thenReturn(command);

        mvc.perform(MockMvcRequestBuilders.get("/recipe/"+ recipeId +"/ingredient/" + id + "/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ingredient/ingredientform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("ingredient", "uomList"));
    }

    @Test
    void shouldSaveOrUpdate() throws Exception {
        long recipeId = 1L;
        long id = 2L;

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        ingredientCommand.setId(id);
        when(ingredientService.saveIngredientCommand(ArgumentMatchers.any())).thenReturn(ingredientCommand);

        mvc.perform(MockMvcRequestBuilders.post("/recipe/"+ recipeId +"/ingredient", new IngredientCommand()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/recipe/"+ recipeId +"/ingredient/" + id + "/show"));
    }

    @Test
    void shouldDelete() throws Exception {
        long recipeId = 1L;
        long ingredientId = 34L;
        mvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeId + "/ingredient/" + ingredientId + "/delete"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/recipe/"+ recipeId +"/ingredients"));
    }
}