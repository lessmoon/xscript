native<extension.reflection>{
    "Root":struct Object;
    struct StructType{
        def this();
        def Object newInstance(Object[] args);
        def bool isChildOf(StructType t);
        def bool equals(StructType t);
        def string getName();
    };

    "Root":struct Object{
        def this();
        def StructType getType();
    };
}