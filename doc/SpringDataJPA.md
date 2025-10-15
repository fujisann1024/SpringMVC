# CrudRepository

```java

public interface CrudRepository<T, ID> extends Repository<T, ID> {

  <S extends T> S save(S entity); // 	指定されたエンティティを保存

  Optional<T> findById(ID primaryKey); // 	指定された ID で識別されるエンティティを返す

  Iterable<T> findAll(); // すべてのエンティティを返

  long count(); // 件数

  void delete(T entity); //指定のエンティティを削除

  boolean existsById(ID primaryKey); //	指定された ID のエンティティが存在するかどうか

  // … more functionality omitted.
}
```
