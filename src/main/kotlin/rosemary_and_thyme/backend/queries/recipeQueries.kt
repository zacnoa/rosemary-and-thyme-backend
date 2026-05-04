package  rosemary_and_thyme.backend.queries

enum class RECIPE_QUERIES(query: String) {
    FETCH_RECIPE(
        """
        SELECT 
            R.*,
            json_agg(DISTINCT jsonb_build_object(
                'id', I.id,
                'name', I.name,
                'index', I.index,
                'amount', I.amount,
                'measuring_unit', I.measuring_unit
            )) AS ingredients,
            json_agg(DISTINCT jsonb_build_object(
                'id', INST.id,
                'index', INST.index,
                'instruction', INST.instruction,
                'images', (
                    SELECT json_agg(jsonb_build_object(
                        'id', INST_IMG.id,
                        'index', INST_IMG.index,
                        'url', INST_IMG.url
                    ))
                    FROM instruction_images AS INST_IMG
                    WHERE INST_IMG.instruction_id = INST.id
                )
            )) AS instructions,
            json_agg(DISTINCT jsonb_build_object(
                'id', HERO_IMG.id,
                'index', HERO_IMG.index,
                'url', HERO_IMG.url
            )) AS hero_images
        FROM recipes AS R
        JOIN ingredients AS I ON I.recipe_id = R.id
        JOIN instructions AS INST ON INST.recipe_id = R.id
        JOIN hero_images AS HERO_IMG ON HERO_IMG.recipe_id = R.id
        WHERE R.id = ? 
        GROUP BY R.id
    """.trimIndent()
    )
}