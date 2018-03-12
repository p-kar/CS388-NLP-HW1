import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Pratyush Kar
 * A bidirectional bigram language model that models the generation of sentences
 * from both left to right and right to left.
 */

public class BidirectionalBigramModel {
	/**
	 * Instance of BigramModel that processes sentences left-to-right
	 */
	private BigramModel bigramModel;
	/**
	 * Instance of BackwardBigramModel that processes sentences right-to-left
	 */
	private BackwardBigramModel backwardBigramModel;
	/**
     * Interpolation weight for BigramModel
     */
    public double lambda1 = 0.5;

    /**
     * Interpolation weight for BackwardBigramModel
     */
    public double lambda2 = 0.5;
	
	/**
     * Initialize model with both the BigramModel and
     * BackwardBigramModel. Both models will be empty and
     * have to be trained separately.
     */
	public BidirectionalBigramModel() {
		bigramModel = new BigramModel();
		backwardBigramModel = new BackwardBigramModel();
	}
	
	/** Train the model on a List of sentences represented as
     *  Lists of String tokens */
    public void train (List<List<String>> sentences) {
		// Accumulate unigram and bigram counts in maps
		bigramModel.train(sentences);
		backwardBigramModel.train(sentences);
    }
    
    /** Like sentenceLogProb (of BigramModel and BackwardBigramModel)
     * but excludes predicting end-of-sentence (<S>) and 
     * start-of-sentence (</S>) when computing prob */
    public double sentenceLogProb (List<String> sentence) {
    		/* last element in list corresponds to </S> */
		double[] forwardTokenProbs = bigramModel.sentenceTokenProbs(sentence);
		/* last element in list corresponds to <S> */
		double[] backwardTokenProbs = backwardBigramModel.sentenceTokenProbs(sentence);
		double logProb = 0.0;
		int i = 0;
		int j = backwardTokenProbs.length - 2;
		while (i < forwardTokenProbs.length - 1 && j >= 0) {
			logProb += Math.log(lambda1 * forwardTokenProbs[i] + lambda2 * backwardTokenProbs[j]);
			i++;
			j--;
		}
//	    System.out.println(logProb + " : " + bigramModel.sentenceLogProb2(sentence) + 
//	    		" : " + backwardBigramModel.sentenceLogProb2(sentence) + " : " + sentence);
		return logProb;
    }
    
    /** Use sentences as a test set to evaluate the model. Prints out perplexity
     *  of the model for this test data excluding the start of sentence (<S>)
     *  and end of sentence (</S>) tokens. */
    public void test (List<List<String>> sentences) {
		double totalLogProb = 0;
		double totalNumTokens = 0;
		for (List<String> sentence : sentences) {
		    totalNumTokens += sentence.size();
		    double sentenceLogProb = sentenceLogProb(sentence);
		    //	    System.out.println(sentenceLogProb + " : " + sentence);
		    totalLogProb += sentenceLogProb;
		}
		double perplexity = Math.exp(-totalLogProb / totalNumTokens);
		System.out.println("Word Perplexity = " + perplexity );
    }
    
    public static int wordCount(List<List<String>> sentences) {
        int wordCount = 0;
        for (List<String> sentence : sentences) {
            wordCount += sentence.size();
        }
        return wordCount;
    }
    
    /** Train and test a bidirectionalBigram model.
     *  Command format: "nlp.lm.BiderectionalBigramModel [DIR]* [TestFrac]" where DIR 
     *  is the name of a file or directory whose LDC POS Tagged files should be 
     *  used for input data; and TestFrac is the fraction of the sentences
     *  in this data that should be used for testing, the rest for training.
     *  0 < TestFrac < 1
     *  Uses the last fraction of the data for testing and the first part
     *  for training.
     */
    public static void main(String[] args) throws IOException {
		// All but last arg is a file/directory of LDC tagged input data
		File[] files = new File[args.length - 1];
		for (int i = 0; i < files.length; i++) 
		    files[i] = new File(args[i]);
		// Last arg is the TestFrac
		double testFraction = Double.valueOf(args[args.length -1]);
		// Get list of sentences from the LDC POS tagged input files
		List<List<String>> sentences = 	POSTaggedFile.convertToTokenLists(files);
		int numSentences = sentences.size();
		// Compute number of test sentences based on TestFrac
		int numTest = (int)Math.round(numSentences * testFraction);
		// Take test sentences from end of data
		List<List<String>> testSentences = sentences.subList(numSentences - numTest, numSentences);
		// Take training sentences from start of data
		List<List<String>> trainSentences = sentences.subList(0, numSentences - numTest);
		System.out.println("# Train Sentences = " + trainSentences.size() + 
				   " (# words = " + wordCount(trainSentences) + 
				   ") \n# Test Sentences = " + testSentences.size() +
				   " (# words = " + wordCount(testSentences) + ")");
		// Create a bigram model and train it.
		BidirectionalBigramModel model = new BidirectionalBigramModel();
		System.out.println("Training...");
		model.train(trainSentences);
		// Test on training data using test and test2
		model.test(trainSentences);
		System.out.println("Testing...");
		// Test on test data using test and test2
		model.test(testSentences);
    }
}
