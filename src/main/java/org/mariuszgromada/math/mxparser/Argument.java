/*
 * @(#)Argument.java        4.4.0   2020-01-03
 *
 * You may use this software under the condition of "Simplified BSD License"
 *
 * Copyright 2010-2020 MARIUSZ GROMADA. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY <MARIUSZ GROMADA> ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of MARIUSZ GROMADA.
 *
 * If you have any questions/bugs feel free to contact:
 *
 *     Mariusz Gromada
 *     mariuszgromada.org@gmail.com
 *     http://mathparser.org
 *     http://mathspace.pl
 *     http://janetsudoku.mariuszgromada.org
 *     http://github.com/mariuszgromada/MathParser.org-mXparser
 *     http://mariuszgromada.github.io/MathParser.org-mXparser
 *     http://mxparser.sourceforge.net
 *     http://bitbucket.org/mariuszgromada/mxparser
 *     http://mxparser.codeplex.com
 *     http://github.com/mariuszgromada/Janet-Sudoku
 *     http://janetsudoku.codeplex.com
 *     http://sourceforge.net/projects/janetsudoku
 *     http://bitbucket.org/mariuszgromada/janet-sudoku
 *     http://github.com/mariuszgromada/MathParser.org-mXparser
 *     http://scalarmath.org/
 *     https://play.google.com/store/apps/details?id=org.mathparser.scalar.lite
 *     https://play.google.com/store/apps/details?id=org.mathparser.scalar.pro
 *
 *                              Asked if he believes in one God, a mathematician answered:
 *                              "Yes, up to isomorphism."
 */
package org.mariuszgromada.math.mxparser;

import org.mariuszgromada.math.mxparser.parsertokens.ParserSymbol;
/**
 * Argument class enables to declare the argument
 * (variable) which can be used in further processing
 * (in expressions, functions and dependent / recursive arguments).
 * <br><br>
 *
 * For example:
 * <ul>
 * <li>'x' - argument in expression 'sin(x)'
 * <li>'x' and 'y' - arguments in expression 'sin(x)+cos(y)'.
 * <li> 'x=2*t' - dependent argument (dependent from 't') in expression 'cos(x)'
 * </ul>
 * <p>
 * Using Argument class you can define two argument types:
 * <ul>
 * <li><b>free argument</b> - when value of argument 'x' is directly given
 * by a number (for example 'x=5')
 * <li><b>dependent argument</b> - when value of argument 'x' is given by
 * expression (for example: 'x=2*a+b' - argument 'x' depends from
 * argument/constant 'a' and argument/constant 'b' or any other
 * possible option like function, etc...)
 * </ul>
 * <p>
 * When creating an argument you should avoid names reserved as
 * parser keywords, in general words known in mathematical language
 * as function names, operators (for example:
 * sin, cos, +, -, etc...). Please be informed that after associating
 * the argument with the expression, function or dependent/recursive argument
 * its name will be recognized by the parser as reserved key word.
 * It means that it could not be the same as any other key word known
 * by the parser for this particular expression. Parser is case sensitive.
 *
 *
 * @author         <b>Mariusz Gromada</b><br>
 *                 <a href="mailto:mariuszgromada.org@gmail.com">mariuszgromada.org@gmail.com</a><br>
 *                 <a href="http://mathspace.pl" target="_blank">MathSpace.pl</a><br>
 *                 <a href="http://mathparser.org" target="_blank">MathParser.org - mXparser project page</a><br>
 *                 <a href="http://github.com/mariuszgromada/MathParser.org-mXparser" target="_blank">mXparser on GitHub</a><br>
 *                 <a href="http://mxparser.sourceforge.net" target="_blank">mXparser on SourceForge</a><br>
 *                 <a href="http://bitbucket.org/mariuszgromada/mxparser" target="_blank">mXparser on Bitbucket</a><br>
 *                 <a href="http://mxparser.codeplex.com" target="_blank">mXparser on CodePlex</a><br>
 *                 <a href="http://janetsudoku.mariuszgromada.org" target="_blank">Janet Sudoku - project web page</a><br>
 *                 <a href="http://github.com/mariuszgromada/Janet-Sudoku" target="_blank">Janet Sudoku on GitHub</a><br>
 *                 <a href="http://janetsudoku.codeplex.com" target="_blank">Janet Sudoku on CodePlex</a><br>
 *                 <a href="http://sourceforge.net/projects/janetsudoku" target="_blank">Janet Sudoku on SourceForge</a><br>
 *                 <a href="http://bitbucket.org/mariuszgromada/janet-sudoku" target="_blank">Janet Sudoku on BitBucket</a><br>
 *                 <a href="https://play.google.com/store/apps/details?id=org.mathparser.scalar.lite" target="_blank">Scalar Free</a><br>
 *                 <a href="https://play.google.com/store/apps/details?id=org.mathparser.scalar.pro" target="_blank">Scalar Pro</a><br>
 *                 <a href="http://scalarmath.org/" target="_blank">ScalarMath.org</a><br>
 *
 * @version        4.4.0
 */
