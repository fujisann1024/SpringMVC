# テスト準備



## 【1】resourceフォルダの作成

1. プロジェクトのsrc/test配下にresourcesフォルダを作成
2. プロジェクトを右クリック→プロパティ
3. Javaのビルドパス→ソースで「フォルダを追加」
4. 1で作成したフォルダを選択→適用して閉じる

# テスト


 - 参考
   - [AAAパターン](https://qiita.com/nnyyaaaann/items/1329758bbe3d02591caa)
   - [JUnit徹底活用！テストの質を高める上級テクニック](https://moritama321.hatenablog.com/entry/2025/06/26/233140)
   - [JUnit5 + Mockitoをつかったテストコードの書き方](https://qiita.com/n_slender/items/fb4c32716902baac36a1)


## テストパターン

<details>

<summary>AAA／Given-When-Then（読みやすい定番）</summary>

```java
@Test @DisplayName("合計が正しく計算される")
void sum() {
  // Arrange
  var calc = new Calculator();

  // Act
  int actual = calc.sum(2, 3);

  // Assert
  assertThat(actual).isEqualTo(5);
}
```

</details>


<details>

<summary>例外検証</summary>

```java
@Test
void throwsWhenInvalid() {
  assertThatThrownBy(() -> service.load(null))
      .isInstanceOf(IllegalArgumentException.class);
}
}
```

</details>


<details>

<summary>まとめ検証</summary>

```java
@Test
void assertAllExample() {
  var u = new User("Taro", 20);
  assertAll(
    () -> assertThat(u.getName()).isEqualTo("Taro"),
    () -> assertThat(u.getAge()).isGreaterThanOrEqualTo(18)
  );
}
```

</details>


<details>

<summary>Parameterized Test</summary>

```java
@ParameterizedTest
@CsvSource({
  "2,3,5",
  "10,5,15"
})
void sumParam(int a, int b, int expected) {
  assertThat(new Calculator().sum(a, b)).isEqualTo(expected);
}
```

</details>


<details>

<summary>Enum / Method ソース</summary>

```java
@ParameterizedTest
@EnumSource(ChronoUnit.class)
void allUnits(ChronoUnit unit) { assertThat(unit).isNotNull(); }
```

</details>


<details>

<summary>Nested</summary>

```java
@Nested @DisplayName("ログイン機能")
class Login {
  @Test @DisplayName("成功")
  void ok() {}
  @Test @DisplayName("失敗")
  void ng() {}
}
```

</details>


<details>

<summary>TestInstance ライフサイクル</summary>

```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Suite { @BeforeAll void init() {} } // static不要
```

</details>


<details>

<summary>Nested</summary>

```java
@Nested @DisplayName("ログイン機能")
class Login {
  @Test @DisplayName("成功")
  void ok() {}
  @Test @DisplayName("失敗")
  void ng() {}
}
```

</details>