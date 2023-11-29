# Router-Simulation

This Java program simulates a simple network with devices connecting to a router with a limited number of Wi-Fi connections. Here's a brief overview of the key components and functionalities:

1. **Semaphore Class:**
   - Represents a semaphore, a synchronization primitive, used for controlling access to the limited Wi-Fi connections.
   - Provides methods for waiting (`_wait`) and signaling (`signal`) to manage access to the shared resource (Wi-Fi connections).
   - The `_wait` method logs device arrival and waiting, as well as when a device occupies a connection.

2. **Router Class:**
   - Manages the available Wi-Fi connections using a semaphore.
   - Maintains a list of available connection slots.
   - Provides methods to get and set thread numbers, connect a device, perform activities, and disconnect a device.
   - Logs device activities and disconnections in an "Output.txt" file.

3. **Device Class (extends Thread):**
   - Represents a device that connects to the network.
   - Each device runs in a separate thread and performs connection, activity, and disconnection actions.
   - Device attributes include name, type, router reference, and thread number.
   - Logs device activities, such as performing online activity and logging out, in the "Output.txt" file.

4. **Network Class:**
   - Manages the overall network, including creating a router and an array of devices.
   - Reads the number of Wi-Fi connections and devices from user input.
   - Initializes devices with user-provided names and types.
   - Starts the network by initiating the threads for each device.

5. **Main Method:**
   - Takes user input for the number of Wi-Fi connections and devices.
   - Creates a network object and starts the network.

6. **File Output:**
   - All log information, including device activities and connections, is written to an "Output.txt" file.

It's important to note that there are some issues in the code:
- In the `Device` constructor, the router is instantiated twice, and the second instantiation overwrites the first one.
- The `while (input.matches(""))` loop in the `Network` class seems redundant and may result in an infinite loop. It appears to be attempting to ensure that the user provides non-empty input for device names and types.
- There are no exception-handling mechanisms in case of issues with file writing.

Additionally, some improvements can be made for better readability, error handling, and resource management.
