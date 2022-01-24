package guru.springframework.controller;

import guru.springframework.domain.Recipe;
import guru.springframework.service.RecipeService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IndexControllerTest {

    private RecipeService recipeService;
    private IndexController indexController;

    @Before
    public void setUp() {
        recipeService = mock(RecipeService.class);
        indexController = new IndexController(recipeService);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    public void shouldGetReturnString() {
        Set<Recipe> recipes = new HashSet<>();
        when(recipeService.getRecipes()).thenReturn(recipes);
        Model model = mock(Model.class);

        String index = indexController.index(model);

        assertThat(index, Matchers.equalTo("index"));
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(ArgumentMatchers.eq("recipes"), ArgumentMatchers.eq(recipes));
    }

}