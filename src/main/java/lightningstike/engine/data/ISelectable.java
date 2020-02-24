package lightningstike.engine.data;

public interface ISelectable<S> {
    S getSelectedObject();

    void setSelectedObject(S obj);
}
