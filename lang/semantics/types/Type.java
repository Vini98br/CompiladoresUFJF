// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.semantics.types;

public interface Type {
    Boolean match(Type variable);

    String typeName();
}
