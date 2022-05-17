package guru.springframework.service;

import guru.springframework.domain.Recipe;
import guru.springframework.repositiories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImage(Long recipeId, MultipartFile file) {
        log.debug("Saving image for recipe with id: {}", recipeId);
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        try{
            byte[] bytes = file.getBytes();
            Byte[] fileBytes = new Byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                fileBytes[i] = bytes[i];
            }
            recipe.get().setImage(fileBytes);
            recipeRepository.save(recipe.get());
        } catch (Exception exception) {

        }
    }
}
