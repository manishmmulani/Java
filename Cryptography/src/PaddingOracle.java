import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class PaddingOracle {

    public String decodeBlock(String block) {
        //Integer val = Integer.decode("0x" + block);
        //Integer.
        // Each block is 16 bytes.
        // Take each byte and xor with 16
        // the result will be decoded text in hexadecimal.
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i+1 < block.length(); i+=2) {
            int hexVal = Integer.parseInt(block.substring(i, i+2), 16);
            //System.out.println(Integer.toHexString(hexVal ^ 16));
            //builder.append((char) (hexVal ^ 16));
            String hex = Integer.toHexString(hexVal ^ 16);
            builder.append((hex.length() == 1 ? "0" : "") + hex);
        }
        return builder.toString();
    }

    public void get404s() throws IOException
    {
        File file = new File("data/cbc.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while( (line = reader.readLine()) != null) {
            if (line.endsWith("404")) {
                System.out.println(line);
            }
        }
        reader.close();
    }

    public void run() {
        String IV = "cac544d7942e50e1a0afa156c803d115";
        StreamCipher cipher = new StreamCipher();
        String hex = decodeBlock("dce6acb565dd951c642b9feeebcdffc9");
        System.out.println(cipher.decode(cipher.xor(IV, hex)));
        hex = decodeBlock("afa631b5b91c019dd9dcd464e333b164");
        System.out.println(cipher.decode(cipher.xor(IV, hex)));
        IV = "084b0199778f14767cbdc989872a1f7d";
        hex = decodeBlock("6b2866e615fb39441cccbdfdfe54684d");
        System.out.println(cipher.decode(cipher.xor(IV, hex)));
        IV = "a59da498c81017fd2adc534610b412e4";
        hex = decodeBlock("daffd5ebb46574cd5bbe267664c56c93");
        System.out.println(cipher.decode(cipher.xor(IV, hex)));
        IV = "8f50d05513a440425f5ca434e5cb29c6";
        hex = decodeBlock("fa32af307095725b4645bd2dfcd230df");
        System.out.println(cipher.decode(cipher.xor(IV, hex)));
    }
    
    public static void main(String s[]) throws IOException
    {
        new PaddingOracle().run();
        //new PaddingOracle().get404s();
        // Five 16 byte blocks are there
        //dce6acb565dd951c642b9feeebcdffc9
        //afa631b5b91c019dd9dcd464e333b164
        //6b2866e615fb39441cccbdfdfe54684d
        //daffd5ebb46574cd5bbe267664c56c93
        //fa32af307095725b4645bd2dfcd230df
    }
}

// First byte =  (d8 xor 1) d9 = 1101 1001
// Second byte = (ed xor 2) ef = 1110 1111
// Third byte  = (de xor 3) dd