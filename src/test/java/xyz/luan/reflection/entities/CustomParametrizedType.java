package xyz.luan.reflection.entities;

public class CustomParametrizedType {
  private Id<String> id;
  private MultiParam<String, Integer, Id<Car>> multiParam;

  public static class MultiParam<T1, T2, T3> {}
}
