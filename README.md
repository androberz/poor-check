[license]: https://opensource.org/licenses/MIT "The MIT License (MIT)"
# Poor Check
The plugin warns of possible Java code poor usage.  
Supports Java 8 - 11 versions.  

Covered usage cases:
* Not overridden ```toString()``` call on object;
* Not overridden ```equals()``` call on object;
* ```java.util.Optional```  ```isPresent()``` always returns ```true```;
* ```java.util.Optional```  ```isEmpty()``` always returns ```false```;
* Not overridden ```equals()```/```hashCode()``` methods for object, that is used as a key in hash structures.
  Includes checks for ```java.util.stream.Collectors```  ```toMap()``` collector;  
  Also checks usage of Guava's ```ImmutableSet``` and ```ImmutableMap```.
* Added support for Lombok's ```@EqualsAndHashCode```.

## How it works
Highlights the code that contains possible weak usage.  
When run (ALT+ENTER) on the highlight, provide an ability to generate missing code.   

## How to enable and examples
Go to plugins marketplace in the Settings, download and enable the plugin.


## Requirements 
Supported Intellij Idea versions: 181.* - 203.*  
JDK 8 - 11

## Pull Requests
The project is opened for any enhancement. Your help is much appreciated. Please, feel free to open your pull requests.

## Created By
[Andrey Berezin](https://www.linkedin.com/in/andrey-berezin-16ba5985/)

## License
Poor Check is released under the MIT License (MIT) (https://opensource.org/licenses/MIT)