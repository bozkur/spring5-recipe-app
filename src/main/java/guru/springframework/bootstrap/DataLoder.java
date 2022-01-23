package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositiories.CategoryRepository;
import guru.springframework.repositiories.RecipeRepository;
import guru.springframework.repositiories.UnitOfMeasureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataLoder implements CommandLineRunner {

    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public DataLoder(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        UnitOfMeasure tablespoon = unitOfMeasureRepository.findByDescription("Tablespoon").get();
        UnitOfMeasure teaspoon = unitOfMeasureRepository.findByDescription("Teaspoon").get();
        UnitOfMeasure pinch = unitOfMeasureRepository.findByDescription("Pinch").get();
        Recipe perfectGuacomole = getPerfectGuacomole(tablespoon, teaspoon, pinch);
        Recipe spicyChickenTacos = getSpicyChickenTacos(tablespoon, teaspoon, pinch);
        recipeRepository.save(perfectGuacomole);
        recipeRepository.save(spicyChickenTacos);
    }

    private Recipe getPerfectGuacomole(UnitOfMeasure tablespoon, UnitOfMeasure teaspoon, UnitOfMeasure pinch) throws Exception {
        Recipe perfectGuacomole = new Recipe();
        perfectGuacomole.setDescription("Perfect Guacomole");
        Set<Category> categories = new HashSet<>();
        Category mexican = categoryRepository.findByCategoryName("Mexican").get();
        categories.add(mexican);
        perfectGuacomole.setCategories(categories);
        perfectGuacomole.setDifficulty(Difficulty.EASY);
        perfectGuacomole.setDirections("");
        perfectGuacomole.setPrepTime(10);
        perfectGuacomole.setServings(4);
        perfectGuacomole.setImage(readImg("img/guacomole.webp"));
        perfectGuacomole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        perfectGuacomole.setDirections(readDirections("directions/guacomole.txt"));

        Ingredient avocado = new Ingredient();
        avocado.setDescription("Ripe avocados");
        avocado.setAmount(BigDecimal.valueOf(2));

        Ingredient salt = new Ingredient();
        salt.setDescription("salt, plus more to taste");
        salt.setAmount(BigDecimal.valueOf(0.25));
        salt.setUom(teaspoon);

        Ingredient lime = new Ingredient();
        lime.setDescription("fresh lime or lemon juice");
        lime.setAmount(BigDecimal.valueOf(1));
        lime.setUom(tablespoon);

        Ingredient onion = new Ingredient();
        onion.setDescription("minced red onion or thinly sliced green onion");
        onion.setAmount(BigDecimal.valueOf(2));
        onion.setUom(tablespoon);

        Ingredient chilis = new Ingredient();
        chilis.setDescription("serrano (or jalapeño) chilis, stems and seeds removed, minced");
        chilis.setAmount(BigDecimal.valueOf(1));

        Ingredient cilantro = new Ingredient();
        cilantro.setDescription("cilantro (leaves and tender stems), finely chopped");
        cilantro.setAmount(BigDecimal.valueOf(2));
        cilantro.setUom(tablespoon);

        Ingredient blackPepper = new Ingredient();
        blackPepper.setDescription("freshly ground black pepper");
        blackPepper.setAmount(BigDecimal.ONE);
        blackPepper.setUom(pinch);

        Ingredient tomato = new Ingredient();
        tomato.setDescription("ripe tomato, chopped (optional)");
        tomato.setAmount(BigDecimal.valueOf(0.5));

        Ingredient radish = new Ingredient();
        radish.setDescription("Red radish or jicama slices for garnish (optional)");
        radish.setAmount(BigDecimal.ONE);

        Ingredient tortillaChips = new Ingredient();
        tortillaChips.setDescription("Tortilla chips, to serve");
        tortillaChips.setAmount(BigDecimal.ONE);

        setIngredients(perfectGuacomole, avocado, salt, lime, onion, chilis, cilantro, blackPepper, tomato, radish, tortillaChips);
        Notes notes = new Notes();
        notes.setRecipeNotes("Chilling tomatoes hurts their flavor. So, if you want to add chopped tomato to your guacamole, add it just before serving.");
        notes.setRecipe(perfectGuacomole);
        perfectGuacomole.setNotes(notes);
        return perfectGuacomole;
    }

    private Byte[] readImg(String s) throws Exception {
        byte[] bytes = Files.readAllBytes(getPathFromResource(s));
        Byte[] boxedBytes = new Byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) {
            boxedBytes[i] = bytes[i];
        }
        return boxedBytes;
    }

    private Path getPathFromResource(String resourceName) throws URISyntaxException {
        URL url = ClassLoader.getSystemClassLoader().getResource(resourceName);
        return Paths.get(url.toURI());
    }

    private String readDirections(String directionsFile) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        Files.lines(getPathFromResource(directionsFile)).forEach(s -> stringBuilder.append(s).append("<br/>"));
        return stringBuilder.toString();
    }

    private void setIngredients(Recipe recipe, Ingredient ... ingredients) {
        Set<Ingredient> ingredientSet = new HashSet<>();
        for (Ingredient in: ingredients) {
            in.setRecipe(recipe);
          ingredientSet.add(in);
        }
        recipe.setIngredients(ingredientSet);
    }

    private Recipe getSpicyChickenTacos(UnitOfMeasure tablespoon, UnitOfMeasure teaspoon, UnitOfMeasure pinch) throws Exception {
        Recipe spicyChickenTaco = new Recipe();
        spicyChickenTaco.setDescription("Spicy Grilled Chicken Tacos");
        Set<Category> categories = new HashSet<>();
        Category mexican = categoryRepository.findByCategoryName("Mexican").get();
        categories.add(mexican);
        spicyChickenTaco.setCategories(categories);
        spicyChickenTaco.setDifficulty(Difficulty.MODERATE);
        spicyChickenTaco.setDirections("");
        spicyChickenTaco.setPrepTime(20);
        spicyChickenTaco.setCookTime(15);
        spicyChickenTaco.setServings(6);
        spicyChickenTaco.setImage(readImg("img/spicy_chicken_taco.webp"));
        spicyChickenTaco.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        spicyChickenTaco.setDirections(readDirections("directions/spicy_chicken_taco.txt"));



        UnitOfMeasure clove = unitOfMeasureRepository.findByDescription("Clove").get();
        UnitOfMeasure cup = unitOfMeasureRepository.findByDescription("Cup").get();
        UnitOfMeasure pint = unitOfMeasureRepository.findByDescription("Pint").get();

        Ingredient chiliPowder = createIngredient(2, tablespoon, "ancho chili powder");
        Ingredient driedOregano = createIngredient(1, teaspoon, "dried oregano");
        Ingredient driedCumin = createIngredient(1, teaspoon, "dried cumin");
        Ingredient sugar = createIngredient(1, teaspoon, "sugar");
        Ingredient salt = createIngredient(0.5, teaspoon, "salt");
        Ingredient garlic = createIngredient(1, clove, "garlic, finely chopped");
        Ingredient orange = createIngredient(1, tablespoon, "finely grated orange zest ");
        Ingredient orangeJuice = createIngredient(3, tablespoon, " fresh-squeezed orange juice");
        Ingredient oliveOil = createIngredient(2, tablespoon, "olive oil");
        Ingredient chickenThighs = createIngredient(6, null, "boneless chicken thighs");
        Ingredient tortillas = createIngredient(8, null, "small corn tortillas");
        Ingredient arugula = createIngredient(3, cup, "packed baby arugula");
        Ingredient avocado = createIngredient(2, null, "medium ripe avocados, sliced");
        Ingredient radish = createIngredient(4, null, "radishes, thinly sliced");
        Ingredient cherryTomato = createIngredient(0.5, pint, "cherry tomatoes, halved");
        Ingredient onion = createIngredient(0.25, null, "red onion, thinly sliced ");
        Ingredient cilantro = createIngredient(1, null, "Roughly chopped cilantro");
        Ingredient cream = createIngredient(0.5, cup, "cup sour cream");
        Ingredient milk = createIngredient(0.25, cup, "milk");
        Ingredient lime = createIngredient(1, null, "lime, cut into wedges");

        setIngredients(spicyChickenTaco, chiliPowder, driedCumin, driedOregano, sugar, salt, garlic,
                orange, orangeJuice, oliveOil, chickenThighs, tortillas, arugula, avocado, radish, cherryTomato,
                onion, cilantro, cream, milk, lime);
        Notes notes = new Notes();
        notes.setRecipeNotes("Look for ancho chile powder with the Mexican ingredients at your grocery store, on buy it online. (If you can't find ancho chili powder, you replace the ancho chili, the oregano, and the cumin with 2 1/2 tablespoons regular chili powder, though the flavor won't be quite the same.");
        spicyChickenTaco.setNotes(notes);
        notes.setRecipe(spicyChickenTaco);
        return spicyChickenTaco;
    }

    private Ingredient createIngredient(double amount, UnitOfMeasure measure, String description) {
        Ingredient ingredient = new Ingredient();
        ingredient.setAmount(BigDecimal.ONE);
        ingredient.setUom(measure);
        ingredient.setDescription(description);
        return ingredient;
    }
}
