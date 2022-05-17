package guru.springframework.controller;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.domain.Ingredient;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientsController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService uomService;

    public IngredientsController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService uomService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.uomService = uomService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        Long actualRecipeId = Long.valueOf(recipeId);
        RecipeCommand recipe = recipeService.findCommandById(actualRecipeId);
        model.addAttribute("recipe", recipe);
        return "ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{id}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
        Long actualRecipeId = Long.valueOf(recipeId);
        Long actualId = Long.valueOf(id);
        IngredientCommand ingredient = ingredientService.findByRecipeIdAndIngredientId(actualRecipeId, actualId);
        model.addAttribute("ingredient", ingredient);
        return "ingredient/show";
    }


    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
        Long actualRecipeId = Long.valueOf(recipeId);
        Long actualId = Long.valueOf(id);
        IngredientCommand ingredient = ingredientService.findByRecipeIdAndIngredientId(actualRecipeId, actualId);
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("uomList",uomService.listAllUoms());
        return "ingredient/ingredientform";
    }

    @PostMapping
    @RequestMapping("/recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("Saved recipe id: {}", savedCommand.getRecipeId());
        log.debug("Saved ingredient command: {}", savedCommand.getId());
        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/"+ savedCommand.getId() +"/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        Long actualRecipeId = Long.valueOf(recipeId);
        IngredientCommand command = new IngredientCommand();
        command.setRecipeId(actualRecipeId);

        model.addAttribute("ingredient", command);
        model.addAttribute("uomList",uomService.listAllUoms());
        return "ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{id}/delete")
    public String delete(@PathVariable String recipeId, @PathVariable String id) {
        Long actualRecipeId = Long.valueOf(recipeId);
        Long actualId = Long.valueOf(id);
        
        ingredientService.delete(actualRecipeId,  actualId);
        return "redirect:/recipe/" +  actualRecipeId + "/ingredients";
    }
}
