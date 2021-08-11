// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.semantics.types;

public class Array implements Type {
    Type type;

    public Array(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String typeName() {
        return "Array de " + this.type.typeName();
    }

    @Override
    public Boolean match(Type variable) {
        return variable instanceof Array || variable instanceof UnreadValue;
    }
}
