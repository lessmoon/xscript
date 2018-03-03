struct Expression;
struct Variable:Expression;
struct Constant:Expression;
struct Vector:Expression;
struct Expression {
    def virtual Expression takeDerivative(Variable b);
    def Vector takeDerivativeVector(Variable[] b);
    def virtual Expression optimize() {
        return this;
    }
    def virtual Constant getValue() {return null;}
    @+
    def Expression add(Expression a);
    @*
    def Expression mult(Expression a);
    @/
    def Expression div(Expression a);
    @-
    def Expression sub(Expression a);
    @string
    def virtual string toString();
}
struct Variable : Expression {
    string name;
    Constant value;
    def this(string name) {
        this.name = name;
    }
    
    def override Expression takeDerivative(Variable b);
    def override string toString() {
        return this.name;
    }
    
    def void setValue(Constant value) {
        this.value = value;
    }
    
    def override Constant getValue() {
        return this.value;
    }
}
struct Vector : Expression {
    Expression[] expr;
    def this(Expression[] expr) {
        this.expr = expr;
    }
    def override Expression takeDerivative(Variable b) {
        auto result = new Vector(new Expression[sizeof this.expr]);
        for (int i = 0; i < sizeof this.expr; i++) {
            result.expr[i] = this.expr[i].takeDerivative(b);
        }
        return result;
    }
    def override Expression optimize() {
        for (int i = 0; i < sizeof this.expr; i++) {
            this.expr[i] = this.expr[i].optimize();
        }
        return this;
    }
    @string
    def override string toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        string d = "";
        for (int i = 0; i < sizeof this.expr; i++) {
            sb.append(d)
              .append(this.expr[i].toString());
            d = ", ";
        }
        sb.append("]");
        return sb.toString();
    }
}
def Vector Expression.takeDerivativeVector(Variable[] b) {
    Expression[] d = new Expression[sizeof b];
    for (int i = 0; i < sizeof b; i++) {
        d[i] = this.takeDerivative(b[i]);
    }
    return new Vector(d);
}
struct Constant : Expression {
    real value;
    
    def this(real value) {
        this.value = value;
    }

    def override Expression takeDerivative(Variable b);
    
    def override Constant getValue() {
        return this;
    }
    
    def override string toString() {
        return this.value;
    }
    
    def Constant _add(Constant a) {
        return new Constant(this.value + a.value);
    }
    def Constant _sub(Constant a) {
        return new Constant(this.value - a.value);
    }
    def Constant _mult(Constant a) {
        return new Constant(this.value * a.value);
    }
    def Constant _div(Constant a) {
        return new Constant(this.value / a.value);
    }
}
const Constant CONSTANT_ZERO = new Constant(0.0),
               CONSTANT_ONE  = new Constant(1.0);

def Expression Variable.takeDerivative(Variable b) {
    if (b == this || b.name == this.name) {
        return CONSTANT_ONE;
    } else {
        return CONSTANT_ZERO;
    }
}

def Expression Constant.takeDerivative(Variable b) {
    return CONSTANT_ZERO;
}

const Constant CONSTANT_E = new Constant(2.718281828459) {
                    def override string toString() {
                        return "e";
                    }
               };

struct BinaryExpression : Expression {
    Expression a;
    Expression b;

    def this(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }
}
struct Addition : BinaryExpression {
    def this(Expression a, Expression b) {
        super(a, b);
    }
    
    def override string toString() {
        return "(" + this.a.toString() + "+" + this.b.toString() + ")";
    }
    
    def override Expression takeDerivative(Variable v) {
        Expression a_d = this.a.takeDerivative(v);
        Expression b_d = this.b.takeDerivative(v);
        
        return a_d+b_d;
    }
    
    def override Expression optimize() {
        this.a = this.a.optimize();
        this.b = this.b.optimize();
        if (this.a == CONSTANT_ZERO) {
            return this.b;
        }
        if (this.b == CONSTANT_ZERO) {
            return this.a;
        }
        return this;
    }
}
struct Multiplication : BinaryExpression {
    def this(Expression a, Expression b) {
        super(a, b);
    }
    def override string toString() {
        return  "(" + this.a.toString() + "*" + this.b.toString() + ")";
    }
    def override Expression takeDerivative(Variable v) {
        Expression a_d = this.a.takeDerivative(v);
        Expression b_d = this.b.takeDerivative(v);
        return a_d*this.b+b_d*this.a;
    }
    
