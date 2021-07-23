/* Jean Paiva: 20153522, Thales Castro: 201535025 */
package lang.semantics.types;

public class UnreadValue implements Type
{
    @Override
    public Boolean match(Type variable)
    {
        return true;
    }

    @Override
    public String typeName()
    {
        return this.getClass().getSimpleName();
    }
}
