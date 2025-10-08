# Black and White Dot Detection (bow)

Dot detection has often been used for things like PowerPlay (2022-2023). Where there were three detections that were randomized. This was with the orientation of a cone and would return a 1, 2, or 3 so you could get extra points for detecting the position.&#x20;

This one, like the moa detection, uses a builder to determine the parameters for the detection.

The first parameter is a dotColor. Which is an enum for either BLACK, or WHITE. Depending on the color you want to look for.

The second parameter is a DotDetectionBuilder. This takes a Rectangle, as seen in moa, is the rectangle in which the program will "look" or "focus". Then it takes a minArea and maxArea that will further narrow down the detections to make sure it always sees the dots correctly.

```java
BlackAndWhiteDotDetector blackAndWhiteDotDetector = new BlackAndWhiteDotDetector(
            DotColor.BLACK, // could be changed to WHITE
            new DotDetectionBuilder(
                    new Rect(50, 50, 100, 75), // used to narrow down detection to a specific area
                    10.0, 100.0 // these will need to be tuned to your specific "dots"
            )
    );
```
