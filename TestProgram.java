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
 class Node{
    private MyEntry e;
    Node above, below, prev, next;
    public Node(MyEntry e){
        this.e = e;
    }
    public int getKey(){
        return e.getKey();
    }
    public String getValue(){
        return e.getValue();
    }
    public void H_delete(){
        Node prev = this.prev;
        Node next = this.next;
        prev.next = next;
        next.prev = prev;
    } 
    public String toString(){
        return e.toString();
    }
 }
 //Class SkipListPQ
 class SkipListPQ {
    private double alpha;
    private Random rand;
    private Node s; // nodo di partenza (in alto a sx)
    private int height;
    private int n_entries; 
    public SkipListPQ(double alpha) {
        this.alpha = alpha;
        this.rand = new Random();
        this.height = 0;
        this.n_entries = 0;
        this.s = new Node(new MyEntry(Integer.MIN_VALUE, "-inf"));
        Node inf = new Node(new MyEntry(Integer.MAX_VALUE, "+inf"));
        s.next = inf;
        inf.prev = s;
        
         // TO BE COMPLETED       
    }
    public int size() {
        return n_entries;       
    }

    public MyEntry min() {
        if(n_entries == 0) return null;
        Node temp_node = s;
        while(temp_node.below != null) temp_node = temp_node.below;
        temp_node = temp_node.next;
        return new MyEntry(temp_node.getKey(),temp_node.getValue());
    }

    public int insert(int key, String value) {
        int traversed_nodes = 0;
        Node to_be_inserted = new Node(new MyEntry(key,value));
        Node temp_node = s;
        int level = generateEll(alpha, key);
        if(height==0){
                Node below = new Node(new MyEntry(Integer.MIN_VALUE, "-inf"));
                s.below = below;
                below.above = s;
                Node inf = s.next;
                below = new Node(new MyEntry(Integer.MAX_VALUE, "+inf"));
                inf.below = below;
                below.above = inf;
                below.prev = s.below;
                s.below.next = below;
            }
        while(height <= level){//se l'altezza del nodo da inserire supera quella della skiplist aggiungo livelli con le sentinelle
                Node below = s.below;
                Node new_node = new Node(new MyEntry(Integer.MIN_VALUE, "-inf"));
                below.above = new_node;
                s.below = new_node;
                Node inf = s.next;
                below = inf.below;
                new_node = new Node(new MyEntry(Integer.MAX_VALUE, "+inf"));
                below.above = new_node;
                inf.below = new_node;
                new_node.prev = s.below;
                s.below.next = new_node;
        }
        while(temp_node.below != null){
            temp_node = temp_node.below();
            traversed_nodes++; // conto il nodo da cui scendo
            while(key >= temp_node.next().getKey()){
                temp_node = temp_node.next();
                traversed_nodes++; // conto il nodo  da cui mi sposto a dx
            }
        }
        traversed_nodes++; //per contare il nodo in cui si ferma
        Node next = temp_node.next;
        temp_node.next = to_be_inserted;
        next.prev = to_be_inserted;
        //ho inserito nel livello base la mia nuova entry

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
        if(n_entries == 0) return null;
        Node temp_node = s;
        while(temp_node.below != null)temp_node = temp_node.below; 
        temp_node = temp_node.next;
        MyEntry e = new MyEntry(temp_node.getKey(),temp_node.getValue());
        while(temp_node.above != null)temp_node = temp_node.above;
        while(temp_node.below != null){
            temp_node.H_delete();
            temp_node = temp_node.below;
        } 
        temp_node.H_delete();
        return e;
 // TO BE COMPLETED 
    }
    public void print() {
        Node temp_node = s;
        String out = "";
        while(temp_node.below != null) temp_node = temp_node.below;
        for(int i = 0; i < n_entries; i++){
            temp_node = temp_node.next;
            int temp_height = 0;
            Node height_node = temp_node;
            while(height_node.above != null){
                height_node = height_node.above;
                temp_height++;
            } 
            out+=temp_node + " " + temp_height +", ";
        }
        out = out.substring(0,out.length()-2);
        System.out.println(out);
 // TO BE COMPLETED 
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
 // TO BE COMPLETED 
                        break;
                    case 1:
 // TO BE COMPLETED 
                        break;
                    case 2:
 // TO BE COMPLETED 
                        break;
                    case 3:
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