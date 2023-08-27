package io.github.mmolosay.debounce.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

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

class SingleReleaseCallRule(config: Config) : Rule(config) {

    private val releaseMethodNames = listOf("release", "releaseIn")

    override val issue = Issue(
        id = javaClass.simpleName,
        severity = Severity.Defect,
        description = "Rule that detects multiple release calls on same DebounceReleaseScope instance",
        debt = Debt(mins = 1),
    )

    override fun visitLambdaExpression(lambdaExpression: KtLambdaExpression) {
        super.visitLambdaExpression(lambdaExpression)

        if (lambdaExpression.isArgumentOfDebounceFunctionCall().not()) return
        val releaseCalls = lambdaExpression.collectDescendantsOfType<KtCallExpression> {
            it.calleeExpression?.text in releaseMethodNames
        }

        if (releaseCalls.size < 2) return
        releaseCalls
            .drop(1)
            .forEach { releaseCall ->
                val finding = finding(releaseCall)
                report(finding)
            }
    }

    private fun KtLambdaExpression.isArgumentOfDebounceFunctionCall(): Boolean {
        val callExpression = getParentOfType<KtCallExpression>(strict = true) ?: return false
        val calleeExpression = callExpression.calleeExpression ?: return false
        if (calleeExpression !is KtNameReferenceExpression) return false
        return (calleeExpression.getReferencedName() == "debounce")
    }

    private fun finding(releaseCall: PsiElement): Finding =
        CodeSmell(
            issue = issue,
            entity = Entity.from(releaseCall),
            message = "Multiple 'release' methods inside debounce lambda",
        )
}