public class Argument extends PrimitiveElement {
    /**
     * Syntax error in the dependent argument definition.
     */
    public static final boolean SYNTAX_ERROR_OR_STATUS_UNKNOWN = Expression.SYNTAX_ERROR_OR_STATUS_UNKNOWN;
    /**
     * Double.NaN as initial value of the argument.
     */
    public static final double ARGUMENT_INITIAL_VALUE = Double.NaN;
    /**
     * When argument was not not found
     */
    public static final int NOT_FOUND = Expression.NOT_FOUND;
    /**
     * Type indicator for free argument.
     */
    public static final int FREE_ARGUMENT = 1;
    /**
     * Type indicator for dependent argument.
     */
    public static final int DEPENDENT_ARGUMENT = 2;
    /**
     * Type indicator for recursive argument.
     */
    public static final int RECURSIVE_ARGUMENT = 3;
    /**
     * Argument type id for the definition of key words
     * known by the parser.
     */
    public static final int TYPE_ID		= 101;
    public static final String TYPE_DESC	= "User defined argument";
    /**
     * Argument with body based on the value or expression string.
     *
     * @see Argument#getArgumentBodyType()
     */
    public static final int BODY_RUNTIME = 1;


    public static final int BODY_EXTENDED = 2;
    /**
     * Argument body type.
     *
     * @see Argument#BODY_RUNTIME
     * @see Argument#BODY_EXTENDED
     * @see Argument#getArgumentBodyType()
     */
    private int argumentBodyType;
    /**
     * Argument extension (body based in code)
     *
     * @see ArgumentExtension
     */
    private ArgumentExtension argumentExtension;
    /**
     * Description of the argument.
     */
    private String description;
    /**
     * Argument expression for dependent and recursive
     * arguments.
     */
    Expression argumentExpression;
    /**
     * Argument name (x, y, arg1, my_argument, etc...)
     */
    private String argumentName;
    /**
     * Argument type (free, dependent)
     */
    int argumentType;
    /**
     * Argument value (for free arguments).
     */
    double argumentValue;
    /**
     * Index argument.
     *
     * @see RecursiveArgument
     */
    protected Argument n;
    /*=================================================
     *
     * Constructors
     *
     *=================================================
     */
    /**
     * Default constructor - creates argument based on the argument definition string.
     *
     * @param      argumentDefinitionString        Argument definition string, i.e.:
     *                                             <ul>
     *                                                <li>'x' - only argument name
     *                                                <li>'x=5' - argument name and argument value
     *                                                <li>'x=2*5' - argument name and argument value given as simple expression
     *                                                <li>'x=2*y' - argument name and argument expression (dependent argument 'x' on argument 'y')
     *                                             </ul>
     *
     * @param      elements   Optional parameters (comma separated) such as Arguments, Constants, Functions
     */
    public Argument(String argumentDefinitionString, PrimitiveElement...elements) {
        super(Argument.TYPE_ID);
        if ( mXparser.regexMatch(argumentDefinitionString, ParserSymbol.nameOnlyTokenRegExp) ) {
            argumentName = argumentDefinitionString;
            argumentValue = ARGUMENT_INITIAL_VALUE;
            argumentType = FREE_ARGUMENT;
            argumentExpression = new Expression(elements);
        } else if ( mXparser.regexMatch(argumentDefinitionString, ParserSymbol.constArgDefStrRegExp) ) {
            HeadEqBody headEqBody = new HeadEqBody(argumentDefinitionString);
            argumentName = headEqBody.headTokens.get(0).tokenStr;
            Expression bodyExpr = new Expression(headEqBody.bodyStr);
            double bodyValue = bodyExpr.calculate();
            if ( (bodyExpr.getSyntaxStatus() == Expression.NO_SYNTAX_ERRORS) && (bodyValue != Double.NaN) ) {
                argumentExpression = new Expression();
                argumentValue = bodyValue;
                argumentType = FREE_ARGUMENT;
            } else {
                argumentExpression = bodyExpr;
                addDefinitions(elements);
                argumentType = DEPENDENT_ARGUMENT;
            }
        } else if ( mXparser.regexMatch(argumentDefinitionString, ParserSymbol.functionDefStrRegExp) ) {
            HeadEqBody headEqBody = new HeadEqBody(argumentDefinitionString);
            argumentName = headEqBody.headTokens.get(0).tokenStr;
            argumentExpression = new Expression(headEqBody.bodyStr, elements);
            argumentExpression.setDescription(headEqBody.headStr);
            argumentValue = ARGUMENT_INITIAL_VALUE;
            argumentType = DEPENDENT_ARGUMENT;
            n = new Argument(headEqBody.headTokens.get(2).tokenStr);
        } else {
            argumentValue = ARGUMENT_INITIAL_VALUE;
            argumentType = FREE_ARGUMENT;
            argumentExpression = new Expression();
            argumentExpression.setSyntaxStatus(SYNTAX_ERROR_OR_STATUS_UNKNOWN, "[" + argumentDefinitionString + "] " + "Invalid argument definition (patterns: 'x', 'x=5', 'x=5+3/2', 'x=2*y').");
        }
        argumentBodyType = BODY_RUNTIME;
        setSilentMode();
        description = "";
    }

