/* Jean Paiva: 20153522, Thales Castro: 201535025 */
package lang.semantics.types;

public interface Type
{
    Boolean match(Type variable);
    String typeName();
}
