# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: the longest length of a hexagon is (3 * s - 2)
. The number of left empty space is (maxLen - num) / 2. Num is the number of
tiles in a line. That's it. Simple math.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: Maybe use some math and loop?

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: The helper function to draw the little pieces of the target.

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: They are similar because they're all consist of wall and empty space.
The difference is their shape.
