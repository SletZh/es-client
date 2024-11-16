1.使用spring data jpa+mysql操作源数据  
2.使用spring data elastic操作索引，包括创建索引，文档（包含批量），更新索引，文档（包含批量）以及查询文档信息（包含分页）  
3.基本流程：  
1）从数据库中加载源数据，并把数据同步到elasticsearch  
2）手动拉取源数据内更新内容，并把数据同步到elasticsearch  
3）使用StringQuery+分页查询文档信息  
示例：  
1）{"bool":{"must":[{"term":{"comCode":"948573"}},{"match":{"userName":"用户"}}]}}  
bool ： 组合查询  
term ： 精准匹配  
match ： 模糊匹配，不限制单词顺序  
2）{"bool":{"must":[{"match_phrase":{"comName":{"query":"达团","slop":5}}}]}}  
must : 必须条件  
should ：非必须条件  
match_phrase : 模糊匹配，限制单词顺序  
slop ： 分词间偏移量，越大匹配项越多
