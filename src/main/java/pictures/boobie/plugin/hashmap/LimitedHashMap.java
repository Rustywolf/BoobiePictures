package pictures.boobie.plugin.hashmap;

import java.util.ArrayList;
import java.util.HashMap;

public class LimitedHashMap<K, V> extends HashMap<K, V> {
    
    private final ArrayList<K> values;
    private final int sizeLimit;
    
    public LimitedHashMap(int sizeLimit) {
        this.values = new ArrayList<>();
        this.sizeLimit = sizeLimit;
    }
    
    @Override
    public V put(K key, V value) {
        if (values.size() >= sizeLimit) {
            this.remove(values.remove(0));
        }
        
        values.add(key);
        return super.put(key, value);
    }
    
    @Override
    public V remove(Object key) {
        this.values.remove(key);
        return super.remove(key);
    }
    
}
