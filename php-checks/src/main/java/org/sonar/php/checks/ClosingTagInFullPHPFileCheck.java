/*
 * SonarQube PHP Plugin
 * Copyright (C) 2010 SonarSource and Akram Ben Aissi
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.php.checks;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.php.checks.utils.CheckUtils;
import org.sonar.plugins.php.api.tree.CompilationUnitTree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;

@Rule(
  key = ClosingTagInFullPHPFileCheck.KEY,
  name = "Closing tag \"?>\" should be omitted on files containing only PHP",
  priority = Priority.MINOR,
  tags = {Tags.CONVENTION, Tags.PSR2})
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("2min")
public class ClosingTagInFullPHPFileCheck extends PHPVisitorCheck {

  public static final String KEY = "S1780";
  private static final String MESSAGE = "Remove this closing tag \"?>\".";

  private int inlineHTMLCounter = 0;
  private boolean isOnlyClosingTag = false;
  private SyntaxToken lastInlineHTMLToken = null;

  public void initScan() {
    inlineHTMLCounter = 0;
    isOnlyClosingTag = false;
    lastInlineHTMLToken = null;
  }

  @Override
  public void visitToken(SyntaxToken token) {
    if (token.is(Kind.INLINE_HTML_TOKEN)) {
      inlineHTMLCounter++;
      isOnlyClosingTag = CheckUtils.isClosingTag(token);
      lastInlineHTMLToken = token;
    }
  }

  @Override
  public void visitCompilationUnit(CompilationUnitTree tree) {
    initScan();

    super.visitCompilationUnit(tree);

    if (inlineHTMLCounter == 1 && isOnlyClosingTag) {
      context().newIssue(this, MESSAGE).tree(lastInlineHTMLToken);
    }
  }
}
