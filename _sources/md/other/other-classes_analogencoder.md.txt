# AnalogEncoder

## Why use it?

Using analog encoders is incredibly necessary in FIRST, but uses operations and math to get the current position.

## How to use it

```java
public class AnalogEncoderOpMode extends LinearOpMode {
    AnalogEncoder analogEncoder = null;
    List<Operation> operations = Arrays.asList(new Operation(Operand.DIVIDE, 3.0),
        new Operation(Operand.MULTIPLY, 2.0)); 
        // operations to be performed on the encoder value
    
    @Override
    public void runOpMode() {
        analogEncoder = new AnalogEncoder("encoder",0.0,operations);
        analogEncoder.init(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            double currentPose = analogEncoder.getCurrentPosition(); // get the current position of the encoder
        }
    }
}
```



The operations follow their order, aka if you do DIVIDE then ADD, it will happen in that order, then getCurrentPosition follows that and will get the degrees of rotation

