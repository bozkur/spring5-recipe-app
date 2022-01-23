package guru.springframework.service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositiories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{

    private RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("I'm şn service.");
        Set<Recipe> result = new HashSet<>();
        recipeRepository.findAll().forEach(r -> {
            r.setBase64Image(getBase64Image(r));
            result.add(r);
        });
        return result;
    }

    private String getBase64Image(Recipe recipe) {

        Byte [] image = recipe.getImage();
        byte[] bytes = new byte[image.length];
        for (int i=0; i<bytes.length; i++) {
            bytes[i] = image[i];
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
