package io.github.mmolosay.debounce

import io.github.mmolosay.debounce.rules.SingleReleaseCallRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

/*
 * Copyright 2023 Mikhail Malasai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class DebounceRuleSetProvider : RuleSetProvider {

    // used in ~/config/detekt/detekt.yml
    override val ruleSetId = "debounce-rules"

    override fun instance(config: Config): RuleSet =
        RuleSet(
            id = ruleSetId,
            rules = rules(config),
        )

    private fun rules(config: Config) =
        listOf<Rule>(
            SingleReleaseCallRule(config),
        )
}