    /**
     * Constructor - creates free argument.
     *
     * @param      argumentName   the argument name
     * @param      argumentValue  the argument value
     */
    public Argument(String argumentName, double argumentValue) {
        super(Argument.TYPE_ID);
        argumentExpression = new Expression();
        if ( mXparser.regexMatch(argumentName, ParserSymbol.nameOnlyTokenRegExp) ) {
            this.argumentName=new String(argumentName);
            this.argumentValue=argumentValue;
            argumentType = FREE_ARGUMENT;
        } else {
            this.argumentValue = ARGUMENT_INITIAL_VALUE;
            argumentExpression.setSyntaxStatus(SYNTAX_ERROR_OR_STATUS_UNKNOWN, "[" + argumentName + "] " + "Invalid argument name, pattern not match: " + ParserSymbol.nameOnlyTokenRegExp);
        }
        argumentBodyType = BODY_RUNTIME;
        setSilentMode();
        description = "";
    }

    /**
     * Constructor - creates dependent argument(with hidden
     * argument expression).
     *
     * @param      argumentName                  the argument name
     * @param      argumentExpressionString      the argument expression string
     * @param      elements                      Optional parameters (comma separated)
     *                                           such as Arguments, Constants, Functions
     *
     * @see        Expression
     * @see        PrimitiveElement
     */
    public Argument(String argumentName, String argumentExpressionString, PrimitiveElement... elements) {
        super(Argument.TYPE_ID);
        if ( mXparser.regexMatch(argumentName, ParserSymbol.nameOnlyTokenRegExp) ) {
            this.argumentName=new String(argumentName);
            argumentValue=ARGUMENT_INITIAL_VALUE;
            argumentExpression = new Expression(argumentExpressionString, elements);
            argumentExpression.setDescription(argumentName);
            argumentType = DEPENDENT_ARGUMENT;
        } else {
            this.argumentValue = ARGUMENT_INITIAL_VALUE;
            argumentExpression = new Expression();
            argumentExpression.setSyntaxStatus(SYNTAX_ERROR_OR_STATUS_UNKNOWN, "[" + argumentName + "] " + "Invalid argument name, pattern not match: " + ParserSymbol.nameOnlyTokenRegExp);
        }
        argumentBodyType = BODY_RUNTIME;
        setSilentMode();
        description = "";
    }

