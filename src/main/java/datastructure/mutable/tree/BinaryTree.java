package datastructure.mutable.tree;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;

/*
    Represents an empty tree node
 */
class Leaf extends BinaryTree{

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
class Branch<A> extends BinaryTree<A> {
    A data;
    BinaryTree<A> left;
    BinaryTree<A> right;

    @Override
    public String toString() {
        return String.format("Branch(%s, %s, %s)", data, left, right);
    }
}


/*
    Binary Tree functionality
 */
interface IBinaryTree<A> {
    Integer size();
    Integer depth();
    Boolean compare(BinaryTree<A> that);
    void flip();
    boolean flipEqual(BinaryTree<A> that);
}

/*
    Binary tree implementation
 */
public class BinaryTree<A> implements IBinaryTree<A> {

    static Leaf leaf = new Leaf();

    /*
        Initializes Binary tree
     */
    public static <B> BinaryTree<B> of(B... items) {
        if(items.length == 0) {
            return leaf;
        }

        int[] half = {items.length / 2, items.length / 2};

        B head = items[0];
        B[] leftItems = (B[])Arrays.stream(items).skip(1).takeWhile(elem -> half[0]-- > 0).toArray();
        B[] rightItems =  (B[])Arrays.stream(items).skip(1).dropWhile(elem -> half[1]-- > 0).toArray();

        return new Branch<>(head, of(leftItems), of(rightItems));
    }

    protected BinaryTree() {}

    /*
      Return the size of the tree
      Size of the tree is the number of nodes in the tree (Leaf nodes excluded)
    */
    @Override
    public Integer size() {
        if(this.isLeaf()) return 0;

        Branch<A> current = (Branch) this;

        return 1 + current.left.size() + current.right.size();
    }

    /*
        Depth is the length of the longest path from root to leaf
   */
    @Override
    public Integer depth() {
        if(this.isLeaf()) return 0;

        Branch<A> current = (Branch) this;

        return 1 + Math.max(current.left.depth(), current.right.depth());
    }

    /*
        Compares two trees
        return true if they are equal (values and structure)
   */
    @Override
    public Boolean compare(BinaryTree<A> that) {

        if(this.isLeaf() && that.isLeaf()) {

            return true;

        } else if (this.isNotLeaf() && that.isNotLeaf()) {

            Branch<A> thisTree = ((Branch<A>)this);
            Branch<A> thatTree = ((Branch<A>)that);

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
        Flip the tree left and right nodes
    */
    @Override
    public void flip() {

        if(this.isLeaf()) return;

        Branch<A> cursor = (Branch<A>) this;
        BinaryTree<A> left = cursor.left;

        cursor.setLeft(cursor.right);
        cursor.setRight(left);

        cursor.left.flip();
        cursor.right.flip();
    }

    /*
        Checks if "that" tree is the flip of the current tree (this)
    */
    @Override
    public boolean flipEqual(BinaryTree<A> that) {
        if(this.isLeaf() && that.isLeaf()) {

            return true;

        } else if (this.isNotLeaf() && that.isNotLeaf()) {

            Branch<A> thisTree = ((Branch<A>)this);
            Branch<A> thatTree = ((Branch<A>)that);

            if(thisTree.data.equals(thatTree.data)) {

                return thisTree.left.flipEqual(thatTree.right) &&
                        thisTree.right.flipEqual(thatTree.left);
            }else{

                return false;
            }

        } else {

            return false;
        }
    }

    private Boolean isLeaf() {
        return this instanceof Leaf;
    }

    private boolean isNotLeaf(){
        return !isLeaf();
    }


}
