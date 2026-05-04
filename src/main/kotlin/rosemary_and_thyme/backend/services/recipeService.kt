package rosemary_and_thyme.backend.services

import org.springframework.stereotype.Service
import rosemary_and_thyme.backend.repositories.RecipeRepository


@Service
class RecipeService(private val recipeRepository: RecipeRepository){

}