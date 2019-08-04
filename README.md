# ohtayo-commons-java
The ohtayo-commons-java provides basic classes for developing mathematical programming projects in java languages.

## How to use

### Random - jp.ohtayo.commons.random
    // Create instance Random() class.
	Random random = new Random(Random.SFMT);
    // Get random number using SFMT(period is 2^(19937-1)).
 	double hoge = random.nextDouble(); 

### Matrix - jp.ohtayo.commons.math
    // Create matrix A from double arrays.
    double[][] temp = {{-1,0},{3,4}};
    Matrix A = new Matrix(temp);
    // Create size specified matrix B.
    Matrix B = new Matrix(2, 2);
    // Set value.
    int row = 0;
    int column = 1;
    double value = 2;
    B.set(row, column, value);
    
    // Calculate matrix.
    Matrix C = A.plus(B);
    
	// Show inner values in matrix A.
    System.out.println("A = ");
    System.out.println(A.toString());
    
### Logging - jp.ohtayo.commons.log
    // Output log file to the project root directory.
    String message = "message";
    Logging.logger.severe(message);

### ConfigBase - jp.ohtayo.commons.io
#### Create config class.
    import jp.ohtayo.commons.io.ConfigBase;
    public class Config extends ConfigBase {
    	public String a;
    }    		
#### Create config file(XML).
    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
    <properties>
    <comment>comment</comment>
        <entry key="a">10</entry>
    </properties>
#### Read config file.
    Config config = new Config();
    config.read("config.xml");
    System.out.println(config.a);	

### More detail
see jp.ohtayo.commons.samples.

## Licence
The ohtayo-commons-java is open-sourced software licensed under the [MIT license](https://github.com/ohtayo/commons-java/blob/master/LICENSE).
