// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.semantics.types;

public class Char implements Type {
    @Override
    public Boolean match(Type variable) {
        return variable instanceof Char || variable instanceof UnreadValue;
    }

    @Override
    public String typeName() {
        return this.getClass().getSimpleName();
    }
}
