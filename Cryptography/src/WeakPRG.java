import java.util.ArrayList;
import java.util.List;


public class WeakPRG
{
    public static void main(String s[])
    {
        //new WeakPRG().solve();
        new WeakPRG().generate(89059908, 164204369, 295075153);
    }

    public void solve()
    {
//        output #1: 210205973
//        output #2: 22795300
//        output #3: 58776750
//        output #4: 121262470
//        output #5: 264731963
//        output #6: 140842553
//        output #7: 242590528
//        output #8: 195244728
//        output #9: 86752752
        List<Integer> outputs = new ArrayList<Integer>();
        outputs.add(210205973);
        outputs.add(22795300);
        outputs.add(58776750);
        outputs.add(121262470);
        outputs.add(264731963);
        outputs.add(140842553);
        outputs.add(242590528);
        outputs.add(195244728);
        outputs.add(86752752);

        int p = 295075153;
        for (int x = 0; x <= p; x++)
        {
            int y = x^outputs.get(0);
            if (solve(x, y, p, 1, outputs))
            {
                System.out.println("x : " + x + "\ny : " + y);
            }
        }
    }
    public void generate(int x, int y, int p)
    {
        int outputsCnt = 11;
        System.out.println("Output#1 : " + (x^y));
        for (int i = 2; i < outputsCnt; i++)
        {
            x = (x + x + 5) % p;
            y = (y + y + y + 7) % p;
            System.out.println("Output#" + i + " : " + (x^y));
        }
    }

    public boolean solve(int x, int y, int p, int index, List<Integer> outputs)
    {
        if (index == outputs.size()) {
            return true;
        }

        int x1 = (x + x + 5) % p;
        int y1 = (y + y + y + 7) % p;
        if ((x1 ^ y1) == outputs.get(index))
        {
            return solve(x1, y1, p, index + 1, outputs);
        }
        return false;
    }
}
