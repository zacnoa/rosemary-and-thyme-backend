package rosemary_and_thyme.backend.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recipe/")
class RecipeController {


    @GetMapping
    fun test(): String = "vvvv"
}