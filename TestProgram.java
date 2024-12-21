import java.io.BufferedReader;
 import java.io.FileReader;
 import java.io.IOException;
 import java.util.*;
 //Class my entry
 class MyEntry {
    private Integer key;
    private String value;
    public MyEntry(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
    public Integer getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        return key + " " + value;
    }
 }
 //Class SkipListPQ
 class SkipListPQ {
    private double alpha;
    private Random rand;
     //AGGIUNTE MIE
     private List<List<MyEntry>> sl;
     //private static List<MyEntry> empty_list;
     //FINE AGGIUNTE MIE
    public SkipListPQ(double alpha) {
        this.alpha = alpha;
        this.rand = new Random();
        this.sl = new ArrayList<>();//col fatto ch è un array che implementa una lista potrebbe dare problemi nell'insert
        List<MyEntry> empty_list = new ArrayList<MyEntry>();
        empty_list.add(new MyEntry(Integer.MIN_VALUE,"-inf"));
        empty_list.add(new MyEntry(Integer.MAX_VALUE,"+inf"));
        sl.add(empty_list);
 // TO BE COMPLETED       
    }
    public int size() {
        return sl.get(0).size() - 2;
 // TO BE COMPLETED (finito?)       
    }
    public MyEntry min() {
        if(this.size()!=0)return sl.get(0).get(1);
        return null;
 // TO BE COMPLETED (finito?) 
    }
    public int insert(int key, String value) {
 // TO BE COMPLETED 
    }
    private int generateEll(double alpha_ , int key) {
        int level = 0;
        if (alpha_ >= 0. && alpha_< 1) {
          while (rand.nextDouble() < alpha_) {
              level += 1;
          }
        }
        else{
          while (key != 0 && key % 2 == 0){
            key = key / 2;
            level += 1;
          }
        }
        return level;
    }
    public MyEntry removeMin() {
        MyEntry e = min();
        if(e==null) return e;
        int i = 0;
        boolean check = sl.get(i).remove(e);
        while(check)
        {
            i++;
            check = sl.get(i).remove(e);  
        } 
        //parte di pulizia
        while (sl.size() > 1 && sl.get(sl.size() - 2).size() == 2) sl.remove(sl.size() - 1);
        //fine pulizia
        return e;
 // TO BE COMPLETED (finito?) 
    }
    public void print() {
        int i = 1;
        String s = "";
        while(i < sl.get(0).size()-1)
        {   
            MyEntry e = sl.get(0).get(i);
            int j = 1;
            while((j < sl.size()) && sl.get(j).contains(e))
            {
                j++;
            } 
            s+=(e + " "+ j + ", ");
            i++;
        }
        s = s.substring(0,s.length()-2);
        //System.out.println(sl.get(0).size()-2); -> trovato è un problema di inserimento, guarda in insert
        System.out.println(s);
 // TO BE COMPLETED (finito?) 
    }
 }
 //TestProgram
 public class TestProgram {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TestProgram <file_path>");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String[] firstLine = br.readLine().split(" ");
            int N = Integer.parseInt(firstLine[0]);
            double alpha = Double.parseDouble(firstLine[1]);
            System.out.println(N + " " + alpha);
            SkipListPQ skipList = new SkipListPQ(alpha);
            for (int i = 0; i < N; i++) {
                String[] line = br.readLine().split(" ");
                int operation = Integer.parseInt(line[0]);
                switch (operation) {
                    case 0:
                    MyEntry min_entry = skipList.min();
                        if(min_entry != null) System.out.println(min_entry);
 // TO BE COMPLETED (finito?)
                        break;
                    case 1:
                    skipList.removeMin();
 // TO BE COMPLETED (finito?)
                        break;
                    case 2:
                    int key = Integer.parseInt(line[1]);
                    String value = line[2];
                    skipList.insert(key, value);
// TO BE COMPLETED (finito?)
                        break;
                    case 3:
                    skipList.print();
 // TO BE COMPLETED 
                        break;
                    default:
                        System.out.println("Invalid operation code");
                        return;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
 }