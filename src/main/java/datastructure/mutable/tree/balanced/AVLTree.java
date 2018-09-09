package datastructure.mutable.tree.balanced;


class AVLNode {
    int key;
    int height;
    AVLNode left;
    AVLNode right;

    public AVLNode(int key) {
        this.key = key;
        this.height = 0;
    }

    @Override
    public String toString() {
        return String.format("[%s, left = %s, right = %s]", key, left, right);
    }
}

public class AVLTree {

    AVLNode root;

    public AVLNode root() {return root;}

    public AVLNode find(int key) {

        AVLNode current = root;

        while(current != null && current.key != key) {
            if(key < current.key) current = current.left;
            else current = current.right;
        }

        return current;
    }

    public void insert(int key) {
        root = insert(root, key);
    }

    private AVLNode insert(AVLNode node, int key) {
        if(node == null) return new AVLNode(key);

        if(key < node.key) node.left = insert(node.left, key);
        else node.right = insert(node.right, key);

        updateHeight(node);
        node = doBalance(node);

        return node;
    }

    public void delete(int key) {
        root = delete(root, key);
    }

    private AVLNode delete(AVLNode node, int key) {
        if(node == null) return null;

        if(key < node.key) node.left = delete(node.left, key);
        else if (key > node.key) node.right = delete(node.right, key);
        else {
            if(node.left == null) return node.right;
            else if(node.right == null) return node.left;

            AVLNode replaceNode = findMin(node.right);
            replaceNode.right = delete(node.right, replaceNode.key);
            replaceNode.left = node.left;
            node = replaceNode;
        }

        updateHeight(node);
        node = doBalance(node);

        return node;
    }

    private AVLNode findMin(AVLNode node) {
        if(node == null) return null;

        while(node.left != null) {
            node = node.left;
        }

        return node;
    }

    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int height(AVLNode node) {
        if(node == null) return -1;

        return node.height;
    }

    private int balanceFactor(AVLNode node) {
        return height(node.left) - height(node.right);
    }

    private AVLNode rotateLeft(AVLNode node) {
        AVLNode newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private AVLNode rotateRight(AVLNode node) {
        AVLNode newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private AVLNode doBalance(AVLNode node) {
        int balance = balanceFactor(node);

        if(balance > 1) // indicates either left-left or left-right
        {
            if(balanceFactor(node.left) < 0) // left-right
            {
                node.left = rotateLeft(node.left);
            }
            node = rotateRight(node);

        } else if (balance < -1) // indicates either right-right or right-left
        {
            if(balanceFactor(node.right) > 0) // right-left
            {
                node.right = rotateRight(node.right);
            }
            node = rotateLeft(node);
        }

        return node;
    }
}
