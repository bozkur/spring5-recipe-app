package guru.springframework.service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositiories.RecipeRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class ImageServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;
    private ImageServiceImpl imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void shouldSaveImage() {
        Long id = 1L;
        MultipartFile file = new MockMultipartFile("imagefile", "testing.txt", "text/plain", "SFG".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(id);
        when(recipeRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(recipe));

        ArgumentCaptor<Recipe> recipeCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveImage(id, file);

        Mockito.verify(recipeRepository, times(1)).save(recipeCaptor.capture());
        Recipe savedRecipe = recipeCaptor.getValue();
        assertThat(savedRecipe, Matchers.equalTo(recipe));
    }
}
