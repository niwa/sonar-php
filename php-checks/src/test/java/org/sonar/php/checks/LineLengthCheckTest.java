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

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.sonar.php.tree.visitors.PHPIssue;
import org.sonar.plugins.php.TestUtils;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.visitors.Issue;

import java.util.List;

public class LineLengthCheckTest {

  private LineLengthCheck check = new LineLengthCheck();

  @Test
  public void defaultValue() throws Exception {
    List<Issue> issues = ImmutableList.<Issue>of(
      new PHPIssue(check, "Split this 122 characters long line (which is greater than 120 authorized).").line(4));

    PHPCheckTest.check(check, TestUtils.getCheckFile("LineLengthCheck.php"), issues);
  }

  @Test
  public void custom() throws Exception {
    check.maximumLineLength = 30;
    List<Issue> issues = ImmutableList.<Issue>of(
      new PHPIssue(check, "Split this 122 characters long line (which is greater than 30 authorized).").line(4),
      new PHPIssue(check, "Split this 42 characters long line (which is greater than 30 authorized).").line(5));

    PHPCheckTest.check(check, TestUtils.getCheckFile("LineLengthCheck.php"), issues);
  }
}
