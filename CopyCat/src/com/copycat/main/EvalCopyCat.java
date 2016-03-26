package com.copycat.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class EvalCopyCat {

	String referenceFile = null;
	String evaluateFile = null;
	
	PDFParser pdfParser = null;
	PDFTextStripper pdfTextStripper = null;
	PDDocument pdDocument = null;
	COSDocument cosDocument = null;
	
	static int WORD_COMPARISON_LENGTH = 3;
	
	public EvalCopyCat(){
		
	}
	
	public EvalCopyCat(String evaluateFile, String referenceFile){		
		this.evaluateFile = evaluateFile;
		this.referenceFile = referenceFile;
	}
	
	public void checkPathValidity(String evaluateFile, String referenceFile){
		File fileValidate = null;		
		byte[] data = null;
		String referenceData = null;
		String evaluateData = null;
		fileValidate = new File(referenceFile);
		if(fileValidate.exists() && !fileValidate.isDirectory()){
			try {
				pdfParser = new PDFParser(new RandomAccessFile(fileValidate, "r"));
				pdfParser.parse();
				
				cosDocument = pdfParser.getDocument();
				
				pdfTextStripper = new PDFTextStripper();
				
				pdDocument = new PDDocument(cosDocument);
				pdfTextStripper.setStartPage(1);
				pdfTextStripper.setEndPage(pdDocument.getNumberOfPages());
				
				referenceData = pdfTextStripper.getText(pdDocument);
				System.out.println(referenceData);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		fileValidate = new File(evaluateFile);
		if(fileValidate.exists() && !fileValidate.isDirectory()){
			try {
				FileInputStream fileInputStream = new 
						FileInputStream(fileValidate);
				
				data = new byte[(int) fileValidate.length()];
				fileInputStream.read(data);
				fileInputStream.close();
				evaluateData = new String(data, "UTF-8");
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(!referenceData.equals(null) && !evaluateData.equals(null)){
			fileCompare(referenceData, evaluateData);
		}
	}
	
	public void fileCompare(String referenceData, String evaluateData){
	
		int k = 0;
		int l = 0;
		
		List<String> listCopyData = new ArrayList<String>();
		String tempWordsCopy = "";
		String[] arrReferenceData = referenceData.split(" ");
		String[] arrEvaluateData = evaluateData.split(" ");
		for(int i=0; i < arrReferenceData.length; i++){
			System.out.println("Input: "+ arrReferenceData[i]);
			for(int j=0; j < arrEvaluateData.length; j++){
				System.out.println("Comparison: "+ arrEvaluateData[j]);
				if(arrReferenceData[i].equals(arrEvaluateData[j])){
					tempWordsCopy = arrReferenceData[i];
					for( k = i+1;  k < arrReferenceData.length;k++){
						for(l = j+1; l<arrEvaluateData.length; l++){
							if(arrReferenceData[k].equals(arrEvaluateData[l])){
								tempWordsCopy = tempWordsCopy + " " + arrReferenceData[k]; 
							}else{
								listCopyData.add(tempWordsCopy);
								break;
							}
						}
					}
				}
			}
		}
		
		System.out.println("--------Probable Copy of Data------------");
		for(int val=0;val<listCopyData.size(); val++){
			System.out.println(listCopyData.get(val));
		}
	}
	
	public static void main(String[] args){
		EvalCopyCat evalCopyCat = new EvalCopyCat(args[0].toString(),
				args[1].toString());
		
		evalCopyCat.checkPathValidity(evalCopyCat.evaluateFile, evalCopyCat.referenceFile);
	}
}
