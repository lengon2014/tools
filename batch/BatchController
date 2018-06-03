
//示例代码，

@Autowired
private BatchDao batchDao;


public void index(){
	List<User> users = new ArrayList<>();
	batchDao.insertAll(users);
	
	List<User> users = new ArrayList<>();
	batchDao.insertAll(users,"my_user", Arrays.asList("name","age"));
}