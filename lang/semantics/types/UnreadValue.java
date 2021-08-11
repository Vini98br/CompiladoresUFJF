// Arthur Dornelas: 201735004, Vinicius Soranço: 201735003

package lang.semantics.types;

public class UnreadValue implements Type {
    @Override
    public Boolean match(Type variable) {
        return true;
    }

    @Override
    public String typeName() {
        return this.getClass().getSimpleName();
    }
}
