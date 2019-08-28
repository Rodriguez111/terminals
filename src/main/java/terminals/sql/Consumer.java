package terminals.sql;

public interface Consumer<T> {
    void accept(T t) throws Exception;

}
