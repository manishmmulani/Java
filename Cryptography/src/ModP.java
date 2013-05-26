import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


public class ModP {

    public static void main(String s[])
    {
        //new ModP().run();
        System.out.println(new ModP().order(2, 35));
    }

    public int order(int x, int m)
    {
        for (int i = 1 ; i < 100; i++) {
            BigInteger mod = power(new BigInteger(x + ""), i, new BigInteger(m + ""));
            if (BigInteger.ONE.equals(mod)) {
                return i;
            }
        }
        return 0;
    }
    public void run()
    {
        BigInteger g = new BigInteger("11717829880366207009516117596335367088558084999998952205599979459063929499736583746670572176471460312928594829675428279466566527115212748467589894601965568");
        BigInteger p = new BigInteger("13407807929942597099574024998205846127479365820592393377723561443721764030073546976801874298166903427690031858186486050853753882811946569946433649006084171");
        BigInteger h = new BigInteger("3239475104050450443565264378728065788649097520952449527834792452971981976143292558073856937958553180532878928001494706097394108577585732452307673444020333");

        compute(g, h, p);
    }

    private void compute(BigInteger g, BigInteger h, BigInteger p)
    {
        int B = (1<<20);
        BigInteger gPowerB = power(g, B, p);

        Map<BigInteger, Integer> RHSMap = new HashMap<BigInteger, Integer>(B);
        BigInteger prod = BigInteger.ONE;
        for (int i = 0; i < B; i++) {
            RHSMap.put(prod = prod.multiply(gPowerB).mod(p), i);
        }

        System.out.println("Population of RHSMap is over : " + RHSMap.size());
        BigInteger gInverse = g.modInverse(p);
        System.out.println("GInverse : " + gInverse);
        BigInteger LHS = null;
        prod = BigInteger.ONE;
        for (int j = 0; j < B; j++) {
            LHS = h.multiply(prod = prod.multiply(gInverse).mod(p)).mod(p);
            Integer x0 = RHSMap.get(LHS);
            if (x0 != null) {
                System.out.println("x1 : " + j + "\nx0 : " + x0);
                break;
            }
        }
    }

    private BigInteger power(BigInteger a, int x, BigInteger p)
    {
        if (x==0) {
            return BigInteger.ONE;
        }
        if (x==1) {
            return a;
        }

        BigInteger by2 = power(a, (x>>1), p);
        BigInteger result = by2.multiply(by2).mod(p);

        if ((x&1)==1) {
            result = result.multiply(a).mod(p);
        }

        return result;
    }
}
