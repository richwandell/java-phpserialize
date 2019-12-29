# PHP Serialize
PHP Serialize is a library that can serialize Java POJO's into PHP POPO's as well as deserialize PHP POPO's into 
java POJO's. The spec used for this project was taken from [PHP Internals Book]. Unit tests are not 
comprehensive but I did try to cover a lot of use cases. 

## Usage
In order for Java POJO's and PHP POPO's to work via the PHP serialization mechanism, we first need to make 
sure that the PHP class and the Java class are compatible with one another. The following example shows a PHP class 
and a Java class that are compatible. 
```php
namespace wandell\Example\Popo;

//PHP Class
class Popo
{
    private $property1;

    private $property2;

    public function __construct($property1, $property2)
    {
        $this->property1 = $property1;
        $this->property2 = $property2;
    }
}
```
```java
//java class
import com.wandell.PHP.PHPClass;

//annotate with the PHPClass annotation
@PHPClass("wandell\\Example\\Popo\\Popo")
public class Pojo {
    //types will be validated when deserializing
    private int property1;

    private int property2;

    //empty constructor is required
    public Pojo() {
        super();
    }
    
    public Pojo(int property1, int property2) {
        this.property1 = property1; 
        this.property2 = property2;
    }
}
```

If we have the following PHP serialized string created from a `Popo` object.
     
    O:25:"wandell\Example\Popo\Popo":2:{s:36:"\0wandell\Example\Popo\Popo\0property1";i:10;s:36:"\0wandell\Example\Popo\Popo\0property2";i:11;}
*note: \0 octal escape sequence is used to represent the null byte character* 

* Deserialize this string in Java
    ```java
    //create an unserializer object and give it a list of classes that are annotated with PHPClass
    Unserializer un = new Unserializer(new Class[]{Pojo.class});
    Pojo pojo = (Pojo)un.unserialize(PHP_OBJECT_STRING);
    ```
* Serialize a new Pojo
    ```java
    Pojo pojo = new Pojo(10, 11);
    //create a serializer
    Serializer serializer = new Serializer();
    //get the serialized string
    String serialized = serializer.serialize(pojo);
    ```



[PHP Internals Book]: <http://www.phpinternalsbook.com/php5/classes_objects/serialization.html>