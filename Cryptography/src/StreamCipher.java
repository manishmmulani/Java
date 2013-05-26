import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StreamCipher {

    public static void main(String s[])
    {
        new StreamCipher().runSamples();
        //new StreamCipher().test();
    }

    public String decode(String hex)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i+1 < hex.length(); i+=2) {
            int hexVal = Integer.parseInt(hex.substring(i, i+2), 16);
            builder.append((char) hexVal);
        }
        return builder.toString();
    }

    public String xor(String hex1, String hex2)
    {
        StringBuilder builder = new StringBuilder();
        String longerHex = null, shorterHex = null;
        if (hex1.length() > hex2.length()) {
            longerHex  = hex1;
            shorterHex = hex2;
        }
        else {
            shorterHex = hex1;
            longerHex  = hex2;
        }

        int i = 0;
        for (; i+1 < shorterHex.length(); i+=2)
        {
            int hex1Val = Integer.parseInt(shorterHex.substring(i, i+2), 16);
            int hex2Val = Integer.parseInt(longerHex.substring(i, i+2), 16);
            int xorVal  = hex1Val ^ hex2Val;
            String xorStr = Integer.toHexString(xorVal);
            if (xorStr.length() == 1) {
                xorStr = "0" + xorStr;
            }
            builder.append(xorStr);
        }

        builder.append(longerHex.substring(i));

        return builder.toString();
    }

    public void runSamples()
    {
        List<String> hexList = new ArrayList<String>();
        File file = new File("data/input.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                hexList.add(line);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        Map<String, boolean[]> spacePositionsMap = getSpacePositionsMap(hexList);
        String secretMsg = hexList.get(10);

        displaySpacePositions(secretMsg, spacePositionsMap.get(secretMsg));
        for (int i = 0 ; i < hexList.size() - 1; i++)
        {
            char[] solution = new char[secretMsg.length()/2];
            for (int k = 0; k < solution.length; k++) {
                solution[k] = '-';
            }
            String msg = hexList.get(i);
            boolean[] spacePositions = spacePositionsMap.get(msg);
            for (int j = 0; j < Math.min(secretMsg.length(), msg.length()); j+=2) {
                if (spacePositions[j]) {
                    String secMsg = secretMsg.substring(j, j+2);
                    String m = msg.substring(j, j+2);
                    String resultStr = decode(xor(secMsg, m));
                    char resultChar = resultStr.charAt(0);
                    if (resultChar >= 'a' && resultChar <= 'z' ||
                        resultChar >= 'A' && resultChar <= 'Z')
                    {
                        solution[j/2] = resultChar;
                    }
                }
            }
            System.out.println(solution);
            System.out.println("\n");
            
            // solved the puzzle.. now got the key.. some special chars remaining
            String key = "66396e89c9dbd8cc9874352acd6395102eafce78aa7fed28a07f6bc98d29c50b69b0339a19f8aa401a9c6d708f80c066c763fef0123148cdd8e802d05ba98777335daefcecd59c433a6b268b60bf4ef03c9a61";
            for (String s : hexList) {
                System.out.println(decode(xor(key, s)));
            }
            
            
        }
        /*
         * Algorithm :
         * Identify position of all spaces in each message (including the secret message)
         * XOR the secret message with each message (with known positions of spaces)
         * to get the case changed letter at those positions.
         * 
         * To identify position of all spaces in each message, choose a message greater in lenght
         * and xor to find a HEX value whose first byte starts with number > 3
         * 
         * A-Z  from 41 to 5A
         * a-z  from 61 to 7A
         */
    }

    private Map<String, boolean[]> getSpacePositionsMap(List<String> hexList)
    {
     // stores first char position in hex string that represents a space in original message
        Map<String , boolean[]> spacePositionsMap = new HashMap<String, boolean[]>();

        for (String hexMsg : hexList)
        {
            boolean[] spacePositions = new boolean[hexMsg.length()];
            spacePositionsMap.put(hexMsg, spacePositions);
            String otherHexMsg = null;
            for (String otherMsg : hexList) {
                // identify string with max length not equal to this
                if (hexMsg.equals(otherMsg)) {
                    continue;
                }
                if (otherMsg.length() > hexMsg.length()) {
                    otherHexMsg = otherMsg;
                    break;
                }

                if (otherHexMsg == null) {
                    otherHexMsg = otherMsg;
                }
                else if (otherMsg.length() > otherHexMsg.length()) {
                    otherHexMsg = otherMsg;
                }
            }

            String result = xor(hexMsg, otherHexMsg);
            for (int i = 0 ; i < hexMsg.length(); i+=2) {
                String hexResult = result.substring(i, i+2);
                String decodeStr = decode(hexResult);
                char t = decodeStr.charAt(0);
                if (t >= 'a' && t <= 'z' || t >= 'A' && t <= 'Z') {
                    spacePositions[i]=true;
                }
            }
        }
        
        return spacePositionsMap;
    }
    public String convertToLower(String hexStr)
    {
        StringBuilder lower = new StringBuilder();
        for (int i = 0; i < hexStr.length(); i+=2) {
            int hexVal = Integer.parseInt(hexStr.substring(i, i+2), 16);
            String retVal = Integer.toHexString(hexVal | 32);
            if (retVal.length() == 1) {
                retVal = "0" + retVal;
            }
            lower.append(retVal);
        }
        return lower.toString();
    }

    private void displaySpacePositions(String hexMsg, boolean[] spacePositions) {
      for (int i = 0; i < hexMsg.length(); i++) {
        System.out.print(spacePositions[i]?1:0);
      }
      System.out.println();
    }

    private String strToHex(String a)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            int t = a.charAt(i);
            String hex = Integer.toHexString(t);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            builder.append(hex);
        }
        return builder.toString();
    }

    public void test()
    {
        int i = Integer.parseInt("61", 16);
        char c = (char)i;
        System.out.println(c);
        System.out.println("helloworld".substring(1, 3));
        File file = new File("data/input.txt");
        System.out.println(file.getAbsolutePath());
        System.out.println(Integer.toHexString(0));
        System.out.println(Integer.toHexString(66));
        String t = "The secret message is: When using a stream cipher, never use the key more than once";
        String hex = "32510ba9babebbbefd001547a810e67149caee11d945cd7fc81a05e9f85aac650e9052ba6a8cd8257bf14d13e6f0a803b54fde9e77472dbff89d71b57bddef121336cb85ccb8f3315f4b52e301d16e9f52f904";
        String key = xor(strToHex(t), hex);
        System.out.println("key :\n" + key);
    }
}
