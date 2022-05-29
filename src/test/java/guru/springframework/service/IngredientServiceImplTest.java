package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositiories.IngredientRepository;
import guru.springframework.repositiories.RecipeRepository;
import guru.springframework.repositiories.UnitOfMeasureRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private UnitOfMeasureRepository uomRepository;
    @Mock
    private IngredientRepository ingredientRepository;
    private IngredientServiceImpl ingredientService;
    private IngredientCommandToIngredient ingredientCommandToIngredient;
    private IngredientToIngredientCommand ingredientToIngredientCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
        ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        ingredientService = new IngredientServiceImpl(recipeRepository, uomRepository, ingredientRepository, ingredientToIngredientCommand, ingredientCommandToIngredient);
    }

    @Test
    void shouldSaveIngredientCommand() {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(1L);
        ingredientCommand.setId(3L);

        Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().forEach(i -> i.setId(3L));
        when(recipeRepository.findById(1L)).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        assertEquals(Long.valueOf(3L), savedIngredientCommand.getId());

    }

    @Test
    public void updateExistingIngredient() {
        IngredientCommand ingredient = new IngredientCommand();
        ingredient.setRecipeId(1L);

        UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
        uomCommand.setDescription("Tea spoon");
        uomCommand.setId(1L);
        ingredient.setUom(uomCommand);

        ingredient.setAmount(BigDecimal.valueOf(15.0));
        ingredient.setDescription("New ingredient");

        Recipe recipe = new Recipe();
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        Recipe savedRecipe= new Recipe();
        savedRecipe.setId(recipe.getId());
        Ingredient expectedIngredient = new Ingredient();
        expectedIngredient.setId(25L);
        expectedIngredient.setRecipe(savedRecipe);
        expectedIngredient.setUom(new UnitOfMeasureCommandToUnitOfMeasure().convert(ingredient.getUom()));
        expectedIngredient.setAmount(ingredient.getAmount());
        expectedIngredient.setDescription(ingredient.getDescription());
        Set<Ingredient> expectedIngredients = new HashSet<>();
        expectedIngredients.add(expectedIngredient);
        savedRecipe.setIngredients(expectedIngredients);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredient);

        assertThat(savedCommand.getAmount(), Matchers.equalTo(ingredient.getAmount()));
        assertThat(savedCommand.getDescription(), Matchers.equalTo(ingredient.getDescription()));
        assertThat(savedCommand.getUom().getDescription(), Matchers.equalTo(ingredient.getUom().getDescription()));
    }

    @Test
    void shouldFindByRecipeIdAndIngredientId() {
        long recipeId = 1L;
        long ingredientId = 2L;
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setId(ingredientId);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(3L);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setId(4L);

        Recipe recipe = new Recipe();
        recipe.setId(recipeId);
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        IngredientCommand command = ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId);
        assertThat(command.getId(), Matchers.equalTo(ingredientId));
        assertThat(command.getRecipeId(), Matchers.equalTo(recipe.getId()));
        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    void shouldDeleteIngredient() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);

        recipe.addIngredient(ingredient);

        when(recipeRepository.findById(recipe.getId())).thenReturn(Optional.of(recipe));

        ingredientService.delete(recipe.getId(), ingredient.getId());

        verify(ingredientRepository).deleteById(ingredient.getId());

    }
}