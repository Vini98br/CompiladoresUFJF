/* Jean Paiva: 20153522, Thales Castro: 201535025 */
package lang.semantics.types;

import java.util.HashMap;

public class Data implements Type
{
    String name;
    HashMap<String, Type> symbolTable = new HashMap<>();

    public Data(String name)
    {
        this.name = name;
    }

    public String typeName()
    {
        return this.name;
    }

    public void declareProperty(String propertyName, Type type)
    {
        symbolTable.put(propertyName, type);
    }

    public Type getPropertyType(String propertyName)
    {
        return symbolTable.get(propertyName);
    }

    public Boolean hasProperty(String propertyName)
    {
        return symbolTable.containsKey(propertyName);
    }

    @Override
    public Boolean match(Type variable)
    {
        if (variable instanceof Data) {
            return variable.typeName().equals(this.typeName());
        } else {
            return false;
        }
    }
}
