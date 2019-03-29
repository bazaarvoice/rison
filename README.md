Rison Parser and Generator for Jackson
======================================

A plugin for the [Jackson streaming JSON processor v2.9.x](http://wiki.fasterxml.com/JacksonHome) that adds
support for reading and writing JSON objects in the [Rison](http://mjtemplate.org/examples/rison.html)
serialization format.  Support for Jackson [v2.1.x](https://github.com/bazaarvoice/rison/tree/rison-2.1.1), [v2.0.x](https://github.com/bazaarvoice/rison/tree/rison-2.0.1),
and [v1.9.x](https://github.com/bazaarvoice/rison/tree/rison-1.2) is also available.

Rison expresses the exact same data structures as JSON, but is much more compact and readable than JSON
when encoded in a URI.

See https://github.com/Nanonid/rison for more information about Rison.

Usage
-----

In your code, create a `RisonFactory` instead of a `JsonFactory`.  Then read and write objects just
as you do regular JSON objects.  All the Jackson mapper facilities are available with Rison.

```java
import com.bazaarvoice.jackson.rison.RisonFactory;

ObjectMapper RISON = new ObjectMapper(new RisonFactory());

String string = "(a:0,b:foo,c:'23skidoo')";
Map map = RISON.readValue(json, Map.class);
...
RISON.writeValueAsString(map);
```

O-Rison
-------

If you know the value you wish to serialize is always an object, you can configure the `RisonFactory`
to omit the containing `(` and `)` characters.

```java
ObjectMapper O_RISON = new ObjectMapper(new RisonFactory().
    enable(RisonGenerator.Feature.O_RISON).
    enable(RisonParser.Feature.O_RISON));

String string = "a:0,b:foo,c:'23skidoo'";
Map map = O_RISON.readValue(json, Map.class);
...
System.out.println(O_RISON.writeValueAsString(map));
```


A-Rison
-------

If you know the value you wish to serialize is always an array, you can configure the `RisonFactory`
to omit the containing `!(` and `)` characters.

```java
ObjectMapper A_RISON = new ObjectMapper(new RisonFactory().
    enable(RisonGenerator.Feature.A_RISON).
    enable(RisonParser.Feature.A_RISON));

String string = "item1,item2,item3";
List list = A_RISON.readValue(json, List.class);
...
System.out.println(A_RISON.writeValueAsString(list));
```


Other Implementations
---------------------
See the [Rison](http://mjtemplate.org/examples/rison.html) page for implementations in JavaScript,
Python and Ruby.
