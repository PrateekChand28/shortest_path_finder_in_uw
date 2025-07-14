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
        if(key == null){
            throw new NullPointerException("null keys not allowed");
        }
        if(this.containsKey(key)){
            throw new IllegalArgumentException("key " + key.toString() + " already present in the" +
                    " map");
        }

        // uses the Pair class to generate a Pair object that stores the key value pair
        Pair keyValuePairToPut = new Pair(key, value);
        int indexForPair = this.calculateHashCode(key);

        // initializes the null references with new linked list if the index doesn't already
        // contain one. When the table is first initialized in the constructor, all the index values
	// are set to null so calling add directly can cause an exception
        if(this.table[indexForPair] == null){
            this.table[indexForPair] = new LinkedList<>();
        }

        this.table[indexForPair].add(keyValuePairToPut);
        // Checks if rehashing is necessary after mapping this pair
        this.performRehash();
    }

    /**
     * Computes a hash code for the given key and returns the corresponding index.
     * Index in the table corresponds to the absolute value of the key's hashcode modulus the
     * current capacity of the table array
     */
    private int calculateHashCode(KeyType key){
        return Math.abs(key.hashCode()) % this.table.length;
    }

    /**
     * Calculates the load factor for current table.
     *
     * A hash table's load factor is the number of entries divided by the number of buckets(size
     * of the array). In worst case, traversing through the Pair objects at a certain index may
     * take O(N). The load factor needs to be kept low so that the number of entries at one index
     * is less and so the time complexity is almost constant.
     *
     * @return the load factor for current table
     */
    private double calculateLoadFactor(){
        return this.getSize() / (double) this.getCapacity();
    }

    /**
     * Dynamically grows the table dy doubling its capacity and rehashing whenever the load
     * factor is greater than or equal to 80%.
     *
     * A new array is allocated and all the items from the old array are re-inserted into the new
     * array, making the rehashing operation's time complexity O(N).
     *
     * Steps involved:
     * For each new entry to the map, the load factor is checked.
     * If the load factor is greater than 80% then the array size is doubled and all the
     * existing entries are copied into this array.
     * Since the size of the array has increased, the hash code will also change leading to different 
     * index locations for elements in the old table.
     */
    private void performRehash(){
        // if the load factor is less than 80% continue using the existing table
        if(this.calculateLoadFactor() < 0.80){
            return;
        }
        // when the load factor exceeds the threshold, a new table with double the size of the current
        // table is created
        LinkedList<Pair>[] oldTable = this.table;
        this.table =  new LinkedList[this.table.length * 2];;

        // all the key-value pairs of the current table are copied into the new table. However,
        // since the capacity of the new array changes, they key needs to be mapped again
        for (LinkedList<Pair> listAtCurrentIndex : oldTable) {
            // skip the current index if empty
            if (listAtCurrentIndex == null) {
                continue;
            }
            // traverse through the linked list, and recalculate the hash code for each key in this
            // linked list before remapping
            for (Pair pairInCurrentList : listAtCurrentIndex) {
                int newIndex = this.calculateHashCode(pairInCurrentList.key);
		// still need to check if the index is empty before calling .add()
                if(this.table[newIndex] == null){
                    this.table[newIndex] = new LinkedList<>();
                }
                this.table[newIndex].add(pairInCurrentList);
            }
        }
    }

    /**
     * Checks whether a key maps to a value in this collection.
     *
     * @param key the key to check
     * @return true if the key maps to a value, and false is the key doesn't map to a value
     */
    @Override
    public boolean containsKey(KeyType key) {
        if (key == null){
            throw new NullPointerException("null keys not allowed");
        }
        return containsKeyHelper(key);
    }

    /**
     * Helper to containsKey method that helps to compare Pair objects in a linked list
     */
    private boolean containsKeyHelper(KeyType key){
        // calculates the expected index location and retrieves the linked list at that index
        int indexToLookInto = this.calculateHashCode(key);
        LinkedList<Pair> linkedListToLookInto = this.table[indexToLookInto];

        // if the index to look into hasn't been initialized yet, means the index value is null and 
	// so the key will not be present in this location
        if(linkedListToLookInto == null){
            return false;
        }
        // traverses through the linked list comparing the keys of each Pair object
        for(Pair currentPair: linkedListToLookInto){
            if(currentPair.key.equals(key)){
                return true;
            }
        }
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
        if(!containsKey(key)){
            throw new NoSuchElementException("Key not present");
        }
        int indexMappedWithKey = this.calculateHashCode(key);
        LinkedList<Pair> linkedListWithKey = this.table[indexMappedWithKey];

        ValueType valueToReturn = null;
        for(Pair currentPair: linkedListWithKey){
            if(currentPair.key.equals(key)){
                valueToReturn = currentPair.value;
                break;
            }
        }
        return valueToReturn;
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
        if(!this.containsKey(key)){
            throw new NoSuchElementException("key not in table");
        }

        int indexMappedWithKey = this.calculateHashCode(key);
        LinkedList<Pair> linkedListWithKey = this.table[indexMappedWithKey];

        ValueType valueToReturn = null;
        for(Pair currentPair: linkedListWithKey){
            if(currentPair.key.equals(key)){
                valueToReturn = currentPair.value;
                linkedListWithKey.remove(currentPair);
                break;
            }
        }
        return valueToReturn;

    }

    /**
     * Removes all key,value pairs from this collection.
     */
    @Override
    public void clear() {
	for(int index = 0; index < this.table.length; index++){
	    	this.table[index] = null;
	    }

    }

    /**
     * Retrieves the number of keys stored in this collection.
     *
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize() {
        int count = 0;
        for(LinkedList<Pair> currentList: this.table){
            if(currentList != null){
                count+= currentList.size();
            }
        }
        return count;
    }

    /**
     * Retrieves this collection's capacity.
     *
     * @return the size of the underlying array for this collection
     */
    @Override
    public int getCapacity() {
        return this.table.length;
    }

    /**
     * Tests the general implementation of put(). The test checks if Pair objects are created with
     * the expected key-value pairs and added into the table field of the HashtableMap class.
     */
    @Test
    public void testHashtableMapPut(){
        HashtableMap<Integer, String> testHashtable = new HashtableMap<>();
        testHashtable.put(64, "Prateek" );

        assertEquals("Prateek", testHashtable.table[0].getFirst().value,
                "Value didn't match");
        assertEquals(64, testHashtable.table[0].getFirst().key,
                "Key didn't match");

        testHashtable.put(65,"Chand");

        assertEquals("Chand", testHashtable.table[1].getFirst().value,
                "Value didn't match");
        assertEquals(65, testHashtable.table[1].getFirst().key);

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
        
	// gets mapped to index 0
        testHashtable.put(0, "Prateek");

	// also gets mapped to index 0
        testHashtable.put(128, "Chand");

        assertEquals("Chand", testHashtable.table[0].get(1).value, "Value didn't match");
        assertEquals(128, testHashtable.table[0].get(1).key, "Key didn't match");
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
        testHashtable.put(-63, "Prateek");

        assertTrue(testHashtable.containsKey(-63), "Didn't find the key when it was" +
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
        testHashtable.put(0, "Prateek");
        testHashtable.put(64, "Chand");
        testHashtable.put(65, "Apple");
        testHashtable.put(66, "Mango");
        testHashtable.put(67, "Banana");
        testHashtable.put(128, "Dog");

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
            if(currentIndex != null){
		size += currentIndex.size();
	    }
        }

        assertEquals(size, testHashtable.getSize(),"Bruteforce Approach: The returned size didn't" +
                " match");
    }

    /**
     * Tests the implementation of .remove() and .clear() on the complete implementation of a Hashtable.
     */ 
    @Test
    public void testhashtableRemoveandClear(){
	HashtableMap<Integer, String> testHashtable = new HashtableMap<>();

        testHashtable.put(908100100, "Prateek");

        assertEquals("Prateek", testHashtable.remove(908100100), "Incorrect value returned when removing key");
	assertFalse(testHashtable.containsKey(908100100), "The key didn't get removed from the tree");

        testHashtable.put(0, "Prateek");
        // this key-value pair gets mapped to index 0
        testHashtable.put(64, "Chand");
        // this key-value pair gets mapped to index 1
        testHashtable.put(65, "Apple");
        // this key-value pair gets mapped to index 2
        testHashtable.put(66, "Mango");
        // this key-value pair gets mapped to index 3
        testHashtable.put(67, "Banana");
        // this key-value pair gets mapped to index 1
        testHashtable.put(128, "Dog");

	testHashtable.clear();

	assertEquals(0, testHashtable.getSize(), "Table not cleared");

    
    }

    /**
     * Tests the complete implementation of put() when the table requires rehashing. The test checks the 
     * capacity of the table at different load factors and verifies if hashcode are calculated properly 
     * when resizing.
     */ 
    @Test
    public void testhashtablePerformRehash(){
	HashtableMap<Integer, String> testHashtable = new HashtableMap<>(5);

	// this key-value pair gets mapped to index 0
	testHashtable.put(0, "Prateek");
	assertEquals(5, testHashtable.getCapacity(), "Rehashed when load factor was < 0.80");
	assertEquals(0, testHashtable.table[0].get(0).key, "Key didn't match");

        // this key-value pair gets mapped to index 0
        testHashtable.put(5, "Chand");
	assertEquals(5, testHashtable.getCapacity(), "Rehashed when load factor was < 0.80");
	assertEquals(5, testHashtable.table[0].get(1).key, "Key didn't match");


        // this key-value pair gets mapped to index 1
        testHashtable.put(6, "Apple");
	assertEquals(5, testHashtable.getCapacity(), "Rehash when load factor < 0.80");
	assertEquals(6, testHashtable.table[1].get(0).key, "Key didn't match");

	
        // this key-value pair gets mapped to index 7 and the capacity of the table increases to 10
	testHashtable.put(7, "Mango");   
	assertEquals(10, testHashtable.getCapacity(), "Did not rehash when load factor was equal to 0.80");
	assertEquals(7, testHashtable.table[7].get(0).key, "Key 7 didn't get mapped to node 0 of index 7");

     
	// this key-value pair gets mapped to index 0 node 1. Key 0 shouldn't change from index 0 node 0 and 
	// Key 5 needs to be remapped to index 5
        testHashtable.put(10, "Banana");
	assertEquals(10, testHashtable.getCapacity(), "Did not rehash when load factor was > 0.80");
	assertEquals(10, testHashtable.table[0].get(1).key, "Key 10 didn't get mapped to node 1 of index 0");
	assertEquals(0, testHashtable.table[0].get(0).key, "Key 0 got remapped after rehasing");
	assertEquals(5, testHashtable.table[5].get(0).key, "Key 5 didn't get remapped to new index");


        // this key-value pair gets mapped to index 1 node 0. Key 6 needs to be remapped to index 6
        testHashtable.put(11, "Dog");
	assertEquals(10, testHashtable.getCapacity(), "Did not rehash when load factor was > 0.80");
	assertEquals(11, testHashtable.table[1].get(0).key, "Key didn't match");
	assertEquals(6, testHashtable.table[6].get(0).key, "Key 6 didn't get remapped to new index");







    
    }





}
