package datastructure.immutable.tree;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/*
    Represents an empty tree node
 */
class LeafNode extends BinarySearchTree {

    @Override
    public String toString() {
        return "Leaf";
    }
}

/*
    Represents a non empty tree node
 */
@Data
@AllArgsConstructor
class BranchNode<A extends Comparable<A>> extends BinarySearchTree<A> {
    A data;
    BinarySearchTree<A> left;
    BinarySearchTree<A> right;

    @Override
    public String toString() {
        return String.format("Branch(%s, %s, %s)", data, left, right);
    }
}


/*
    Binary Tree functionality
 */
interface IBinarySearchTree<A extends Comparable<A>> {
    Integer size();
    Integer depth();
    Boolean compare(BinarySearchTree<A> that);
    <B extends Comparable<B>> BinarySearchTree<B> map(Function<A, B> f);
    BinarySearchTree<A> insert(A data);
    Optional<A> find(A key);
    void preOrderTraversal(Consumer<A> consumer);
    void inOrderTraversal(Consumer<A> consumer);
    void postOrderTraversal(Consumer<A> consumer);
}

/*
    Binary tree implementation
 */
public class BinarySearchTree<A extends Comparable<A>> implements IBinarySearchTree<A> {

    static LeafNode leafNode = new LeafNode();

    /*
        Initializes Binary tree
     */
    public static <B extends Comparable<B>> BinarySearchTree<B> of(B... items) {
        Arrays.sort(items);
        return bst(items);
    }

    private static <B extends Comparable<B>> BinarySearchTree<B> bst(B... items) {
        if(items.length == 0) {
            return leafNode;
        }

        int half = items.length / 2;

        B head = items[half];
        B[] leftItems = Arrays.copyOfRange(items, 0, half);
        B[] rightItems = Arrays.copyOfRange(items, half + 1, items.length);

        return new BranchNode<>(head, bst(leftItems), bst(rightItems));
    }

    protected BinarySearchTree() {}

    /*
      Return the size of the tree
      Size of the tree is the number of nodes in the tree (Leaf nodes excluded)
    */
    @Override
    public Integer size() {
        if(this.isLeaf()) return 0;

        BranchNode<A> current = (BranchNode) this;

        return 1 + current.left.size() + current.right.size();
    }

    /*
        Depth is the length of the longest path from root to leaf
   */
    @Override
    public Integer depth() {
        if(this.isLeaf()) return 0;

        BranchNode<A> current = (BranchNode) this;

        return 1 + Math.max(current.left.depth(), current.right.depth());
    }

    /*
        Compares two trees
        return true if they are equal (values and structure)
   */
    @Override
    public Boolean compare(BinarySearchTree<A> that) {

        if(this.isLeaf() && that.isLeaf()) {

            return true;

        } else if (this.isNotLeaf() && that.isNotLeaf()) {

            BranchNode<A> thisTree = ((BranchNode<A>)this);
            BranchNode<A> thatTree = ((BranchNode<A>)that);

            if(thisTree.data.equals(thatTree.data)) {

                return thisTree.left.compare(thatTree.left) &&
                        thisTree.right.compare(thatTree.right);
            }else{

                return false;
            }

        } else {

            return false;
        }
    }

    /*
        Apply function f to each node
    */
    @Override
    public <B extends Comparable<B>> BinarySearchTree<B> map(Function<A, B> f) {
        if(this.isLeaf()) return leafNode;
        BranchNode<A> current = (BranchNode) this;

        return new BranchNode<>(f.apply(current.data), current.left.map(f), current.right.map(f));
    }

    /*
        Adds new node
    */
    @Override
    public BinarySearchTree<A> insert(A data) {
        if(this.isLeaf()) return new BranchNode<>(data, leafNode, leafNode);

        BranchNode<A> current = (BranchNode) this;

        if(data.compareTo(current.data) < 0) {
            return new BranchNode<>(current.data, current.left.insert(data), current.right);
        }else{
            return new BranchNode<>(current.data, current.left, current.right.insert(data));
        }
    }

    /*
        For simplicity the key is the same as the value
        If key found return optional[key] otherwise return Nones
    */
    @Override
    public Optional<A> find(A key) {
        if(this.isLeaf()) return Optional.empty();

        BranchNode<A> current = (BranchNode) this;

        if(current.data.compareTo(key) == 0) return Optional.ofNullable(current.data);
        else if (current.data.compareTo(key) == 1) return current.left.find(key);
        else return current.right.find(key);
    }

    /*
        Visits node -> left -> right
    */
    @Override
    public void preOrderTraversal(Consumer<A> consumer) {
        if(this.isLeaf()) return;

        BranchNode<A> current = (BranchNode<A>) this;

        consumer.accept(current.data);
        current.left.preOrderTraversal(consumer);
        current.right.preOrderTraversal(consumer);
    }

    /*
        Visits left -> node -> right
    */
    @Override
    public void inOrderTraversal(Consumer<A> consumer) {
        if(this.isLeaf()) return;

        BranchNode<A> current = (BranchNode<A>) this;

        current.left.inOrderTraversal(consumer);
        consumer.accept(current.data);
        current.right.inOrderTraversal(consumer);
    }

    /*
        Visits left -> right -> node
    */
    @Override
    public void postOrderTraversal(Consumer<A> consumer) {
        if(this.isLeaf()) return;

        BranchNode<A> current = (BranchNode<A>) this;

        current.left.postOrderTraversal(consumer);
        current.right.postOrderTraversal(consumer);
        consumer.accept(current.data);
    }

    private Boolean isLeaf() {
        return this instanceof LeafNode;
    }

    private boolean isNotLeaf(){
        return !isLeaf();
    }


}
