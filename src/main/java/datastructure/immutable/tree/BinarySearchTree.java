package datastructure.immutable.tree;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
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

    @Override
    public <B extends Comparable<B>> BinarySearchTree<B> map(Function<A, B> f) {
        if(this.isLeaf()) return leafNode;
        BranchNode<A> cursor = (BranchNode) this;

        return new BranchNode<>(f.apply(cursor.data), cursor.left.map(f), cursor.right.map(f));
    }

    private Boolean isLeaf() {
        return this instanceof LeafNode;
    }

    private boolean isNotLeaf(){
        return !isLeaf();
    }


}
