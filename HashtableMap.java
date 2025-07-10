import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashtableMap <KeyType, ValueType> implements MapADT<KeyType, ValueType>{

    protected LinkedList<Pair>[] table = null ;

    /**
     * Default constructor
     */
    @SuppressWarnings("unchecked")
    public HashtableMap(){

        this.table = new LinkedList[64];
    }

    /**
     * Constructor that allows to specify the size of the array for table
     * @param capacity the size of the array used for the table field
     */
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity){
        this.table = new LinkedList[capacity];
    }


    /**
     * Inner class to store pairs of ket-value pairs for the hashtable array
     */
    protected class Pair{
        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value){
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Adds a new key,value pair/mapping to this collection.
     *
     * @param key   the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException     if key is null
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {

    }

    /**
     * Checks whether a key maps to a value in this collection.
     *
     * @param key the key to check
     * @return true if the key maps to a value, and false is the key doesn't map to a value
     */
    @Override
    public boolean containsKey(KeyType key) {
        return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     *
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this collection
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        return null;
    }

    /**
     * Remove the mapping for a key from this collection.
     *
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this collection
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        return null;
    }

    /**
     * Removes all key,value pairs from this collection.
     */
    @Override
    public void clear() {

    }

    /**
     * Retrieves the number of keys stored in this collection.
     *
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize() {
        return 0;
    }

    /**
     * Retrieves this collection's capacity.
     *
     * @return the size of the underlying array for this collection
     */
    @Override
    public int getCapacity() {
        return 0;
    }

    /**
     * Tests the general implementation of put(). The test checks if Pair objects are created with
     * the expected key-value pairs and added into the table field of the HashtableMap class.
     */
    @Test
    public void testHashtableMapPut(){
        HashtableMap<Integer, String> testHashtable = new HashtableMap<>();
        testHashtable.put(908100100, "Prateek" );

        // Let's assume this key-value pair gets mapped to index 0
        assertEquals("Prateek:", testHashtable.table[0].getFirst().value,
                "Value didn't match");

        assertEquals(908100100, testHashtable.table[0].getFirst().key,
                "Key didn't match");

        // Let's assume this key-value pair gets mapped to index 1
        testHashtable.put(908100101,"Chand");

        assertEquals("Chand:", testHashtable.table[1].getFirst().value,
                "Value didn't match");

        assertEquals(908100100, testHashtable.table[1].getFirst().key);

    }

    /**
     * Tests the implementation of put() when collision occurs. A complete implementation of the
     * HashtableMap uses chaining to handle collision when different keys are mapped to same
     * index. The class uses an array of LinkedList. So when collision occurs at an index, the
     * key-value pair get chained into the LinkedList of that particular index.
     */
    @Test
    public void testHashtableMapChaining(){
        HashtableMap<Integer, String> testHashtable = new HashtableMap<>();
        // let's assume this key-value pair gets mapped to index 0
        testHashtable.put(908100100, "Prateek");

        //Let's expect the following key will produce the same hash code as above key
        testHashtable.put(908100101, "Chand");

        assertEquals("Chand", testHashtable.table[0].get(1).value, "Value didn't match");

        assertEquals(908100100, testHashtable.table[0].get(1).key, "Key didn't match");
    }

    /**
     * Tests the implementation of contains() for looking up a key. A complete implementation of
     * this method should return true if the key exists, false otherwise. In best case, when keys
     * get hashed to different indices in the table, the lookup in the array should take O(1). In
     * worst case, if the keys get mapped to the same index, the linked list should be traversed
     * and the lookup will take O(N).
     */
    @Test
    public void testHashtableMapLookUp(){
        HashtableMap<Integer, String> testHashtable = new HashtableMap<>();
        // let's assume this key-value pair gets mapped to index 0
        testHashtable.put(908100100, "Prateek");

        assertTrue(testHashtable.containsKey(908100100), "Didn't find the key when it was" +
                "present in the table");

    }

    /**
     * Tests the implementation of .get() for retrieving the specific value that a key maps to. A
     * complete implementation, similar to the contains(), will have best case time complexity of
     * O(1) and worst case of O(N).
     */
    @Test
    public void testHashtableGetValue(){
        HashtableMap<Integer, String> testHashtable = new HashtableMap<>();
        // let's assume this key-value pair gets mapped to index 0
        testHashtable.put(908100100, "Prateek");

        assertFalse(testHashtable.get(908100100).isEmpty(), "Value for the key not retrieved");
        assertEquals("Prateek", testHashtable.get(908100100), "Incorrect value returned");
    }

    /**
     * Tests the implementation of .getSize() and .getCapacity(). A correct implementation of
     * getSize() should return the number of keys in every array index across all the linked list
     * elements. A correct implementation of getCapacity() should return the size of the underlying
     * array used by the table.
     */
    @Test
    public void testHashtableSizeAndCapacity(){
        HashtableMap<Integer, String> testHashtable = new HashtableMap<>(10);
        // let's assume this key-value pair gets mapped to index 0
        testHashtable.put(908100100, "Prateek");
        // let's assume this key-value pair gets mapped to index 0
        testHashtable.put(908100101, "Chand");
        // let's assume this key-value pair gets mapped to index 1
        testHashtable.put(908100102, "Apple");
        // let's assume this key-value pair gets mapped to index 2
        testHashtable.put(908100103, "Mango");
        // let's assume this key-value pair gets mapped to index 3
        testHashtable.put(908100104, "Banana");
        // let's assume this key-value pair gets mapped to index 1
        testHashtable.put(908100105, "Dog");

        // Normal Approach
        assertEquals(6, testHashtable.getSize() , "The hash table didn't return correct size");
        assertEquals(10, testHashtable.getCapacity(), "The capacity of the table didn't match");

        // Bruteforce check
        assertEquals(testHashtable.table.length, testHashtable.getCapacity(), "Bruteforce " +
                "Approach: The returned capacity didn't match" );

        int size = 0;
        // Iterates through the entire array and counts the elements in the linked list at each
        // index location
        for(LinkedList<HashtableMap<Integer, String>.Pair> currentIndex: testHashtable.table){
            size += currentIndex.size();
        }

        assertEquals(size, testHashtable.getSize(),"Bruteforce Approach: The returned size didn't" +
                " match");
    }


}
