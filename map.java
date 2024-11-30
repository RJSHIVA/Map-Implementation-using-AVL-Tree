import java.util.HashMap;

class Map {
    // Static root of the binary search tree to store key-value pairs
    private static Map root = null;

    // Key and value
    int first, second, depth;

    // Left, right, and parent nodes for the binary search tree
    Map left, right, par;

    // Search for a key in the tree
    private int search(int first) {
        Map temp = root;

        // Traverse the tree until the key is found or a leaf node is reached
        while (temp != null && temp.first != first) {
            if (first < temp.first) {
                // Move to the left if the key is smaller
                temp = temp.left;
            } else {
                // Move to the right if the key is greater
                temp = temp.right;
            }
        }

        // If the key is found, return the corresponding value
        if (temp != null) {
            return temp.second;
        }

        // If the key is not found, return 0
        return 0;
    }

    // Create a new node with the given key
    private Map create(int first) {
        Map newNode = new Map();
        newNode.first = first;
        newNode.second = 0;
        newNode.left = null;
        newNode.right = null;
        newNode.par = null;

        // Depth of a new node is set to 1 (differentiating no child from having a child)
        newNode.depth = 1;
        return newNode;
    }

    // Perform a right rotation around node 'x'
    private void rightRotation(Map x) {
        Map y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.par = x;
        }
        if (x.par != null && x.par.right == x) {
            x.par.right = y;
        } else if (x.par != null && x.par.left == x) {
            x.par.left = y;
        }
        y.par = x.par;
        y.right = x;
        x.par = y;
    }

    // Perform a left rotation around node 'x'
    private void leftRotation(Map x) {
        Map y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.par = x;
        }
        if (x.par != null && x.par.left == x) {
            x.par.left = y;
        } else if (x.par != null && x.par.right == x) {
            x.par.right = y;
        }
        y.par = x.par;
        y.left = x;
        x.par = y;
    }

    // Helper method to balance the tree
    private void helper(Map node) {
        // If left skewed
        if (depthf(node.left) - depthf(node.right) > 1) {
            if (depthf(node.left.left) > depthf(node.left.right)) {
                node.depth = Math.max(depthf(node.right) + 1, depthf(node.left.right) + 1);
                node.left.depth = Math.max(depthf(node.left.left) + 1, depthf(node) + 1);
                rightRotation(node);
            } else {
                node.left.depth = Math.max(depthf(node.left.left) + 1, depthf(node.left.right.left) + 1);
                node.depth = Math.max(depthf(node.right) + 1, depthf(node.left.right.right) + 1);
                node.left.right.depth = Math.max(depthf(node) + 1, depthf(node.left) + 1);
                leftRotation(node.left);
                rightRotation(node);
            }
        } else if (depthf(node.left) - depthf(node.right) < -1) {
            // If right skewed
            if (depthf(node.right.right) > depthf(node.right.left)) {
                node.depth = Math.max(depthf(node.left) + 1, depthf(node.right.left) + 1);
                node.right.depth = Math.max(depthf(node.right.right) + 1, depthf(node) + 1);
                leftRotation(node);
            } else {
                node.right.depth = Math.max(depthf(node.right.right) + 1, depthf(node.right.left.right) + 1);
                node.depth = Math.max(depthf(node.left) + 1, depthf(node.right.left.left) + 1);
                node.right.left.depth = Math.max(depthf(node) + 1, depthf(node.right) + 1);
                rightRotation(node.right);
                leftRotation(node);
            }
        }
    }

    // Balance the tree after insertion
    private void balance(Map node) {
        while (node != root) {
            int d = node.depth;
            node = node.par;
            if (node.depth < d + 1) {
                node.depth = d + 1;
            }
            if (node == root && Math.abs(depthf(node.left) - depthf(node.right)) > 1) {
                if (depthf(node.left.left) > depthf(node.left.right)) {
                    root = node.left;
                } else {
                    root = node.left.right;
                }
                helper(node);
                break;
            }
            helper(node);
        }
    }

    // Get the depth of the subtree at the node
    private int depthf(Map node) {
        if (node == null) {
            return 0;
        }
        return node.depth;
    }

    // Insert a new key into the tree
    private Map insert(int first) {
        Map newNode = create(first);

        // If tree is empty, create root
        if (root == null) {
            root = newNode;
            return root;
        }

        Map temp = root, prev = null;
        while (temp != null) {
            prev = temp;
            if (first < temp.first) {
                temp = temp.left;
            } else if (first > temp.first) {
                temp = temp.right;
            } else {
                return temp; // Return existing node if key is found
            }
        }

        if (first < prev.first) {
            prev.left = newNode;
        } else {
            prev.right = newNode;
        }
        newNode.par = prev;

        // Balance the tree after insertion
        balance(newNode);
        return newNode;
    }

    // Overload [] operator for insertion and accessing key-value pairs
    public int get(int key) {
        return search(key); // Search for the key
    }

    public void put(int key, int value) {
        insert(key).second = value; // Insert or update the key-value pair
    }

    public static void main(String[] args) {
        // Create a new map instance
        Map map = new Map();

        // Test Case 1: Basic insertions
        map.put(132, 3); // Insert key 132 with value 3
        map.put(34, 5);  // Insert key 34 with value 5
        map.put(42, 7);  // Insert key 42 with value 7
        map.put(-83, 4); // Insert key -83 with value 4
        map.put(66, 9);  // Insert key 66 with value 9
        map.put(197, 8); // Insert key 197 with value 8
        map.put(-2, -88);// Insert key -2 with value -88

        // Test Case 2: Update existing key
        map.put(42, 55); // Update key 42 with a new value 55

        // Test Case 3: Retrieve existing keys
        System.out.println("Key: Value");
        System.out.println("Expected: 3, Actual: " + map.get(132)); // Test key 132
        System.out.println("Expected: 5, Actual: " + map.get(34));  // Test key 34
        System.out.println("Expected: 55, Actual: " + map.get(42)); // Test updated key 42
        System.out.println("Expected: 4, Actual: " + map.get(-83)); // Test key -83

        // Test Case 4: Retrieve non-existing keys
        System.out.println("Expected: 0, Actual: " + map.get(-34)); // Non-existing key
        System.out.println("Expected: 0, Actual: " + map.get(60));  // Non-existing key
        System.out.println("Expected: 0, Actual: " + map.get(56));  // Non-existing key

        // Test Case 5: Negative keys
        System.out.println("Expected: -88, Actual: " + map.get(-2)); // Test key -2 with negative value

        // Test Case 6: Check tree balancing
        map.put(1, 10);   // Insert key 1 to test balancing
        map.put(500, 20); // Insert a large key to test balancing
        map.put(-500, 30); // Insert a small key to test balancing
        System.out.println("Expected: 10, Actual: " + map.get(1));   // Confirm new key exists
        System.out.println("Expected: 20, Actual: " + map.get(500)); // Confirm new key exists
        System.out.println("Expected: 30, Actual: " + map.get(-500)); // Confirm new key exists

        // Test Case 7: Edge case - empty map
        Map emptyMap = new Map();
       
