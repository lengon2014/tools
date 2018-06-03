package batch;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;


public interface BatchDao {

	@InsertProvider(type = BatchDAOProvider.class, method = "insertAll")
	void insertAll(@Param("list") List<?> batchs);

	@InsertProvider(type = BatchDAOProvider.class, method = "insertAllByTable")
	void insertAllWithTable(@Param("list") List<?> batchs, @Param("table") String tableName,
			@Param("keys") List<String> keys);

}
