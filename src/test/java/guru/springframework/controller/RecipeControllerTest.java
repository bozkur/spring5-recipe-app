package guru.springframework.controller;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.NotFoundException;
import guru.springframework.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;
    private RecipeController recipeController;
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService);
        mvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(ExceptionControllerAdvice.class)
                .build();
    }

    @Test
    void getRecipe() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/show"));
    }

    @Test
    void shouldSubmitNewRecipe() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));

    }

    @Test
    void shouldUpdateRecipe() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        when(recipeService.findCommandById(recipeCommand.getId())).thenReturn(recipeCommand);
        mvc.perform(MockMvcRequestBuilders.get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("recipe/recipeform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
    }

    @Test
    void shouldSaveRecipeCommand() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        RecipeCommand savedRecipeCommand = new RecipeCommand();
        savedRecipeCommand.setId(15L);
        when(recipeService.saveRecipeCommand(ArgumentMatchers.any())).thenReturn(savedRecipeCommand);
        String resultUrl = "redirect:/recipe/"+savedRecipeCommand.getId() + "/show";
        mvc.perform(MockMvcRequestBuilders.post("/recipe", recipeCommand))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name(resultUrl));
    }

    @Test
    void shouldDeleteRecipeWithId() throws Exception {
        long id = 2L;
        mvc.perform(MockMvcRequestBuilders.get("/recipe/" + id + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/"));
    }

    @Test
    void shouldSend404WhenRecipeCanNotBeFound() throws Exception {
        long recipeId = 3L;
        when(recipeService.findById(recipeId)).thenThrow(new NotFoundException(""));
        mvc.perform(MockMvcRequestBuilders.get("/recipe/" + recipeId + "/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("errors/404error"));
    }

    @Test
    void shouldSend400WhenNumberFormatExceptionOccurs() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/recipe/asd/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("errors/400error"));
    }

}