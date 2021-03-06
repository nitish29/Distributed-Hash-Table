# Distributed Hash Table
***
<!---=========================================================--->

* Distributed Hah Table is a Simple Key-Value storage based on Chord design.

* The Simplified version of Chord covers three things:
    * ID space partitioning/re-partitioning.
    * Ring based routing
    * Node joins

* SHA-1 hash function is used to lexically arrange nodes in a ring and find the location for a particular key to be stored.

* Each node maintains a successor and predecessor pointer for nodes in the ring.

* Content Provider is File Based key-value storage where:
    - The first column name is “key” and is used to store all keys. 
    - The second column is “value” and is used to store all values associated with corresponding keys.


<!---* However, Node failures, concurrent node-joins and finger tables are not implemented. --->


### PTest

- Inserts 50 arbitrary key-value pairs in the DHT and queries to check if they exist and returns success/failure messages.


### LDump

- When touched, this button dumps and displays all the <key, value> pairs stored in local partition of the node.


### GDump

- When touched, this button dumps and displays all the <key, value> pairs stored in the entire DHT.


## References

[1] Read about Chord [here](http://conferences.sigcomm.org/sigcomm/2001/p12-stoica.pdf)

[2] Single best resource on Android, [Android dev](http://developer.android.com)

[3] Link to Chord repository on Github, [here](https://github.com/sit/dht/wiki)

