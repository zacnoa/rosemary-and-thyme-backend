package rosemary_and_thyme.backend.repositories.recipes

import kotlinx.coroutines.*
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import rosemary_and_thyme.backend.models.public.tables.HeroImages.Companion.HERO_IMAGES
import rosemary_and_thyme.backend.models.public.tables.Ingredients.Companion.INGREDIENTS
import rosemary_and_thyme.backend.models.public.tables.InstructionImages.Companion.INSTRUCTION_IMAGES
import rosemary_and_thyme.backend.models.public.tables.Instructions.Companion.INSTRUCTIONS
import rosemary_and_thyme.backend.models.public.tables.Recipes.Companion.RECIPES
import rosemary_and_thyme.backend.models.public.tables.records.HeroImagesRecord
import rosemary_and_thyme.backend.models.public.tables.records.IngredientsRecord
import rosemary_and_thyme.backend.models.public.tables.records.InstructionImagesRecord
import rosemary_and_thyme.backend.models.public.tables.records.InstructionsRecord
import rosemary_and_thyme.backend.models.public.tables.records.RecipesRecord
import java.util.*

@Repository
class RecipeRepository(private val dsl: DSLContext) {

    fun fetchRecipe(id: UUID): RecipeDTO = runBlocking {
        coroutineScope {
            val recipeDeferred: Deferred<RecipesRecord?> = async(Dispatchers.IO) { fetchRecipeBase(id) }
            val ingredientsDeferred: Deferred<List<IngredientsRecord>> = async(Dispatchers.IO) { fetchIngredients(id) }
            val instructionsDeferred: Deferred<List<InstructionsRecord>> =
                async(Dispatchers.IO) { fetchInstructions(id) }
            val heroImagesDeferred: Deferred<List<HeroImagesRecord>> = async(Dispatchers.IO) { fetchHeroImages(id) }

            val recipe = recipeDeferred.await() ?: throw NoSuchElementException("Recipe $id not found")
            val ingredients = ingredientsDeferred.await()
            val instructions = instructionsDeferred.await()
            val heroImages = heroImagesDeferred.await()

            val instructionImages = fetchInstructionImages(instructions.mapNotNull{ it.id })


            Utils.buildRecipeDTO(
                ingredients = ingredients,
                instructions = instructions,
                heroImages = heroImages,
                recipe =recipe,
                instructionImages = instructionImages,
            )
        }
    }

    private fun fetchRecipeBase(recipeId: UUID): RecipesRecord? =
        dsl.selectFrom(RECIPES)
            .where(RECIPES.ID.eq(recipeId))
            .fetchOneInto(RecipesRecord::class.java)


    private fun fetchIngredients(recipeId: UUID): List<IngredientsRecord> =
        dsl.selectFrom(INGREDIENTS)
            .where(INGREDIENTS.RECIPE_ID.eq(recipeId))
            .orderBy(INGREDIENTS.INDEX)
            .fetchInto(IngredientsRecord::class.java)

    private fun fetchInstructions(recipeId: UUID): List<InstructionsRecord> =
        dsl.selectFrom(INSTRUCTIONS)
            .where(INSTRUCTIONS.RECIPE_ID.eq(recipeId))
            .orderBy(INSTRUCTIONS.INDEX)
            .fetchInto(InstructionsRecord::class.java)

    private fun fetchHeroImages(recipeId: UUID): List<HeroImagesRecord> =
        dsl.selectFrom(HERO_IMAGES)
            .where(HERO_IMAGES.RECIPE_ID.eq(recipeId))
            .orderBy(HERO_IMAGES.INDEX)
            .fetchInto(HeroImagesRecord::class.java)

    private fun fetchInstructionImages(instructionIds: List<UUID>): Map<UUID, List<InstructionImagesRecord>> =
        dsl.selectFrom(INSTRUCTION_IMAGES)
            .where(INSTRUCTION_IMAGES.INSTRUCTION_ID.`in`(instructionIds))
            .orderBy(INSTRUCTION_IMAGES.INDEX)
            .fetchInto(InstructionImagesRecord::class.java)
            .groupBy { it.instructionId }
}