    def override Expression optimize() {
        this.a = this.a.optimize();
        this.b = this.b.optimize();
        if (this.a == CONSTANT_ZERO||this.b == CONSTANT_ZERO) {
            return CONSTANT_ZERO;
        }
        if (this.a == CONSTANT_ONE) {
            return this.b;
        }
        if (this.b == CONSTANT_ONE) {
            return this.a;
        }
        return this;
    }
}
struct Division : BinaryExpression {
    def this(Expression a, Expression b) {
        super(a, b);
    }
    def override string toString() {
        return "(" + this.a.toString() + "/" + this.b.toString() + ")";
    }
    def override Expression takeDerivative(Variable v) {
        Expression a_d = this.a.takeDerivative(v);
        Expression b_d = this.b.takeDerivative(v);
        return (a_d*this.b-b_d*this.a)/(this.b*this.b);
    }
    
    def override Expression optimize() {
        this.a = this.a.optimize();
        this.b = this.b.optimize();
        if (this.a == CONSTANT_ZERO) {
            return CONSTANT_ZERO;
        }
        if (this.b == CONSTANT_ONE) {
            return this.a;
        }
        if (this.a == this.b) {
            return CONSTANT_ONE;
        }
        return this;
    }
}
struct Subtraction : BinaryExpression {
    def this(Expression a, Expression b) {
        super(a, b);
    }
    def override string toString() {
        return "(" + this.a.toString() + "-" + this.b.toString() + ")";
    }
    def override Expression takeDerivative(Variable v) {
        Expression a_d = this.a.takeDerivative(v);
        Expression b_d = this.b.takeDerivative(v);
        return a_d-b_d;
    }
    def override Expression optimize();
}
def Expression Expression.add(Expression a) {
    return new Addition(this, a);
}
def Expression Expression.mult(Expression a){
    return new Multiplication(this, a);
}
def Expression Expression.div(Expression a){
    return new Division(this, a);
}
def Expression Expression.sub(Expression a){
    return new Subtraction(this, a);
}
struct UnaryExpression : Expression {
    Expression expr;
    def this(Expression expression) {
        this.expr = expression;
    }
}
struct Negative : UnaryExpression {
    def this(Expression expression) {
        super(expression);
    }
    
    def override string toString() {
        return "-("+this.expr.toString()+")";
    }
    
    def override Expression takeDerivative(Variable v) {
        Expression e_d = this.expr.takeDerivative(v);
        return new Negative(e_d);
    }
    
    def override Expression optimize() {
        if(this.expr == CONSTANT_ZERO) {
            return CONSTANT_ZERO;
        }
        return this;
    }
}
def Expression Subtraction.optimize() {
    this.a = this.a.optimize();
    this.b = this.b.optimize();
    if (this.a == CONSTANT_ZERO) {
        return new Negative(this.b);
    }
    if (this.b == CONSTANT_ZERO) {
        return this.a;
    }
    return this;
}

struct Power : UnaryExpression {
    Constant exponent;
    def this(Expression base, Constant exponent) {
        super(base);
        this.exponent = exponent;
    }
    def override string toString() {
        return "("+this.expr.toString() + "^" + this.exponent.toString() + ")";
    }
    def override Expression takeDerivative(Variable v) {
        Expression e_d = this.expr.takeDerivative(v);
        return this.exponent*e_d*new Power(this.expr, this.exponent._sub(CONSTANT_ONE));
    }
    def override Expression optimize() {
        this.expr = this.expr.optimize();
        if(this.expr == CONSTANT_ZERO) {
            return CONSTANT_ZERO;
        }
        if(this.expr == CONSTANT_ONE||this.exponent == CONSTANT_ZERO) {
            return CONSTANT_ONE;
        }
        if(this.exponent == CONSTANT_ONE) {
            return this.expr;
        }
        return this;
    }
}
struct Exponential : UnaryExpression {
    Constant base;
    def this(Constant base, Expression exponent) {
        super(exponent);
        this.base = base;
    }
    def override string toString() {
        return "(" + this.base.toString() + "^" + this.expr.toString() + ")";
;
    }
    def override Expression takeDerivative(Variable v);
    
