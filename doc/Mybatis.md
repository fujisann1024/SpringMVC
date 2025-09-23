# ライブラリ

## Mybatis

 - 参考
   - [Mybatisの書き方](https://qiita.com/5zm/items/e1faac61a1d00998fb1f)
   - [MybatisでExcelを用いたテスト](https://qiita.com/asachan/items/9d8cbb1965633531c868)

### 記述ルール
 
 - Mapperファイルはインターフェースと同一パッケージに配置すること
 - Mapperファイルはインターフェースと同一のファイル名で作成すること
 - Mapperファイルとインターフェースを紐付けるため、namespace属性でインターフェースのFQCNを指定すること
 - 





```xml

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.mybatis.example.BlogMapper">
  <select id="selectBlog" resultType="Blog">
    select * from Blog where id = #{id}
  </select>
</mapper>

```