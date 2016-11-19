import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kuryakov on 19-Nov-16.
 */
public class Runner {

    private static final String INPUT_FILE_NAME = "bytes.txt";
    private static final String OUTPUT1_FILE_NAME = "bytes1.txt";
    private static final String OUTPUT2_FILE_NAME = "bytes2.txt";
    private static final String OUTPUT3_FILE_NAME = "bytes3.txt";
    private static List<String> list;

    public static void main(String[] args) {
        list = new ArrayList<>();
        list = readListOfStrings(INPUT_FILE_NAME);

        List<Template> templates = new ArrayList<>();
        for (String s : list) {
            templates.add(parseTemplate(s));
        }
        //OUTPUT 1
        List<String> tempList = new LinkedList<>();
        for (int i = 0; i < templates.size(); i++) {
            tempList.add(templates.get(i).getName());
        }
        Collections.sort(tempList);
        writeToFile(OUTPUT1_FILE_NAME, tempList.iterator());
        //OUTPUT 2
        Comparator<Template> comparator = (o1, o2) -> {
            if (o1.getElementSize() < o2.getElementSize()) return -1;
            if (o1.getElementSize() > o2.getElementSize()) return 1;
            return 0;
        };
        Collections.sort(templates, comparator);
        tempList.clear();
        for (int i = 0; i < templates.size(); i++) {
            tempList.add(templates.get(i).getName());
        }
        writeToFile(OUTPUT2_FILE_NAME, tempList.iterator());
        //OUTPUT 3
        tempList.clear();
        for (Template t : templates) {
            tempList.add(String.valueOf(t.getSize()));
        }
        writeToFile(OUTPUT3_FILE_NAME, tempList.iterator());
    }

    private static Template parseTemplate(String s) {
        return new Template(s.substring(s.indexOf(' '), s.indexOf('[')), typeLength(s), calculateWeightOfString(s));
    }

    private static void writeToFile(String filename, Iterator<?> iterator) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(filename));
            while (iterator.hasNext()) {
                fw.write((String) iterator.next());
                fw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException | NullPointerException ex) {
                System.out.println("Stream didn't closed or not exists. Reading from file failed. ");
            }
        }
    }

    private static int typeLength(String s) {
        switch (s) {
            case "int":
                return 4;
            case "long":
                return 8;
            case "byte":
                return 1;
            case "double":
                return 8;
            case "char":
                return 2;
            case "boolean":
                return 1;
            case "short":
                return 2;
            case "float":
                return 4;
        }
        return 4;
    }

    private static List<String> readListOfStrings(String path) {
        BufferedReader br = null;
        List<String> listOfStrings = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(new File(path)));
            String tempString = br.readLine();
            while (tempString != null) {
                listOfStrings.add(tempString);
                tempString = br.readLine();
            }
        } catch (IOException ex) {
            System.out.println("Reading from file successfully failed. IOException");
        } finally {
            try {
                br.close();
            } catch (IOException | NullPointerException ex) {
                System.out.println("Stream didn't closed or not exists. Reading from file failed. ");
            }
        }
        return listOfStrings;
    }

    private static int calculateWeightOfString(String s) {
        int size = 0;
        String type = s.substring(0, s.indexOf(' '));

        Pattern pattern = Pattern.compile("\\[\\d+\\]");
        Matcher matcher = pattern.matcher(s);
        int a;
        List<Integer> list = new ArrayList<>();
        while (matcher.find()) {
            String tempString = matcher.group();
            a = Integer.parseInt(tempString.substring(1, tempString.length() - 1));
            list.add(a);
        }
        Collections.reverse(list);
        int tempSize = 1;
        boolean isFirst = true;
        for (Integer i : list) {
            int firstSizeWithType = tempSize * i;
            if (isFirst) {
                firstSizeWithType *= typeLength(type);
                isFirst = false;
            }
            tempSize = 24 + firstSizeWithType;
        }
        return tempSize;
    }
}