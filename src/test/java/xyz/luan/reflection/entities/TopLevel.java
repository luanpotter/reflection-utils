package xyz.luan.reflection.entities;

public class TopLevel {

    int one;

    public enum EnumClass {
        FOO, BAR;
    }

    public static class Static {
        public class Inner {
            int one;
            int two;
        }
    }
    
    public class Inner {
        int one;
    }

    public class InnerChild extends Inner {
        int two;
    }

    public static class StaticChild extends Static {
        int one;
    }
}
