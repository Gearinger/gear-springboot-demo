## 使用标准的 word2vec 模型

从以下地址下载模型：

https://github.com/Embedding/Chinese-Word-Vectors/tree/master

Word2Vec w2v = new Word2Vec();

w2v.loadGoogleModel2() 加载模型

w2v.getWordVector() 获取词向量

## 自己训练模型

train.Train.learnFile() 加载本地语料进行训练

Word2Vec w2v = new Word2Vec();

w2v.loadJavaModel() 加载模型

w2v.getWordVector() 获取词向量