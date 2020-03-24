package coral.base.data;

import wfc.service.util.OrderedHashSet;

public interface MetadataService {

	public MetadataTable extractMetadataFromDatabase(String tableName);

	public void applyMetadataToDatabase(MetadataTable mt);

	public OrderedHashSet<String> getPkList(MetadataTable mt);

}