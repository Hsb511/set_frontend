package com.team23.domain.usecase

import com.team23.domain.model.Card
import com.team23.domain.statemachine.GameState


class UpdateGameAfterSetFoundUseCase(
    private val containsAtLeastOneSetUseCase: ContainsAtLeastOneSetUseCase,
) {

    fun invoke(table: List<Card>, setFound: Set<Card>, deck: List<Card>): GameState {
        var updatedDeck = deck.drop(3)
        var newCards = deck.take(3)

        val (newTable, deckIsUnchanged) = replaceCardsInTable(
            table = table,
            setFound = setFound,
            deck = newCards
        )
        if (deckIsUnchanged) {
            updatedDeck = deck
        }

        var tableContainsNoSet = !containsAtLeastOneSetUseCase.invoke(newTable)

        // Dynamically add 3s until a set exists or the deck is empty (enables 15->18, etc.)
        while (tableContainsNoSet && updatedDeck.isNotEmpty()) {
            newCards = updatedDeck.take(3)
            updatedDeck = updatedDeck.drop(3)
            newTable.addAll(newCards)
            tableContainsNoSet = !containsAtLeastOneSetUseCase.invoke(newTable)
        }

        return if (updatedDeck.isEmpty() && tableContainsNoSet) {
            GameState.Finished(cards = newTable)
        } else {
            GameState.Playing(deck = updatedDeck, table = newTable)
        }
    }

    fun replaceCardsInTable(
        table: List<Card>,
        setFound: Set<Card>,
        deck: List<Card>
    ): Pair<MutableList<Card>, Boolean> {
        val newTable = table.toMutableList()

        return if (table.size > 12) {
            val hypotheticalNewTable = table.toMutableList()
            hypotheticalNewTable.removeAll(setFound)

            if (containsAtLeastOneSetUseCase.invoke(hypotheticalNewTable)) {
                // 15 -> 12: move only the last 3 into the holes, then drop the tail (do not consume deck)
                setFound.forEach { card ->
                    val idx = newTable.indexOf(card)
                    newTable[idx] = Card.Empty
                }
                table.takeLast(3).filterIsInstance<Card.Data>().forEach { cardToMove ->
                    val idx = newTable.indexOfFirst { it is Card.Empty }
                    newTable[idx] = cardToMove
                }
                newTable.dropLast(3).toMutableList() to true
            } else {
                // 15 -> 15: replace in place using up to 3 cards from the deck (or Empty if missing)
                val cardsToAdd = if (deck.isEmpty()) MutableList(3) { Card.Empty } else deck.toMutableList()
                setFound.forEach { cardToRemove ->
                    val idx = newTable.indexOf(cardToRemove)
                    if (idx >= 0 && cardsToAdd.isNotEmpty()) {
                        newTable[idx] = cardsToAdd.removeAt(0)
                    }
                }
                newTable.toMutableList() to false
            }
        } else {
            // 12-card table: replace in place with deck's top 3, or with Empty if deck is empty
            val cardsToAdd = if (deck.isEmpty()) MutableList(3) { Card.Empty } else deck.toMutableList()
            setFound.forEach { cardToRemove ->
                val idx = newTable.indexOf(cardToRemove)
                if (idx >= 0 && cardsToAdd.isNotEmpty()) {
                    newTable[idx] = cardsToAdd.removeAt(0)
                }
            }
            newTable.toMutableList() to false
        }
    }
}
