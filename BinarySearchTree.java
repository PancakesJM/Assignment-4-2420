package assign08;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * A sorted set backed by an unbalanced binary search tree.
 * Items are ordered by their natural ordering and duplicates are not stored.
 *
 * @param <Type> - the type of items stored in the set
 * @author Yujia Zhao and Ava Murphy
 * @version November 4, 2026
 */
public class BinarySearchTree<Type extends Comparable<? super Type>> implements SortedSet<Type> {

    private Node root;
    private int size;

    /**
     * Represents a node in this binary search tree. Each node stores an item
     * and references to its left and right children.
     */
    private class Node {
        private Type data;
        private Node left;
        private Node right;

        /**
         * Constructs a node containing the specified item.
         *
         * @param data - the item stored in this node
         */
        private Node(Type data) {
            this.data = data;
        }
    }

    /**
     * Constructs an empty binary search tree.
     */
    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    /**
     * Ensures that this set contains the specified item. An item with the same
     * natural ordering as an existing item is not inserted.
     *
     * @param item - the item whose presence is ensured in this set
     * @return true if the item was inserted; otherwise, false
     */
    @Override
    public boolean add(Type item) {
        if (root == null) {
            root = new Node(item);
            size++;
            return true;
        }

        Node current = root;
        while (true) {
            int comparison = item.compareTo(current.data);
            if (comparison == 0) {
                return false;
            }
            if (comparison < 0) {
                if (current.left == null) {
                    current.left = new Node(item);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(item);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    /**
     * Ensures that this set contains all items in the specified collection.
     *
     * @param items - the collection of items to insert into this set
     * @return true if at least one item was inserted; otherwise, false
     */
    @Override
    public boolean addAll(Collection<? extends Type> items) {
        boolean changed = false;
        for (Type item : items) {
            if (add(item)) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Removes all items from this set. The set is empty after this call.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Determines whether this set contains the specified item.
     *
     * @param item - the item sought in this set
     * @return true if the item is present; otherwise, false
     */
    @Override
    public boolean contains(Type item) {
        Node current = root;
        while (current != null) {
            int comparison = item.compareTo(current.data);
            if (comparison == 0) {
                return true;
            }
            current = comparison < 0 ? current.left : current.right;
        }
        return false;
    }

    /**
     * Determines whether this set contains every item in the specified collection.
     *
     * @param items - the collection of items sought in this set
     * @return true if every item is present; otherwise, false
     */
    @Override
    public boolean containsAll(Collection<? extends Type> items) {
        for (Type item : items) {
            if (!contains(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether this set contains no items.
     *
     * @return true if the set is empty; otherwise, false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the smallest item in this set according to natural ordering.
     *
     * @return the smallest item in this set
     * @throws NoSuchElementException if the set is empty
     */
    @Override
    public Type min() throws NoSuchElementException {
        if (root == null) {
            throw new NoSuchElementException("Cannot find minimum in an empty set");
        }
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.data;
    }

    /**
     * Returns the largest item in this set according to natural ordering.
     *
     * @return the largest item in this set
     * @throws NoSuchElementException if the set is empty
     */
    @Override
    public Type max() throws NoSuchElementException {
        if (root == null) {
            throw new NoSuchElementException("Cannot find maximum in an empty set");
        }
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.data;
    }

    /**
     * Ensures that this set does not contain the specified item. When a node
     * has two children, its item is replaced with its inorder successor.
     *
     * @param item - the item whose absence is ensured in this set
     * @return true if an item was removed; otherwise, false
     */
    @Override
    public boolean remove(Type item) {
        Node parent = null;
        Node current = root;

        while (current != null) {
            int comparison = item.compareTo(current.data);
            if (comparison == 0) {
                break;
            }
            parent = current;
            current = comparison < 0 ? current.left : current.right;
        }
        if (current == null) {
            return false;
        }

        if (current.left != null && current.right != null) {
            // Replace the item with its inorder successor, then remove that successor.
            Node successorParent = current;
            Node successor = current.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            current.data = successor.data;
            parent = successorParent;
            current = successor;
        }

        // Here current has at most one child.
        Node replacement = current.left != null ? current.left : current.right;
        if (parent == null) {
            root = replacement;
        } else if (parent.left == current) {
            parent.left = replacement;
        } else {
            parent.right = replacement;
        }
        size--;
        return true;
    }

    /**
     * Ensures that this set does not contain any item in the given collection.
     *
     * @param items - the collection of items to remove from this set
     * @return true if at least one item was removed; otherwise, false
     */
    @Override
    public boolean removeAll(Collection<? extends Type> items) {
        boolean changed = false;
        for (Type item : items) {
            if (remove(item)) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Returns the number of items in this set.
     *
     * @return the number of items in this set
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns an ArrayList containing all items in ascending order.
     * The items are collected using an inorder traversal.
     *
     * @return an ArrayList containing all items in sorted order
     */
    @Override
    public ArrayList<Type> toArrayList() {
        ArrayList<Type> result = new ArrayList<>();
        addInOrder(root, result);
        return result;
    }

    /**
     * Adds the items in the specified subtree to a list in ascending order.
     *
     * @param node - the root of the subtree to traverse
     * @param result - the list to which the items are added
     */
    private void addInOrder(Node node, ArrayList<Type> result) {
        if (node == null) {
            return;
        }
        addInOrder(node.left, result);
        result.add(node.data);
        addInOrder(node.right, result);
    }
}
