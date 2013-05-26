import gnu.trove.map.hash.TLongIntHashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class BirthdayAttack {

    public class CollisionFinder implements Callable<List<Integer>>
    {

        private File file;
        private Collision collision;
        public CollisionFinder(File file, Collision collision) {
            this.file = file;
            this.collision = collision;
        }

        private List<Integer> getMsgsFromFile()
        {
            List<Integer> resultList = new ArrayList<Integer>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = null;

                while( (line = reader.readLine()) != null) {
                    resultList.add(Integer.parseInt(line));
                }
            }
            catch(IOException e) {
                System.out.println("Exception while reading file " + file.getAbsolutePath());
            }
            return resultList;
        }

        @Override
        public List<Integer> call() throws Exception {
            List<Integer> messagesFromFile = getMsgsFromFile();
            TLongIntHashMap map = new TLongIntHashMap();

            List<Integer> result = new ArrayList<Integer>();
            for (int x : messagesFromFile) {
                long hashkey = collision.getL50bitSHA(collision.intToByteArray(x));
                if (map.containsKey(hashkey)) {
                    int y = map.get(hashkey);
                    result.add(y);
                    result.add(x);
                    return result;
                }
                else {
                    map.put(hashkey, x);
                }
            }
            return null;
        }
    }

    public void run() throws InterruptedException, ExecutionException, NoSuchAlgorithmException 
    {
        Collision coll = new Collision();

        ExecutorService taskExecutor = Executors.newFixedThreadPool(10);
        List<Future<List<Integer>>> resObjList = new ArrayList<Future<List<Integer>>>();
        File directory = new File("data/collision");

        if (directory.exists() && directory.isDirectory()) {
            File[] fileArray = directory.listFiles();
            if (fileArray != null) {
                for (File file : fileArray) {
                    CollisionFinder finder = new CollisionFinder(file, coll);
                    Future<List<Integer>> resObj = taskExecutor.submit(finder);
                    resObjList.add(resObj);
                    System.out.println("Submitted job for file : " + file.getAbsolutePath());
                }

                taskExecutor.shutdown();

                System.out.println("future objects list size : " + resObjList.size());

                for (Future<List<Integer>> resObj : resObjList) {
                    List<Integer> resList = resObj.get();
                    if (resList != null) {
                        System.out.println("collision found : " + resList);
                    }
                }
            }
        }
    }

    public void getHexOfCollisions() throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        Collision col = new Collision();

        int x = 40773199;
        int y = 45418854;
        System.out.println("x : " + Integer.toHexString(x) +
                    "\ny : " + Integer.toHexString(y));
        System.out.println(col.getL50bitSHA((col.intToByteArray(x))) + "\n" +
                col.getL50bitSHA((col.intToByteArray(y))));
        System.out.println(byteToStr(md.digest(col.intToByteArray(x))) + "\n" +
                byteToStr(md.digest(col.intToByteArray(y))));
    }

    private String byteToStr(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for(byte b : bytes) {
            builder.append((char)b);
        }
        return builder.toString();
    }
    public static void main(String s[]) throws Exception
    {
         //new BirthdayAttack().run();
        new BirthdayAttack().getHexOfCollisions();
    }
}
