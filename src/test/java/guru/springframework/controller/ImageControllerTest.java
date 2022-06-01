package guru.springframework.controller;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.ImageService;
import guru.springframework.service.RecipeService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Arrays;

public class ImageControllerTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private ImageService imageService;

    private ImageController controller;
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new ImageController(recipeService,imageService);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(ExceptionControllerAdvice.class)
                .build();
    }

    @Test
    void shouldGetImageForm() throws Exception {
        RecipeCommand recipe = new RecipeCommand();
        recipe.setId(1L);
        Mockito.when(recipeService.findCommandById(ArgumentMatchers.anyLong())).thenReturn(recipe);
       mvc.perform(MockMvcRequestBuilders.get("/recipe/1/image"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("recipe"));
    }

    @Test
    void shouldPostImageFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain", "String framework guru".getBytes());
        mvc.perform(MockMvcRequestBuilders.multipart("/recipe/1/image").file(multipartFile))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/recipe/1/show"));
        Mockito.verify(imageService, Mockito.times(1)).saveImage(ArgumentMatchers.anyLong(), ArgumentMatchers.any());
    }

    @Test
    void shouldGetRecipeImage() throws Exception {

        RecipeCommand recipeCommand = new RecipeCommand();
        byte[] content = "Content text".getBytes();
        Byte[] expectedContent = new Byte[content.length];

        for (int i = 0; i < content.length; i++) {
            expectedContent[i] = content[i];
        }

        recipeCommand.setImage(expectedContent);

        Mockito.when(recipeService.findCommandById(1)).thenReturn(recipeCommand);

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get("/recipe/1/recipeimage"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        byte[] imageBytes = response.getContentAsByteArray();

        MatcherAssert.assertThat(imageBytes, Matchers.equalTo(content));
    }

    @Test
    void shouldGoToErroPageWhenNumberFormatExceptionOccurs() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/recipe/asd/image"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