    /**
     * Gets argument description.
     *
     * @return     The argument description string.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Enables argument verbose mode
     */
    public void setVerboseMode() {
        argumentExpression.setVerboseMode();
    }
    /**
     * Disables argument verbose mode (sets default silent mode)
     */
    public void setSilentMode() {
        argumentExpression.setSilentMode();
    }
    /**
     * Returns verbose mode status
     *
     * @return     true if verbose mode is on,
     *             otherwise returns false.
     */
    public boolean getVerboseMode() {
        return argumentExpression.getVerboseMode();
    }

    /**
     * Gets argument name
     *
     * @return     the argument name as string
     */
    public String getArgumentName() {
        return argumentName;
    }

    /**
     * Gets argument type
     *
     * @return     Argument type: Argument.FREE_ARGUMENT,
     *                            Argument.DEPENDENT_ARGUMENT,
     *                            Argument.RECURSIVE_ARGUMENT
     */
    public int getArgumentType() {
        return argumentType;
    }
    /**
     * Sets argument value, if DEPENDENT_ARGUMENT then argument type
     * is set to FREE_ARGUMENT.
     * If BODY_EXTENDED argument the BODY_RUNTIME argument is set.
     *
     * @param  argumentValue       the value of argument
     */
    public void setArgumentValue(double argumentValue) {
        if (argumentType == DEPENDENT_ARGUMENT) {
            argumentType = FREE_ARGUMENT;
            argumentExpression.setExpressionString("");
        }
        argumentBodyType = BODY_RUNTIME;
        this.argumentValue = argumentValue;
    }
    /*=================================================
     *
     * Syntax checking and values calculation
     *
     *=================================================
     */
    /**
     * Returns argument body type: {@link Argument#BODY_RUNTIME} {@link Argument#BODY_EXTENDED}
     * @return Returns argument body type: {@link Argument#BODY_RUNTIME} {@link Argument#BODY_EXTENDED}
     */
    public int getArgumentBodyType() {
        return argumentBodyType;
    }

    /**
     * Gets argument value.
     *
     * @return     direct argument value for free argument,
     *             otherwise returns calculated argument value
     *             based on the argument expression.
     */
    public double getArgumentValue() {
        if (argumentBodyType == BODY_EXTENDED)
            return argumentExtension.getArgumentValue();
        if (argumentType == FREE_ARGUMENT)
            return argumentValue;
        else
            return argumentExpression.calculate();
    }
    /**
     * Adds user defined elements (such as: Arguments, Constants, Functions)
     * to the argument expressions.
     *
     * @param elements Elements list (variadic - comma separated) of types: Argument, Constant, Function
     *
     * @see PrimitiveElement
     */
    public void addDefinitions(PrimitiveElement... elements) {
        argumentExpression.addDefinitions(elements);
    }
    /*=================================================
     *
     * Arguments handling API (the same as in Expression)
     * (protected argument expression)
     *
     *=================================================
     */

    /*=================================================
     *
     * Constants handling API (the same as in Expression)
     * (protected argument expression)
     *
     *=================================================
     */

    /*=================================================
     *
     * Functions handling API (the same as in Expression)
     * (protected argument expression)
     *
     *=================================================
     */

    /*=================================================
     *
     * Related expressions handling
     *
     *=================================================
     */
    /**
     * Adds related expression to the argumentExpression
     *
     * @param      expression          the related expression
     * @see        Expression
     */
    void addRelatedExpression(Expression expression) {
        argumentExpression.addRelatedExpression(expression);
    }
    /**
     * Adds related expression form the argumentExpression
     *
     * @param      expression          related expression
     *
     * @see        Expression
     */
    void removeRelatedExpression(Expression expression) {
        argumentExpression.removeRelatedExpression(expression);
    }

    /**
     * Creates cloned object of the this argument.''
     *
     * @return     clone of the argument.
     */
    @Override
    public Argument clone() {
        Argument newArg = new Argument(this.argumentName);
        newArg.argumentExpression = this.argumentExpression;
        newArg.argumentType = this.argumentType;
        newArg.argumentBodyType = this.argumentBodyType;
        newArg.argumentValue = this.argumentValue;
        newArg.description = this.description;
        newArg.n = this.n;
        if (this.argumentExtension != null) newArg.argumentExtension = argumentExtension.clone();
        else newArg.argumentExtension = null;
        return newArg;
    }
}