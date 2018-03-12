# CS388-NLP-HW1
HW1 of the Natural Language Processing course: N-gram Language Models

## Introduction

Language models are probabilistic models that assign a probability to a sentence of being part of that language. N-grams are a kind of language models that give probability `P(wn|wn−1)` for a word wn given the context `w1...wn−1`. Traditionally, n-gram models 1 are computed in the forward direction i.e. the left n − 1 words are used to assign the probability of `wn`. In this experiment we compare forward bigram models to backward and bidirectional bigram models on 3 different datasets (of varying complexities) – Atis, WSJ and Brown.

## Usage

### Compiling the code

~~~~
$ javac -cp . *.java
~~~~

### Running the models

~~~~
$ java -cp . BigramModel /path/to/data/ 0.1
$ java -cp . BackwardBigramModel /path/to/data/ 0.1
$ java -cp . BidirectionalBigramModel /path/to/data/ 0.1
~~~~

## Results

Based on the results it is evident that the `BDBG` model performs significantly better than both `BG` and `BBG`. This was expected because the `BDBG` models a *"sort-of"* trigram model without the inherent drawbacks of a trigram model. Trigram models generally suffer from extremely sparse data and need lots of data to be useful. By combining both `BG` and `BBG` models we are able to utilise the context on both the sides of the word (`wn`) and hence, get a better word perplexity value across the board.

| Dataset `split_fraction` = 0.1 |       | Perplexity `BG` | Perplexity `BBG` | Word Perplexity `BG` | Word Perplexity `BBG` | Word Perplexity `BDBG` |
|:------------------------------:|:-----:|:---------------:|:----------------:|:--------------------:|:---------------------:|:----------------------:|
|              Atis              | train |    9.043e+00    |     9.013e+00    |       1.059e+01      |       1.164e+01       |        7.235e+00       |
|                                |  test |    1.934e+01    |     1.936e+01    |       2.405e+01      |       2.716e+01       |        1.270e+01       |
|               WSJ              | train |    7.427e+01    |     7.427e+01    |       8.889e+01      |       8.666e+01       |        4.651e+01       |
|                                |  test |    2.197e+02    |     2.195e+02    |       2.751e+02      |       2.664e+02       |        1.261e+02       |
|              Brown             | train |    9.352e+01    |     9.351e+01    |       1.134e+02      |       1.108e+02       |        6.147e+01       |
|                                |  test |    2.313e+02    |     2.312e+02    |       3.107e+02      |       2.997e+02       |        1.675e+02       |