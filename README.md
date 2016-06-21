# HASCデータを端末位置別に分割するプログラム
HASCデータを「端末の位置(TerminalPosition)別に分割して出力するプログラム

## 使い方
#### IntelliJ IDEAから使う場合
1. このプロジェクトを読み込む
2. dataディレクトリにHASCコーパスのデータを入れる.
3. 実行
4. outputディレクトリが作成され, そこに端末位置別にデータが出力される.

#### コマンドラインから使う場合
1. SplitHASCCorpus_into_TerminalPosition.javaを適当なティレクトリにコピー
2. SplitHASCCorpus_into_TerminalPosition.javaがある階層にcdで移動
3. SplitHASCCorpus_into_TerminalPosition.javaと同ディレクトリにdataディレクトリを用意.
4. dataディレクトリにHASCコーパスのデータを入れる.
5. 以下のコマンドを実行
6. outputディレクトリが作成され, そこに補正されたデータが出力される.

##### コマンド
```
javac SplitHASCCorpus_into_TerminalPosition.java
java SplitHASCCorpus_into_TerminalPosition
```


### データのディレクトリ構成
```
.  
data  
├── 1_stay  
│  ├── person0001  
│  │   ├── HASCXXXXXX-acc.csv  
│  │   ├── HASCXXXXXX-gyro.csv  
│  │   ├── HASCXXXXXX-mag.csv  
│  │   └── ...  
│  ├── person0002  
│  └── ...  
├── 2_walk  
│　├── person0001  
│　│   ├── HASCXXXXXX-acc.csv  
│　│   ├── HASCXXXXXX-gyro.csv  
│　│   ├── HASCXXXXXX-mag.csv  
│　│   └── ...  
│　├── person0002  
│　└── ...  
└── ...
```


### 　
Developed by icchi  
2016/06/21
