/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stopwordsfilter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class StopWordsFilter {

    public static void main(String[] args) throws IOException {
        File stopWordsFile = new File("C:/Users/user/Desktop/inforet/stopwords.txt");
        List<String> stopWords = StopWordsUtilities.readStopWordsFile(stopWordsFile);
        
        File inputFiles = new File("C:/Users/user/Desktop/inforet/shorttexts");
        File outputSTPDirectory = new File("C:/Users/user/Desktop/outputSTP");
        
        File[] dataFiles = inputFiles.listFiles();
            for(File f: dataFiles){
            System.out.println("File "+f.getName());
        }
        
         for (File file: outputSTPDirectory.listFiles()) {
	        if (!file.isDirectory())
	            file.delete();
	    }
       
        
        for (File file : dataFiles) {
        	 List<String> filteredText = StopWordsUtilities.fileToListWithoutStopWords(file, stopWords);
        	 StopWordsUtilities.printToFile(filteredText,
                     outputSTPDirectory.getPath() +"/"+ file.getName());
        }
        
        
        System.out.println("--------------------- Project 1  STOP WORDS ---------------------------");
       // File inputFiles2 = new File("C:/Users/user/Desktop/outputSTP");
        File outputSFXDirectory = new File("C:/Users/user/Desktop/outputSFX");
        for (File file: outputSFXDirectory.listFiles()) {
	        if (!file.isDirectory())
	            file.delete();
	    }
        
        File[] stpFiles = outputSTPDirectory.listFiles();
        
        for(File file : stpFiles) {
        	//   System.out.println(" 2 "+file.getName());
        	List<String> tempList = StopWordsUtilities.fileToList("C:/Users/user/Desktop/outputSTP/"+file.getName());
        	
        	List<String> sfxResult = completeStem(tempList);
        	StopWordsUtilities.printToFile(sfxResult,
                   outputSFXDirectory.getPath() +"/"+ file.getName());
        }
      

        System.out.println("-------------------------- Project 2 Stemmer---------------------------");
        /*
        if (inputFileOrDirectory != null && inputFileOrDirectory.exists()) {
            if (inputFileOrDirectory.isDirectory() && inputFileOrDirectory.listFiles().length > 0) {
            	
                for (File inputFile : inputFileOrDirectory.listFiles()) {
                    List<String> filteredText = StopWordsUtilities.fileToListWithoutStopWords(inputFile, stopWords);
                    StopWordsUtilities.printToFile(filteredText,
                            outputSTPDirectory.getPath() + "/filtered_" + inputFile.getName());
                }
            } else {
                List<String> filteredText = StopWordsUtilities.fileToListWithoutStopWords(inputFileOrDirectory, stopWords);
                StopWordsUtilities.printToFile(filteredText,
                        outputSTPDirectory.getPath() + "/filtered_" + inputFileOrDirectory.getName());
            }
        }
        */
        
File [] sfxFiles = outputSFXDirectory.listFiles();

ArrayList<String> wordsNoRep = invertedfile.allWordsNoRep(outputSFXDirectory);
   
double[][] wordsMatrix = new double[sfxFiles.length][wordsNoRep.size()];
//Arrays.stream(wordsMatrix).forEach(a -> Arrays.fill(a, 0));
System.out.println(wordsNoRep);

wordsMatrix= invertedfile.fillMatrix(wordsMatrix, sfxFiles.length, sfxFiles, wordsNoRep);
int[] docFreq = invertedfile.docFreq(wordsMatrix,sfxFiles, wordsNoRep); 
System.out.println("Document Frequency" + Arrays.toString(docFreq));
invertedfile.tfidf(wordsNoRep, sfxFiles, wordsMatrix, docFreq);
invertedfile.userQuery();
double[][] queryMatrix = new double[1][wordsNoRep.size()];
queryMatrix= invertedfile.fillQueryMatrix(queryMatrix,1, sfxFiles, wordsNoRep);
invertedfile.calcCosine(wordsMatrix,queryMatrix);

//invertedfile.printMatrix(wordsMatrix);
    }
    
    
    
    public static List<String> completeStem(List<String> tokens1) {
        //Porter Algorithm
        Stemmer pa = new Stemmer();
        List<String> arrstr = new ArrayList<String>();
        for (String i : tokens1) {
            String s1 = pa.step1(i);
            String s2 = pa.step2(s1);
            String s3 = pa.step3(s2);
            String s4 = pa.step4(s3);
            String s5 = pa.step5(s4);
            arrstr.add(s5);
        }
        
        return arrstr;
    }
    
  

}
