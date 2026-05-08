package rosemary_and_thyme.backend.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import rosemary_and_thyme.backend.repositories.recipes.RecipeDTO
import rosemary_and_thyme.backend.services.RecipeService
import java.util.UUID

@RestController
@RequestMapping("/api/recipe")
class RecipeController(private val recipeService: RecipeService) {


    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable id: UUID): RecipeDTO =

        recipeService.findRecipeById(id)
}