import model.BinaryTree;

public class Main {
    public static void main(String[] args) {

        BinaryTree<Integer> bt = new BinaryTree<>();
        bt.add(5);
        bt.add(9);
        bt.add(8);
        bt.add(7);
        bt.add(6);
        bt.print();
    }
}