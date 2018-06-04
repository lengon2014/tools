
private static final List<String> MAPPER = Arrays.asList("id", "name", "age");


//示例代码，当前兼容了excel2007和2010的版本
public void index(){

    
	File file = new File("test.xlsx");
	try {
			Workbook workbook = null;
			try {
				workbook = new XSSFWorkbook(file.getInputStream());
			} catch (Exception ex) {
				workbook = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));
			}

			Sheet sheet = workbook.getSheetAt(0);
			int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

			ExcelRow2BeanUtils rowUtils = new ExcelRow2BeanUtils();
			for (int j = 0; j < physicalNumberOfRows; j++) {
				Row row = sheet.getRow(j);
				int cellNumber = row.getPhysicalNumberOfCells();
				User user = rowUtils.getBeanFromExcelRow(row, MAPPER, Material.class);
			}

		} catch (IOException e) {
			LogUtils.error("fail to read excel " + e.getMessage());
		}
}