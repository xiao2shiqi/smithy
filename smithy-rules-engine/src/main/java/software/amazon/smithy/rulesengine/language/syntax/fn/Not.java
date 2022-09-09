/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.smithy.rulesengine.language.syntax.fn;

import static software.amazon.smithy.rulesengine.language.error.RuleError.ctx;

import software.amazon.smithy.rulesengine.language.eval.Scope;
import software.amazon.smithy.rulesengine.language.eval.Type;
import software.amazon.smithy.rulesengine.language.syntax.expr.Expr;
import software.amazon.smithy.rulesengine.language.visit.ExprVisitor;
import software.amazon.smithy.utils.SmithyUnstableApi;

@SmithyUnstableApi
public final class Not extends SingleArgFn<Type.Bool> {

    public static final String ID = "not";

    public Not(FnNode fnNode) {
        super(fnNode, Type.bool());
    }

    public static Not ofExpr(Expr expr) {
        return new Not(FnNode.ofExprs(ID, expr));
    }

    public static Not ofExprs(Expr expr) {
        return new Not(FnNode.ofExprs(ID, expr));
    }

    public Expr target() {
        return expectOneArg();
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitNot(target());
    }

    @Override
    public Type typecheckLocal(Scope<Type> scope) {
        // Not must be typechecked in a interior scope because information doesn't flow back out of `not`
        return scope.inScope(() -> ctx("while typechecking `not`", this,
                () -> expectOneArg().typecheck(scope).expectBool()));
    }

    @Override
    protected Type typecheckArg(Scope<Type> scope, Type.Bool arg) {
        return null;
    }
}
