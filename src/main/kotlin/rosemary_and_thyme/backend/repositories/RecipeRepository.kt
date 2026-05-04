package rosemary_and_thyme.backend.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class RecipeRepository(private val jdbc: JdbcTemplate){

    fun getRecipe(id: UUID){
    }
}