/* Jean Paiva: 20153522, Thales Castro: 201535025 */
package lang.semantics.types;

public class Float implements Type
{
    @Override
    public Boolean match(Type variable)
    {
        return variable instanceof Float || variable instanceof UnreadValue;
    }

    @Override
    public String typeName()
    {
        return this.getClass().getSimpleName();
    }
}
