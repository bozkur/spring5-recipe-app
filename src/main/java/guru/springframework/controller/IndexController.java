package guru.springframework.controller;

import guru.springframework.domain.Category;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositiories.CategoryRepository;
import guru.springframework.repositiories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
public class IndexController {

    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public IndexController(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @RequestMapping("/")
    public String index() {
        Optional<Category> category = categoryRepository.findByCategoryName("Mexican");
        Optional<UnitOfMeasure> uom = unitOfMeasureRepository.findByDescription("Pinch");
        log.info("Found category's id: {}", category.get().getId());
        log.info("Found unit of measure's id:{}", uom.get().getId());
        return "index";
    }
}
