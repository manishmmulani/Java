import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.google.common.math.BigIntegerMath;


public class Factorization
{

    public void compute(BigInteger N, BigInteger A, List<BigInteger> primes)
    {
        BigInteger A2MinusN = A.multiply(A).subtract(N);
        BigInteger x = BigIntegerMath.sqrt(A2MinusN, RoundingMode.DOWN);
        if (x.multiply(x).equals(A2MinusN)) {
            BigInteger p = A.subtract(x);
            BigInteger q = A.add(x);
            if (p.multiply(q).equals(N)) {
                primes.add(p);
                primes.add(q);
            }
        }
    }

    public void run3()
    {
        String Nstr =
            "72006226374735042527956443552558373833808445147399984182665305798191" +
            "63556901883377904234086641876639384851752649940178970835240791356868" +
            "77441155132015188279331812309091996246361896836573643119174094961348" +
            "52463970788523879939683923036467667022162701835329944324119217381272" +
            "9276147530748597302192751375739387929";

        BigInteger N = new BigInteger(Nstr);
        BigInteger SIX = new BigInteger("6");
        BigInteger FOUR = new BigInteger("4");
        BigInteger sqrt6NDown = BigIntegerMath.sqrt(SIX.multiply(N), RoundingMode.DOWN);
        BigInteger _2A = sqrt6NDown.add(sqrt6NDown).add(BigInteger.ONE);  // A = floor(sqrt(N)) + 0.5
        BigInteger _3pMinus2q = BigIntegerMath.sqrt(_2A.multiply(_2A).subtract(SIX.multiply(FOUR).multiply(N)), RoundingMode.UNNECESSARY);
        System.out.println("2A = 3p + 2q = " + _2A);
        System.out.println("|3p - 2q| = " + _3pMinus2q);
        BigInteger p = _2A.add(_3pMinus2q).divide(SIX);
        BigInteger q = _2A.subtract(_3pMinus2q).divide(FOUR);
        System.out.println("p = " + p);
        System.out.println("q = " + q);
        System.out.println("p.q == N : " + p.multiply(q).equals(N));
        
        q = _2A.add(_3pMinus2q).divide(FOUR);
        p = _2A.subtract(_3pMinus2q).divide(SIX);
        System.out.println("p = " + p);
        System.out.println("q = " + q);
        System.out.println("p.q == N : " + p.multiply(q).equals(N));
        
    }

    public void run2()
    {
        String Nstr =
            "6484558428080716696628242653467722787263437207069762630604390703787" +
            "9730861808111646271401527606141756919558732184025452065542490671989" +
            "2428844841839353281972988531310511738648965962582821502504990264452" +
            "1008852816733037111422964210278402893076574586452336833570778346897" +
            "15838646088239640236866252211790085787877";

        BigInteger N = new BigInteger(Nstr);
        BigInteger sqrtN = BigIntegerMath.sqrt(N, RoundingMode.UP);
        BigInteger max = new BigInteger(((1<<20) + "")).add(sqrtN);
        List<BigInteger> primes = new ArrayList<BigInteger>();
        for (BigInteger i = sqrtN; i.compareTo(max) <= 0; i=i.add(BigInteger.ONE))
        {
            compute(N, i, primes);
            if (! primes.isEmpty()) {
                break;
            }
        }
        System.out.println("problem 2 :- ");
        System.out.println("p : " + primes.get(0));
        System.out.println("q : " + primes.get(1));
        System.out.println("p.q == N : " + primes.get(0).multiply(primes.get(1)).equals(N));
    }

    public void run1()
    {
        String Nstr = 
        "17976931348623159077293051907890247336179769789423065727343008115" +
        "77326758055056206869853794492129829595855013875371640157101398586" +
        "47833778606925583497541085196591615128057575940752635007475935288" +
        "71082364994994077189561705436114947486504671101510156394068052754" +
        "0071584560878577663743040086340742855278549092581";

        BigInteger N = new BigInteger(Nstr);
        
        BigInteger A = BigIntegerMath.sqrt(N, RoundingMode.UP);
        BigInteger xd = BigIntegerMath.sqrt(A.multiply(A).subtract(N), RoundingMode.DOWN);
        BigInteger xu = BigIntegerMath.sqrt(A.multiply(A).subtract(N), RoundingMode.UP);
        BigInteger p = A.subtract(xd);
        BigInteger q = A.add(xd);

        System.out.println("A : " + A);
        System.out.println("xd : " + xd +"\nxu : " + xu + "\nxu - xd : " + xu.subtract(xd));
        System.out.println("A - x  : " +  A.subtract(xd));
        System.out.println("A + x  : " + A.add(xd));
        
        System.out.println("equals : " + N.equals(p.multiply(q)));
    }

    public static void main(String s[])
    {
        new Factorization().run3();
    }
}
