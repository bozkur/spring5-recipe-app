package guru.springframework.controller;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.ImageService;
import guru.springframework.service.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

@Controller
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("recipe/{id}/image")
    public String getImageForm(@PathVariable String id, Model model) {
        Long recipeId = Long.valueOf(id);
        RecipeCommand recipe = recipeService.findCommandById(recipeId);
        model.addAttribute("recipe", recipe);
        return "recipe/imageuploadform";
    }

    @PostMapping("recipe/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile")MultipartFile file) {
        Long recipeId = Long.valueOf(id);
        imageService.saveImage(recipeId, file);
        return "redirect:/recipe/" + id + "/show";
    }

    @GetMapping("recipe/{id}/recipeimage")
    public void readImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        Long recipeId = Long.valueOf(id);
        RecipeCommand recipe = recipeService.findCommandById(recipeId);
        if (recipe == null) {
            return;
        }
        Byte[] image = recipe.getImage();
        byte [] imageBytes = new byte[image.length];
        for(int i = 0; i < image.length; i++) {
            imageBytes[i] = image[i];
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            IOUtils.copy(bis, response.getOutputStream());
        }
    }
}
