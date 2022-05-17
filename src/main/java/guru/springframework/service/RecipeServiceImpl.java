package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositiories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandConverter;
    private final RecipeToRecipeCommand recipeConverter;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandConverter, RecipeToRecipeCommand recipeConverter) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandConverter = recipeCommandConverter;
        this.recipeConverter = recipeConverter;
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

    @Override
    public Recipe findById(long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if(!optionalRecipe.isPresent()) {
            throw new RuntimeException("Requested recipe with id " + id + " can not be found");
        }
        return optionalRecipe.get();
    }

    @Override
    public RecipeCommand findCommandById(long id) {
        return recipeConverter.convert(findById(id));
    }

    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe recipe = recipeCommandConverter.convert(command);
        Recipe savedRecipe = recipeRepository.save(recipe);
        log.info("Saved recipe with id: {}", savedRecipe.getId());
        return recipeConverter.convert(savedRecipe);
    }

    @Override
    public void delete(long id) {
        log.debug("Deleting recipe with id: {}", id);
        recipeRepository.deleteById(id);
    }

    private String getBase64Image(Recipe recipe) {

        Byte [] image = recipe.getImage();
        if (image == null) {
            return null;
        }
        byte[] bytes = new byte[image.length];
        for (int i=0; i<bytes.length; i++) {
            bytes[i] = image[i];
        }
        return Base64.getEncoder().encodeToString(bytes);
    }
}
