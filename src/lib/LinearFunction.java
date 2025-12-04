package lib;

import java.math.BigInteger;

/**
 * Represents linear function f(x)=ax+b
 */
public record LinearFunction(BigInteger a, BigInteger b) {
    public static final LinearFunction IDENTITY = new LinearFunction(BigInteger.ONE, BigInteger.ZERO);

    public BigInteger apply(BigInteger x) {
        return a.multiply(x).add(b);
    }

    public BigInteger apply(BigInteger x, BigInteger modulo) {
        return apply(x).mod(modulo);
    }

    public LinearFunction compose(LinearFunction o, BigInteger modulo) {
        return new LinearFunction(a.multiply(o.a).mod(modulo), a.multiply(o.b).add(b).mod(modulo));
    }

    public LinearFunction repeat(BigInteger times, BigInteger modulo) {
        if (BigInteger.ZERO.equals(times)) {
            return IDENTITY;
        } else if (BigInteger.ZERO.equals(times.mod(BigInteger.TWO))) {
            LinearFunction linearFunction = this.compose(this, modulo);
            return linearFunction.repeat(times.divide(BigInteger.TWO), modulo);
        } else {
            LinearFunction linearFunction = repeat(times.subtract(BigInteger.ONE), modulo);
            return compose(linearFunction, modulo);
        }
    }
}
