package datastructure.immutable.list;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;


/*
    Represents an empty LinkedList (to allow null entries)
 */
class Nil extends LinkedList{

    @Override
    public String toString() {
        return "Nil";
    }
}

/*
    Represents a non empty LinkedList
 */
@Data
@AllArgsConstructor
class Item<A> extends LinkedList<A> {
    A hd;
    LinkedList<A> tl;

    @Override
    public String toString() {
        return String.format("Item(%s, %s)", hd, tl);
    }
}

/*
    LinkedList functionality
 */
interface ILinkedList<A> {
    boolean isEmpty();
    A head();
    LinkedList<A> tail();
    void traverse(Consumer<A> consumer);
    Optional<A> get(int index);
    Optional<A> getRecursive(int index);
    LinkedList<A> reverse();
    LinkedList<A> drop(int count);
    LinkedList<A> dropWhile(Predicate<A> predicate);
    LinkedList<A> prepend(A value);
    LinkedList<A> prependAll(LinkedList<A> that);
    LinkedList<A> prependAllReversed(LinkedList<A> that);
    LinkedList<A> append(A value);
    LinkedList<A> appendAll(LinkedList<A> that);
    LinkedList<A> delete(int index);
}


/*
    LinkedList implementation
 */
public class LinkedList<A> implements ILinkedList<A> {

    /*
        Only one Nil object is required
     */
    static Nil nil = new Nil();

    /*
        Initializes LinkedList
        Assign the first element to head
        Recursively do same for the rest of the array
     */
    public static <B> LinkedList<B> of(B... items){
        if(items.length == 0) {
           return nil;
        }
        else {
            B head = items[0];
            B[] tail = (B[])Arrays.stream(items).skip(1).toArray();
            return new Item<>(head, of(tail));
        }
    }

    protected LinkedList(){}

    /*
    returns true if the list is empty
   */
    @Override
    public boolean isEmpty() {

        return isNil() ? true : false;
    }

    /*
        Returns the first element in the list
    */
    @Override
    public A head() {
        if(isNil()){
            throw new NoSuchElementException("List is empty");
        }

        return ((Item<A>)this).hd;
    }

    /*
        Returns the list elements without the head (first element)
    */
    @Override
    public LinkedList<A> tail() {
        if(isNil()){
            throw new NoSuchElementException("List is empty");
        }

        return ((Item<A>)this).tl;
    }

    /*
        Apply a consumer on each node
   */
    @Override
    public void traverse(Consumer<A> consumer) {

        LinkedList<A> cursor = this;

        while(cursor.isNotNil()){
            consumer.accept(cursor.head());
            cursor = cursor.tail();
        }
    }

    /*
        Gets an element at index
    */
    @Override
    public Optional<A> get(int index) {

        LinkedList<A> cursor = this;

        if(index < 0) throw new IndexOutOfBoundsException();

        while(index-- > 0 && cursor.isNotNil()) cursor = cursor.tail();

        if(cursor.isNil()) throw new IndexOutOfBoundsException();

        return Optional.ofNullable(cursor.head());
    }


    /*
        An example of recursive implementation
    */
    BiFunction<LinkedList<A>, Integer, Optional<A>> getRecursive;

    @Override
    public Optional<A> getRecursive(int index) {

        getRecursive = (current, idx) -> {
                if(idx < 0 || current.isNil()) throw new IndexOutOfBoundsException();
                if(idx == 0) return Optional.ofNullable(current.head());
                return getRecursive.apply(current.tail(), idx - 1);
            };

        return getRecursive.apply(this, index);
    }

    /*
        Reverses the current(this) list
   */
    @Override
    public LinkedList<A> reverse() {

        LinkedList<A> cursor = this;

        if(cursor.isNil()) return nil;

        Item<A> accumulator = new Item<>(cursor.head(), nil);

        while((cursor = cursor.tail()).isNotNil()){
            Item<A> item = new Item<>(cursor.head(), accumulator);
            accumulator = item;
        }

        return accumulator;

    }

    /*
        Drops n elements from the beginning of the current list (this)
   */
    @Override
    public LinkedList<A> drop(int count) {
        LinkedList<A> cursor = this;

        if(cursor.isNil()) return nil;

        while(cursor.isNotNil() && count-- > 0) {
            cursor = cursor.tail();
        }

        return cursor;
    }

    /*
        Drops the elements from the current list (this) while the condition evaluates to true
   */
    @Override
    public LinkedList<A> dropWhile(Predicate<A> predicate) {
        LinkedList<A> cursor = this;

        if(cursor.isNil()) return nil;

        while(cursor.isNotNil() && predicate.test(cursor.head())) {
            cursor = cursor.tail();
        }

        return cursor;
    }

    /*
        Adds new element (value) at the beginning of the current list (this)
    */
    @Override
    public LinkedList<A> prepend(A value) {
        return new Item<>(value, this);
    }

    /*
        Adds "that" list at the beginning of current (this) list
   */
    @Override
    public LinkedList<A> prependAll(LinkedList<A> that) {
        LinkedList<A> list = this;
        LinkedList<A> reversedThatCursor = that.reverse();

        while(reversedThatCursor.isNotNil()){
            list = list.prepend(reversedThatCursor.head());
            reversedThatCursor = reversedThatCursor.tail();
        }

        return list;
    }

    /*
       Adds reversed "that" list at the beginning of current (this) list
  */
    @Override
    public LinkedList<A> prependAllReversed(LinkedList<A> that) {
        LinkedList<A> list = this;
        LinkedList<A> reversedThatCursor = that;

        while(reversedThatCursor.isNotNil()){
            list = list.prepend(reversedThatCursor.head());
            reversedThatCursor = reversedThatCursor.tail();
        }

        return list;
    }

    /*
        Adds an element in front of current list (this)
   */
    @Override
    public LinkedList<A> append(A value) {
        return of(value).prependAll(this);
    }

    /*
        Adds "that" list in front of current (this) list
    */
    @Override
    public LinkedList<A> appendAll(LinkedList<A> that) {
        return that.prependAll(this);
    }

    /*
        Deletes element at index
     */
    @Override
    public LinkedList<A> delete(int index) {
        LinkedList<A> cursor = this;

        if(cursor.isNil() || index < 0)  throw new IndexOutOfBoundsException();

        LinkedList<A> accumulator = nil;

        while(cursor.isNotNil() && index >= 0){
            if(index == 0) {
                accumulator = cursor.tail().prependAllReversed(accumulator);
            }else{
                accumulator = accumulator.prepend(cursor.head());
                cursor = cursor.tail();
            }

            index--;
        }

        if(cursor.isNil()) throw new IndexOutOfBoundsException();

        return accumulator;
    }

    private Boolean isNil() {
        return this instanceof Nil;
    }

    private Boolean isNotNil() {
        return !isNil();
    }
}
