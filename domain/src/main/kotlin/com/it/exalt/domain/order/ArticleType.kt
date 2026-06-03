package com.it.exalt.domain.order

/**
 * Classifies an article by its consumption type and determines the token cost category.
 * Token costs:
 *   NORMAL_ALCOHOLIC  -> 1 drink token
 *   PREMIUM_ALCOHOLIC -> 2 drink tokens
 *   MEAL              -> 3 food tokens
 *   SNACK             -> 1 food token
 */
enum class ArticleType { NORMAL_ALCOHOLIC, PREMIUM_ALCOHOLIC, MEAL, SNACK }
