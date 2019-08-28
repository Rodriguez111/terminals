package terminals.sql;

public interface Func<T, R> {
    R apply(T t) throws Exception;
}
