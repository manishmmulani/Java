import gnu.trove.map.hash.TLongIntHashMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class Collision {

    private static final int MAX_MESSAGES = (1<<26);
    private MessageDigest md;

    public Collision() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("SHA-256");
    }
    
    Map<File, BufferedWriter> writerMap = new HashMap<File, BufferedWriter>();
    Map<String, File> fileMap = new HashMap<String, File>();

    private void generateMap() throws IOException
    {
        TLongIntHashMap map = new TLongIntHashMap(1025);

        for (int i = 0 ; i < MAX_MESSAGES; i++) {
            if (i%1000000==0) {
                System.out.println("Reached : " + i);
            }
            long key = getL10bitSHA(intToByteArray(i));
            if (map.containsKey(key)) {
                addToFile(key, i);
            }
            else {
                map.put(key, i);
                if (map.size() > 1025) {
                    System.out.println("No. of files is beyond 1024...");
                    break;
                }
                createNewFile(key, i);
            }
        }
    }

    private void addToFile(long key, int message) throws IOException
    {
        File file = getFile(key);

        writeMsg(file, message + "");
    }

    private void createNewFile(long key, int message) throws IOException
    {
        File file = getFile(key);

        file.createNewFile();
        System.out.println("File got created successfully : " + file.getAbsolutePath());
        writeMsg(file, message + "");
    }

    private File getFile(long key) {
        String path = "data/collision/" + key + ".txt";
        File file = null;
        if (fileMap.containsKey(path)) {
            file = fileMap.get(path);
        }
        else {
            file = new File(path);
            fileMap.put(path, file);
        }
        return file;
    }

    private void writeMsg(File file, String message) throws IOException
    {
        BufferedWriter writer = writerMap.get(file);
        if (writer == null) {
            System.out.println("Creating new writer : " + file.getAbsolutePath());
            FileWriter fw = new FileWriter(file, true);
            writer = new BufferedWriter(fw);
            writerMap.put(file, writer);
        }
        writer.write(message + "\n");
    }

    private long getL10bitSHA(byte[] bytes)
    {
        byte[] hash = md.digest(bytes);
        
        //extract the 50 LSB bits of the hash and put it in intHash
        long intHash = (long)(hash[29] & 3);
        for (int t=31; t<32; t++) {
            intHash = (long)(intHash << 8);
            intHash += hash[t];
        }
        return intHash;
        
    }
    
    public synchronized long getL50bitSHA(byte[] bytes)
    {
        byte[] hash = md.digest(bytes);

        //extract the 50 LSB bits of the hash and put it in intHash
        long intHash = (long)(hash[25] & 3);
        for (int t=26; t<32; t++) {
            intHash = (long)(intHash << 8);
            intHash += hash[t];
        }
        return intHash;

    }

    public byte[] intToByteArray(int value) {
        //return ByteBuffer.allocate(4).putInt(value).array();
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public void run() throws NoSuchAlgorithmException, IOException
    {
        md = MessageDigest.getInstance("SHA-256");
        generateMap();
        for (Map.Entry<File, BufferedWriter> entry : writerMap.entrySet()) {
            BufferedWriter writer = entry.getValue();
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void main(String s[]) throws NoSuchAlgorithmException, IOException
    {
        new Collision().run();
    }
}
