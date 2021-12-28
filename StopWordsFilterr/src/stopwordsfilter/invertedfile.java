/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stopwordsfilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class invertedfile {
    //method to convert text file to String List
    public static LinkedList<String> fileToStringList(String fileName) throws FileNotFoundException {
        String tempString;
        LinkedList<String> wordsList;
        try (Scanner input = new Scanner(new File(fileName))) {
            wordsList = new LinkedList<>();
            while (input.hasNext()) {
                // find next line
                tempString = input.next();
                wordsList.add(tempString);
            }
        }
        return wordsList;
    } 
    
    public static ArrayList<String> allWordsNoRep(File outputSFXDirectory) throws FileNotFoundException{
     File[] files_sfx = outputSFXDirectory.listFiles();
     HashSet<String> wordsHashSet = new HashSet<>();
        
        for(File f: files_sfx){
            LinkedList<String> allFileWords = fileToStringList("C:/Users/user/Desktop/outputSFX/"+f.getName());
        	for(String word : allFileWords){
        		wordsHashSet.add(word);
        	}
        
        }
        
        ArrayList<String> allWordsNoRep = new ArrayList<>(wordsHashSet);
     return allWordsNoRep;
    }
    
  public static int[] occurenceOfwordsListInFile(String filePath, ArrayList<String> allWordsWithoutRep) throws FileNotFoundException {
		
		LinkedList<String> allFileWordsList = fileToStringList(filePath); 
                int[] matrixRow = new int[allWordsWithoutRep.size()];
		
		for (int i=0; i<allWordsWithoutRep.size(); i++) {
			String word = allWordsWithoutRep.get(i);
			int count = 0;
			for (String temp: allFileWordsList) {
				if(temp.equals(word)) 
                                    count++;
			}
			matrixRow[i] = count;
		}
		
		return matrixRow;
		
	}
	
//fill the matrix	
public static double[][] fillMatrix(double[][] matrix, int size, File [] sfxFiles, ArrayList<String> allWordsWithoutRepList) throws FileNotFoundException{
        for (int x=0; x < size; x++) {
            int[] tempRow = occurenceOfwordsListInFile("C:/Users/user/Desktop/outputSFX/"+sfxFiles[x].getName(),allWordsWithoutRepList);
        	
        	for (int i=0; i <allWordsWithoutRepList.size();i++) {
        		matrix[x][i] = tempRow[i];
        	}
        }
        printMatrix(matrix);
    return matrix;}
public static int[] docFreq(double[][] matrix,File [] sfxFiles, ArrayList<String> allWordsWithoutRepList){   
        int[] docFreq = new int[allWordsWithoutRepList.size()];
        
        for(int w =0; w < allWordsWithoutRepList.size(); w++) {
        	int dFcount =0;
        	for(int f =0; f<sfxFiles.length; f++) {
        		if(matrix[f][w]!=0)dFcount++;
        	}
        	docFreq[w]= dFcount;
        }
           return docFreq;
}

//print the matrix
public static void printMatrix(double [][] wordsMatrix){
    for (double [] row:wordsMatrix){
        System.out.println(Arrays.toString(row));
    }
}

//Phase 4
public static void userQuery() throws FileNotFoundException, IOException{
    System.out.println("Phase 4");
    File stopWordsFile = new File("C:/Users/user/Desktop/inforet/stopwords.txt");
    List<String> stopWords = StopWordsUtilities.readStopWordsFile(stopWordsFile);
    System.out.println("Search query: ");
    Scanner inputScanner =new  Scanner(System.in);
    String query = StopWordsUtilities.sentenceNoStopWords(inputScanner.nextLine(),stopWords);
    System.out.println(query);
    //Write to file
    String path = "C:/Users/user/Desktop/inforet/query.txt";
    Files.write( Paths.get(path), query.getBytes());
}

public static double[][] fillQueryMatrix(double[][] queryMatrix, int size, File [] sfxFiles, ArrayList<String> allWordsWithoutRepList) throws FileNotFoundException{
    System.out.println("ALL WORDS" + allWordsWithoutRepList + "SIZE" + size);
    for (int x=0; x < size; x++) {
                int[] tempRow = occurenceOfwordsListInFile("C:/Users/user/Desktop/inforet/query.txt",allWordsWithoutRepList);
                
                    for (int i=0; i <allWordsWithoutRepList.size();i++) {
                            queryMatrix[x][i] = tempRow[i];
                            
                    }

            }
            printMatrix(queryMatrix);
            return queryMatrix;
}

// Calculate TFIDF = TF (term freq.) * IDF (inverse doc freq)
public static void tfidf(ArrayList<String> wordsNoRep, File [] sfxFiles, double[][] wordsMatrix, int[] docFreq){ 
    System.out.println("DOC FREQ" + Arrays.toString(docFreq));
    for(int w=0; w < wordsNoRep.size(); w++) {
        	for(int f=0; f < sfxFiles.length; f++) {
        		wordsMatrix[f][w] = wordsMatrix[f][w] * Math.log10(sfxFiles.length/ Double.valueOf(docFreq[w]));
        	
        	}
        }
    printMatrix(wordsMatrix);
}
//Calculate Cosine
public static void calcCosine(double[][] wordsMatrix, double[][] queryMatrix){
    double sum = 0;
    double tisq=0;
    double tjsq=0;
    System.out.println(wordsMatrix[1][0]);
    for (int r =0; r<wordsMatrix.length; r++){
            for (int c =0, cq=0; c<wordsMatrix[r].length && cq<queryMatrix[0].length; c++, cq++){
             double ti = wordsMatrix[r][c];
             double tj = queryMatrix[0][cq];
             sum +=ti*tj;
             tisq += Math.pow(ti, 2);
             tjsq += Math.pow(tj, 2);  
        }
        double cos = sum/Math.sqrt(tisq*tjsq);
        System.out.println("Cosine:" + cos);       
    }
     
}
}
    