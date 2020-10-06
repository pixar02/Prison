package tech.mcprison.prison.shops.data;

import tech.mcprison.prison.shops.PrisonShops;
import tech.mcprison.prison.sorting.PrisonSorter;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PrisonSortableShops extends PrisonSorter {

    @Override
    public Set<Shop> getSortedSet() {
        TreeSet<Shop> shops = new TreeSet<>( new PrisonSortableShops.PrisonSortComparableShops() );

        List<Shop> unsortedShops = PrisonShops.getInstance().getShopManager().getShops();

        shops.addAll( unsortedShops );
        return shops;
    }

    public class PrisonSortComparableShops
            implements Comparator<Shop> {

        @Override
        public int compare( Shop m1, Shop m2 )
        {
            int results = 0;

            if ( m1 == null ) {
                results = -1;
            }
            else if ( m2 == null ) {
                results = 1;
            }
            else {
                results = m1.getName().toLowerCase().compareTo( m2.getName().toLowerCase() );
            }

            return results;
        }

    }
}
