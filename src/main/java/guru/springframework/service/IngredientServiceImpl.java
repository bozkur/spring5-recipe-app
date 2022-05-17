package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositiories.IngredientRepository;
import guru.springframework.repositiories.RecipeRepository;
import guru.springframework.repositiories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService{

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository uomRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientToIngredientCommand converter;
    private final IngredientCommandToIngredient commandConverter;

    public IngredientServiceImpl(RecipeRepository recipeRepository, UnitOfMeasureRepository uomRepository, IngredientRepository ingredientRepository, IngredientToIngredientCommand converter, IngredientCommandToIngredient commandConverter) {
        this.recipeRepository = recipeRepository;
        this.uomRepository = uomRepository;
        this.ingredientRepository = ingredientRepository;
        this.converter = converter;
        this.commandConverter = commandConverter;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if(!recipe.isPresent()) {
            return null;
        }
        Optional<Ingredient> ingredientOp = recipe.get().getIngredients().stream().filter(i -> ingredientId.equals(i.getId())).findFirst();
        Optional<IngredientCommand> ingredientCommandOp = ingredientOp.map(converter::convert);
        if (!ingredientCommandOp.isPresent()) {
            //todo hata verme kodu ekleyelim.
            log.error("Ingredient can not be found with id: {}", ingredientId);
        }
        return ingredientCommandOp.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Long recipeId = command.getRecipeId();
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (!recipeOptional.isPresent()) {
            log.error("Recipe not found for id: {}", command.getRecipeId());
            //TODO kotarmak lazim
            return new IngredientCommand();
        }
        Recipe recipe = recipeOptional.get();
        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ing -> ing.getId().equals(command.getId())).findFirst();
        if (ingredientOptional.isPresent()) {
            Ingredient ingredientFound = ingredientOptional.get();
           ingredientFound.setDescription(command.getDescription());
           ingredientFound.setAmount(command.getAmount());
           ingredientFound.setUom(uomRepository.findById(command.getUom().getId())
                   .orElseThrow(() -> new RuntimeException("UOM not found")));
        } else {
            recipe.addIngredient(commandConverter.convert(command));
        }
        Recipe savedRecipe = recipeRepository.save(recipe);


        Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                .filter(ri -> ri.getId().equals(command.getId())).findFirst();
        if (!savedIngredientOptional.isPresent()) {
            savedIngredientOptional = savedRecipe.getIngredients().stream().filter(ri -> ri.getDescription().equals(command.getDescription()))
                    .filter(ri -> ri.getAmount().equals(command.getAmount()))
                    .filter(ri -> ri.getUom().getId().equals(command.getUom().getId()))
                    .findFirst();
        }
        //TODO check for fail
        log.info("recipe id for ingredient: {}", savedIngredientOptional.get().getRecipe().getId());
        return converter.convert(savedIngredientOptional.get());
    }

    @Override
    public void delete(Long recipeId, Long ingredientId) {
        Optional<Recipe> foundRecipe = recipeRepository.findById(recipeId);
        if(!foundRecipe.isPresent()) {
            throw new RuntimeException("Recipe with id (" + recipeId + ") can not be found." );
        }
        Recipe recipe = foundRecipe.get();
        Optional<Ingredient> target = recipe.getIngredients().stream().filter(i -> ingredientId.equals(i.getId())).findFirst();
        if (!target.isPresent()) {
            throw new RuntimeException("Ingredient with id (" + ingredientId + ") can not be found." );
        }
        recipe.getIngredients().remove(target.get());
        //recipeRepository.save(recipe);
        //recipeRepository.save(recipe);
        ingredientRepository.deleteById(ingredientId);
    }

}
