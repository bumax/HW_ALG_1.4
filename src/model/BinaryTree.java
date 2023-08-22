package model;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree<T extends Comparable<T>> {
    public BinaryTree() {
    }
    Node root;

    public boolean add(T value) {
        if (root == null) {
            root = new Node(value);
            root.color = Color.Black;
            return true;
        }

        root.color = Color.Black;
        var res = addLeaf(root, value);
        return res != null;
    }

    private Node doBalance(Node node) {
        Node res = node;
        boolean needBalance;
        do {
            needBalance = false;
            if (node != null && node.parent != null && node.parent.color == Color.Red) {
                if (node.parent.rightChild != null && node.parent.rightChild.color == Color.Red && node.parent.leftChild != null && node.parent.leftChild.color == Color.Red) {
                    res = colorSwap(node.parent);
                    needBalance = true;
                } else
                if (node.parent.rightChild != null && node.parent.rightChild.color == Color.Red && (node.parent.leftChild == null || node.parent.leftChild.color == Color.Black)) {
                    rotateLeft(node.parent);
                    needBalance = true;
                } else
                if (node.parent.leftChild != null && node.parent.leftChild.color == Color.Red && (node.parent.rightChild == null || node.parent.rightChild.color == Color.Black)) {
                    rotateRight(node.parent);
                    needBalance = true;
                }
            }
        } while (needBalance);
        return res;
    }

    private Node addLeaf(Node root, T value) {
        // если значение больше корня, идем в правую ветвь
        if (root.value.compareTo(value) < 0) {
            if (root.rightChild != null) {
                Node result =  addLeaf(root.rightChild, value);
                doBalance(root.rightChild);
                return result;
            } else {
                root.rightChild = new Node(value);
                root.rightChild.parent = root;
                return root.rightChild;
            }
        }
        // если значение меньше корня, идем в левую ветвь
        if (root.value.compareTo(value) > 0) {
            if (root.leftChild != null) {
                Node result = addLeaf(root.leftChild, value);
                doBalance(root.leftChild);
                return result;
            } else {
                root.leftChild = new Node(value);
                root.leftChild.parent = root;
                return root.leftChild;
            }
        }
        return null;
    }

     private void rotateLeft(Node node) {
        Node parent = node.parent;
        Node rightChild = node.rightChild;

        node.rightChild = rightChild.leftChild;
        if (rightChild.leftChild != null) {
            rightChild.leftChild.parent = node;
        }

        rightChild.leftChild = node;
        node.parent = rightChild;

        replaceParentsChild(parent, node, rightChild);
    }

    private void rotateRight(Node node) {
        Node parent = node.parent;
        Node leftChild = node.leftChild;

        node.leftChild = leftChild.rightChild;
        if (leftChild.rightChild != null) {
            leftChild.rightChild.parent = node;
        }

        leftChild.rightChild = node;
        node.parent = leftChild;

        replaceParentsChild(parent, node, leftChild);
    }

    private void replaceParentsChild(Node parent, Node oldChild, Node newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.leftChild == oldChild) {
            parent.leftChild = newChild;
        } else if (parent.rightChild == oldChild) {
            parent.rightChild = newChild;
        } else {
            throw new IllegalStateException("Node is not a child of its parent");
        }

        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    private Node colorSwap(Node parentNode) {
        Node result = parentNode;
        result.color = Color.Red;
        result.rightChild.color = Color.Black;
        result.leftChild.color = Color.Black;
        return result;
    }


    private class Node {
        public Node(T value) {
            this.value = value;
            // новое значение всегда красное
            color = Color.Red;
        }

        T value;
        Node parent;
        Node leftChild;
        Node rightChild;
        Color color;
    }

    private enum Color {Red, Black}

    private int x = 200; // число символов в буфере вывода по горизонтали в буфере вывода
    private int y = 200; // число символов в буфере вывода по вертикали в буфере вывода

    public void print() {
        // формируем буфер вывода
        char[][] out = new char[x][y];
        // рекурсивно наполняем буфер данными
        print(out, new PrintPos(x / 2, 0), root);
        for (int i = 0; i < y; i++) {
            // формируем строку для печати
            char[] charLine = new char[x];
            boolean emptyLine = true;
            for (int j = 0; j < x; j++) {
                // Заменяем нулевые символы пробелами
                if (out[j][i] == 0)
                    charLine[j] = ' ';
                else {
                    charLine[j] = out[j][i];
                    emptyLine = false;
                }
            }
            // Если строка не пустая, то выводим её на печать
            if (!emptyLine) {
                String line = new String(charLine);
                System.out.println(line);
            }
        }
    }

    private void print(char[][] out, PrintPos position, Node node) {
        char[] printVal = node.value.toString().toCharArray();
        for (int i = 0; i < printVal.length; i++) {
            if (node.color == Color.Red)
                out[position.x - 1][position.y] = '!'; // Если нода красная, то приписываем восклицательный знак
            out[position.x + i][position.y] = printVal[i];
        }
        if (node.leftChild != null || node.rightChild != null) {
            position.y++;
            out[position.x][position.y] = '|';
        }
        if (node.leftChild != null) {
            int maxX;
            if(position.x >= x/2)
                maxX = (x - position.x)/2;
            else
                maxX = (x/2 - position.x)/2;
            for (int i = 0; i < maxX; i++)
                out[position.x - i][position.y + 1] = '-';
            out[position.x - maxX][position.y + 1] = '┌';
            print(out, new PrintPos(position.x - maxX, position.y + 2), node.leftChild);
        }
        if (node.rightChild != null) {
            int maxX;
            if(position.x >= x/2)
                maxX = (x - position.x)/2;
            else
                maxX = (x/2 - position.x)/2;
            for (int i = 0; i < maxX; i++)
                out[position.x + i][position.y + 1] = '-';
            out[position.x + maxX][position.y + 1] = '┐';
            print(out, new PrintPos(position.x + maxX, position.y + 2), node.rightChild);
        }
    }

    private class PrintPos {
        PrintPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x;
        public int y;
    }

}