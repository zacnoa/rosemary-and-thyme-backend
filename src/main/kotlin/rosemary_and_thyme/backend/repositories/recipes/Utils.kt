package rosemary_and_thyme.backend.repositories.recipes

import rosemary_and_thyme.backend.models.public.tables.records.HeroImagesRecord
import rosemary_and_thyme.backend.models.public.tables.records.IngredientsRecord
import rosemary_and_thyme.backend.models.public.tables.records.InstructionImagesRecord
import rosemary_and_thyme.backend.models.public.tables.records.InstructionsRecord
import rosemary_and_thyme.backend.models.public.tables.records.RecipesRecord
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import kotlin.collections.flatten
import kotlin.collections.mapNotNull
import kotlin.collections.sortedBy
import kotlin.let


data class RecipeImageDTO(
    val id: UUID,
    val url: String
)

data class IngredientDTO(
    val id: UUID,
    val name: String,
    val amount: BigDecimal,
    val measuringUnit: String
)

data class InstructionDTO(
    val id: UUID,
    val text: String,
    val images: List<UUID>  // frontend prima UUID[], samo id-eve slika
)

data class RecipeDTO(
    val id: UUID,
    val userId: UUID,
    val userName: String,
    val createDate: LocalDate,
    val name: String,
    val description: String,
    val likes: Long,
    val portions: Int,
    val cookTime: String,
    val difficulty: Int,
    val sideNotes: String?,
    val images: Map<UUID, RecipeImageDTO>,       // Record<UUID, RecipeImage>
    val ingredients: Map<UUID, IngredientDTO>,   // Record<UUID, Ingredient>
    val instructions: Map<UUID, InstructionDTO>, // Record<UUID, Instruction>
    val ingredientsOrder: List<UUID>,
    val instructionsOrder: List<UUID>,
    val heroImagesOrder: List<UUID>                   // UUID[]
)

class Utils {

    companion object {
        fun buildRecipeDTO(
            recipe: RecipesRecord,
            ingredients: List<IngredientsRecord>,
            instructions: List<InstructionsRecord>,
            instructionImages: Map<UUID, List<InstructionImagesRecord>>,
            heroImages: List<HeroImagesRecord>
        ): RecipeDTO {

            val imagesMap = mutableMapOf<UUID, RecipeImageDTO>()

            // hero images idu u images map
            heroImages.forEach { h ->
                h.id?.let { imagesMap[it] = RecipeImageDTO(id = it, url = h.url) }
            }

            // instruction images idu u images map
            instructionImages.values.flatten().forEach { img ->
                img.id?.let { imagesMap[it] = RecipeImageDTO(id = it, url = img.url) }
            }

            return RecipeDTO(
                id=recipe.id!!,
                userId = recipe.userId,
                userName = recipe.userName,
                createDate = recipe.createdDate!!,
                name = recipe.name,
                description = recipe.description,
                likes = recipe.likes!!,
                portions = recipe.portions,
                cookTime = recipe.cookTime,
                difficulty = recipe.difficulty,
                sideNotes = recipe.sideNotes,
                images = imagesMap,
                ingredients = ingredients
                    .associate { i ->
                        i.id!! to IngredientDTO(
                            id = i.id!!,
                            name = i.name,
                            amount = i.amount,
                            measuringUnit = i.measuringUnit
                        )
                    },
                instructions = instructions
                    .associate { inst ->
                        inst.id!! to InstructionDTO(
                            id = inst.id!!,
                            text = inst.instruction,
                            images = instructionImages[inst.id]?.mapNotNull { it.id } ?: emptyList()
                        )
                    },
                ingredientsOrder = ingredients.sortedBy { it.index }.mapNotNull { it.id },
                instructionsOrder = instructions.sortedBy { it.index }.mapNotNull { it.id },
                heroImagesOrder = heroImages.sortedBy { it.index }.mapNotNull { it.id }
            )
        }
    }

}

