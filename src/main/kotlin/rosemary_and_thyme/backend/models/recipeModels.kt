package rosemary_and_thyme.backend.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.util.Date
import java.util.UUID


data class HeroImage(
    val id: UUID,
    val index:Int,
    val url:String
)
data class InstructionImage(
    val id:UUID,
    val index:Int,
    val url:String
)

data class Ingredient(

    val id:UUID,
    val name:String,
    val index:Int,
    val amount: BigDecimal,
    @JsonProperty("measuring_unit")
    val measuringUnit:String
)
data class Instruction(
    val id: UUID,
    val index:Int,
    val instruction:String,
    val images:List<InstructionImage>
)
data class Recipe(

    val id: UUID,
    val userId:UUID,
    val userName: String,
    val createDate: Date,
    val name: String,
    val description: String,
    val likes: Int,
    val portions: Int,
    @JsonProperty("cook_time")
    val cookTime: String,
    val difficulty: Int,
    @JsonProperty("side_notes")
    val sideNotes: String,

    val ingredients:List<Ingredient>,
    val instructions:List<Instruction>,
    val heroImages:List<HeroImage>
)

data class RecipeImageResponse(
    val id:UUID,
    val url:String
)
data class IngredientResponse(
    val id:UUID,
    val name:String,
    val amount:BigDecimal,
    val measuringUnit:String,
)
data class InstructionResponse(
    val id: UUID,
    val text:String,
    val images:List<UUID>
)
data class RecipeResponse(


   val userId: UUID,
   val userName: String,
   val createDate: Date,
   val name: String,
   val description: String,
   val likes: Int,
   val portions: Int,
   val cookTime: String,
   val difficulty: Int,
   val sideNotes: String,
   val images: Map<UUID, RecipeImageResponse>,

   val ingredients: Map<UUID, Ingredient>,
   val instructions: Map<UUID, Instruction>,
   val ingredientsOrder: List<UUID>,
   val instructionsOrder: List<UUID>,
   val heroImages: List<UUID>,


)