    def override Expression optimize();
}
struct Logarithm : UnaryExpression {
    Constant base;
    def this(Constant base, Expression antilogarithm) {
        super(antilogarithm);
        this.base = base;
    }
    
    def override string toString() {
        if (this.base == CONSTANT_E) {
            return "ln(" + this.expr.toString() + ")";
        }
        return "log_("+this.base+")(" + this.expr.toString() + ")";
    }
    
    def override Expression takeDerivative(Variable v);
    
    def override Expression optimize() {
        this.expr = this.expr.optimize();
        if(this.expr == CONSTANT_ONE) {
            return CONSTANT_ZERO;
        }
        if(this.expr == this.base) {
            return CONSTANT_ONE;
        }
        if (this.expr instanceof Exponential) {
            auto tmp = (Exponential)this.expr;
            return (tmp.expr*new Logarithm(this.base, tmp.base)).optimize();
        }
        return this;
    }
}
struct SuperLogarithm : BinaryExpression {
    def this(Expression base, Expression antilogarithm) {
        super(base, antilogarithm);
    }
    
    def override string toString() {
        if (this.a == CONSTANT_E) {
            return "ln(" + this.b.toString() + ")";
        }
        return "log_("+this.a+")(" + this.b.toString() + ")";
    }
    def override Expression takeDerivative(Variable v) {
        return this.optimize().takeDerivative(v);
    }
    def override Expression optimize() {
        return (new Logarithm(CONSTANT_E, this.b)/new Logarithm(CONSTANT_E, this.a)).optimize();
    }
}
def Expression Exponential.optimize() {
    this.expr = this.expr.optimize();
    if(this.base == CONSTANT_ZERO) {
        return CONSTANT_ZERO;
    }
    if(this.base == CONSTANT_ONE||this.expr == CONSTANT_ZERO) {
        return CONSTANT_ONE;
    }
    if(this.expr == CONSTANT_ONE) {
        return this.base;
    }
    if (this.expr instanceof Logarithm) {
        auto tmp = (Logarithm)this.expr;
        return (tmp.expr*new Logarithm(this.base, tmp.base)).optimize();
    }
    
    return this;
}
struct SuperPower : BinaryExpression {
    def this(Expression a, Expression b) {
        super(a, b);
    }
    def override string toString() {
        return "(" + this.a.toString() + "^" + this.b.toString() + ")";
    }
    def override Expression takeDerivative(Variable v) {
        Expression a_d = this.a.takeDerivative(v);
        Expression b_d = this.b.takeDerivative(v);
        return this*(b_d*new Logarithm(CONSTANT_E, this.a) + a_d*this.b/this.a);
    }
    def override Expression optimize() {
        this.a = this.a.optimize();
        this.b = this.b.optimize();
        if (this.a instanceof Constant) {
            return new Exponential((Constant)this.a, this.b).optimize();
        }
        if (this.b instanceof Constant) {
            return new Power(this.a, (Constant)this.b).optimize();
        }
        return this;
    }
}
def Expression Exponential.takeDerivative(Variable v) {
    Expression e_d = this.expr.takeDerivative(v);
    return e_d*new Logarithm(CONSTANT_E, this.base) * this;
}
def Expression Logarithm.takeDerivative(Variable v) {
    Expression e_d = this.expr.takeDerivative(v);
    return e_d / (this.expr * new Logarithm(CONSTANT_E, this.base));
}
def Expression ln(Expression expr) {
    return new Logarithm(CONSTANT_E, expr);
}
def Expression exp(Expression expr) {
    return new Exponential(CONSTANT_E, expr);
}
if(true||_isMain_){
    Variable x = new Variable("x");
    Variable y1 = new Variable("y");

    auto y = new Vector(new Expression[]{new SuperPower(x*x+y1, x), x*y1});
    println("y(x, y) = " + y.optimize().toString());
    println("y[x, y]'(x, y) = " + y.optimize().takeDerivativeVector(new Variable[]{x, y1}).optimize().toString());
}