package com.example.recipes

class Meal(private val name: String, private val recipe: String) {
    companion object {
        val meals = arrayOf(
            Meal("Sałatka grecka", "Składniki: \n 2 pomidory \n 1 ogórek \n 1 czerwona cebula \n 1 papryka czerwona \n 1 papryka zielona \n 200g ser feta \n 20 oliwek czarnych \n sól \n pieprz \n 2 łyżki oliwy \n \n Sposób przygotowania: \n Warzywa pokroić w kostkę, ser feta pokruszyć. Wszystkie składniki wymieszać, doprawić solą, pieprzem i oliwą."),
            Meal("Spaghetti Bolognese", "Składniki: \n 300 g spaghetti \n 400 g mielonego mięsa wołowego \n 1 cebula \n 2 ząbki czosnku \n 2 łyżki oleju \n 2 łyżki koncentratu pomidorowego \n 1 puszka pomidorów krojonych \n sól \n pieprz \n \n Sposób przygotowania: \n Spaghetti ugotować w osolonej wodzie według wskazówek na opakowaniu. Na patelni rozgrzać olej i dodać pokrojoną w kostkę cebulę i czosnek. Smażyć na złoty kolor. Dodać mięso i smażyć, aż się zrumieni. Dodać koncentrat pomidorowy i pomidory krojone. Doprawić solą i pieprzem. Gotować na małym ogniu około 20 minut. Podawać z ugotowanymi spaghetti."),
            Meal("Tortilla z kurczakiem i warzywami", "Składniki: \n 2 filety z kurczaka \n 1 cebula \n 1 czerwona papryka \n 1 zielona papryka \n 4 jajka \n sól \n pieprz \n olej do smażenia \n \n Sposób przygotowania: \n Filety z kurczaka pokroić w kostkę, cebulę i papryki w paski. Na patelni rozgrzać olej i dodać kurczaka oraz cebulę. Smażyć na złoty kolor. Dodać papryki i smażyć razem. Jajka roztrzepać, doprawić solą i pieprzem. Dodać na patelnię i smażyć, aż jajka zetną się. Podawać z tortillą."),
            Meal("Ratatouille", "Składniki: \n 2 bakłażany \n 2 cukinie \n 2 papryki \n 4 pomidory \n 2 cebule \n 2 ząbki czosnku \n 2 łyżki oliwy \n sól \n pieprz \n zioła prowansalskie \n \n Sposób przygotowania: \n Bakłażany i cukinie pokroić w plastry, papryki w paski, cebulę i czosnek posiekać. Pomidory sparzyć, obrać ze skórki i pokroić w kostkę. Wszystko razem wymieszać. Przełożyć do naczynia żaroodpornego i doprawić solą, pieprzem oraz ziołami prowansalskimi. Piec w piekarniku nagrzanym do 180 stopni przez około 45 minut. Podawać jako samodzielne danie lub jako dodatek do mięsa lub ryżu."),
            Meal("Kotlet schabowy", "Składniki: \n 4 kotlety schabowe \n 2 jajka \n 2 łyżki mąki pszennej \n 1 szklanka bułki tartej \n sól \n pieprz \n olej do smażenia \n \n Sposób przygotowania: \n Kotlety rozbić, posolić, popieprzyć. W jednej misce ubić jajka, w drugiej wymieszać mąkę i bułkę tartą. Kotlety obtoczyć w jajku, następnie w mieszaninie mąki i bułki tartej. Smażyć na oleju z obu stron, aż będą rumiane.")
        )
    }

    fun getRecipe(): String {
        return recipe
    }

    fun getName(): String {
        return name
    }

    override fun toString(): String {
        return name
    }
}