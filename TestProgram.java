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
        Node prev_ = this.prev;
        Node next_ = this.next;
        prev_.next = next_;
        next_.prev = prev_;
        this.prev = null;
        this.next = null;
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
               
    }

    //ritorno il numero di entry inserite (nel piano base)
    public int size() {
        return n_entries;       
    }

    //se c'è almeno una entry, finché si può scendere scendo, poi prendo il primo nodo dopo la sentinella -inf -> ritorno la entry di quel nodo
    public MyEntry min() {
        if(n_entries == 0) return null;
        Node temp_node = s;
        while(temp_node.below != null) temp_node = temp_node.below;
        temp_node = temp_node.next;
        return new MyEntry(temp_node.getKey(),temp_node.getValue());
    }

    public int insert(int key, String value) {
        int traversed_nodes = 0;
        int level = generateEll(alpha, key);
        
        if(height==0){//se ho solo una skiplist di base con le 2 sentinelle -> creo un livello sotto per fare da base
                //aggiungo e creo collegamenti verticali a -inf
                Node below = new Node(new MyEntry(Integer.MIN_VALUE, "-inf"));
                s.below = below;
                below.above = s;
                //aggiungo e creo collegamenti verticali a +inf
                Node inf = s.next;
                below = new Node(new MyEntry(Integer.MAX_VALUE, "+inf"));
                inf.below = below;
                below.above = inf;
                //creo collegamenti orizzontali
                below.prev = s.below;
                s.below.next = below;
                height++;
            }
        //nuova implementazione
        Node to_be_inserted = new Node(new MyEntry(key,value));
        Node temp_node = s;
        while(temp_node.below != null){
            temp_node = temp_node.below;
            traversed_nodes++;
            while(key >= temp_node.next.getKey()){
                temp_node = temp_node.next;
                traversed_nodes++;
            }
        }
        traversed_nodes++;
        //inserimento alla base
        Node next = temp_node.next;
        next.prev = to_be_inserted;
        to_be_inserted.next = next;
        temp_node.next = to_be_inserted;
        to_be_inserted.prev = temp_node;
        //fine nuova implementazione
        while(height <= level){//se l'altezza del nodo da inserire supera quella della skiplist aggiungo livelli con le sentinelle
                //aggiungo e creo collegamenti verticali a -inf
                Node below;
                below = s.below;
                Node new_node = new Node(new MyEntry(Integer.MIN_VALUE, "-inf"));
                below.above = new_node;
                new_node.below = below;
                s.below = new_node;
                new_node.above = s;
                //aggiungo e creo collegamenti verticali a +inf
                Node inf = s.next;
                below = inf.below;
                new_node = new Node(new MyEntry(Integer.MAX_VALUE, "+inf"));
                below.above = new_node;
                new_node.below = below;
                inf.below = new_node;
                new_node.above = inf;
                //creo collegamenti orizzontali
                new_node.prev = s.below;
                s.below.next = new_node;
                height++;
        }
        int current_height = 0;
        Node new_insert;
        while(current_height < level){
            new_insert = new Node(new MyEntry(key,value));
            //link verticale
            to_be_inserted.above = new_insert;
            new_insert.below = to_be_inserted;
            //link orizzontale
            //System.out.print(current_height+ " ");
            while(temp_node.above == null) temp_node = temp_node.prev;
            temp_node = temp_node.above;
            next = temp_node.next;
            temp_node.next = new_insert;
            new_insert.prev = temp_node;
            next.prev = new_insert;
            new_insert.next = next;
            to_be_inserted = new_insert;
            current_height++;
        }
        n_entries++;
        //System.out.println("entry "+key+ " "+ value+ ", inserita");
        return traversed_nodes;
  
    }

    //fornito nel template
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

    // se c'è almeno una entry, finché si può scendere scendo, poi prendo il primo nodo dopo la sentinella -inf, salvo la entry del nodo(da ritornare),
    // e salgo recidendo i legami orizzontali -> verticalmente rimane una struttura separata dalla lista bidimensionale di nodi e senza riferimento
    // -> eliminata dal GARBAGE COLLECTOR , poi guardo se l'altezza della struttura deve diminuire
    public MyEntry removeMin() {
        if(n_entries == 0) return null;
        Node temp_node = s;
        while(temp_node.below != null)temp_node = temp_node.below; 
        temp_node = temp_node.next;
        MyEntry e = new MyEntry(temp_node.getKey(),temp_node.getValue());
        temp_node.H_delete();
        while(temp_node.above != null){
            temp_node = temp_node.above;
            temp_node.H_delete();
        }
        n_entries--;
        temp_node = s.below;
        while(temp_node != null && (temp_node.next.getValue() == "+inf")){
            s = temp_node;
            s.above = null;
            s.next.above = null;
            height--;
            temp_node = s.below;
        }
        return e;
    }
    public void print() {
        Node temp_node = s;
        String out = "";
        while(temp_node.below != null) temp_node = temp_node.below;
        for(int i = 0; i < n_entries; i++){
            temp_node = temp_node.next;
            int temp_height = 1;
            Node height_node = temp_node;
            while(height_node.above != null){
                height_node = height_node.above;
                temp_height++;
            } 
            out+=temp_node + " " + temp_height +", ";
        }
        out = out.substring(0,out.length()-2);
        System.out.println(out);
  
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
            //aggiunta mia 
            int total_traversed_nodes = 0;
            int n_inserts = 0;
            //fine aggiunta mia
            for (int i = 0; i < N; i++) {
                String[] line = br.readLine().split(" ");
                int operation = Integer.parseInt(line[0]);
                switch (operation) {
                    case 0:
                        MyEntry min_entry = skipList.min();
                        if(min_entry != null) System.out.println(min_entry);
  
                        break;
                    case 1:
                        skipList.removeMin();
  
                        break;
                    case 2:
                        int key = Integer.parseInt(line[1]);
                        String value = line[2];
                        total_traversed_nodes += skipList.insert(key, value);
                        n_inserts++;
  
                        break;
                    case 3:
                        skipList.print();
 
                        break;
                    default:
                        System.out.println("Invalid operation code");
                        return;
                }
            }
            //aggiunta mia
            double average_nodes = ((double)total_traversed_nodes/n_inserts);
            //System.out.println("totale nodi attraversati: "+ total_traversed_nodes);
            System.out.println(alpha + " " + skipList.size() + " " + n_inserts + " " + average_nodes);
            //fine aggiunta mia
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
 }
 /*
 int current_height = height;
        Node temp_node = s;
        Node to_be_inserted = new Node(new MyEntry(key,value));
        //temp_node parte da s (in alto a sx)
        while(temp_node.below != null){
            current_height--;
            temp_node = temp_node.below;
            traversed_nodes++; // conto il nodo da cui scendo
            while(key >= temp_node.next.getKey()){//probabilmente quando hai fatto un collegamento orizz hai scazzato, controlla
                temp_node = temp_node.next;
                traversed_nodes++; // conto il nodo  da cui mi sposto a dx
            }
            //inserimento
            if(current_height<= level){
                if(current_height < level){
                    //creo un collegamnto verticali se ho già inserito ad un livello più alto 
                    Node new_node = new Node(new MyEntry(key,value));
                    to_be_inserted.below = new_node;
                    new_node.above = to_be_inserted;
                    to_be_inserted = new_node;
                }
                //inserisco il nodo al livello in cui mi trovo (collegamenti orizzontali)
                Node next = temp_node.next;
                temp_node.next = to_be_inserted;
                to_be_inserted.prev = temp_node;
                next.prev = to_be_inserted;   
                to_be_inserted.next = next;
            }
        }
        traversed_nodes++; //per contare il nodo in cui si ferma
 */ 