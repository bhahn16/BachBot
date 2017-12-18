package main;
import java.util.ArrayList;
import java.util.Collections;

public class Song implements Cloneable
{
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
	ArrayList<String> keySignatureArray;
	static ArrayList<ArrayList<String>> keys; //moved out of chooseKeySignature()
	static ArrayList<String> currentKey;
	String key;
	int beatsPerMeasure;
	static int numberOfMeasures;
	
	static ArrayList<Integer> chordProgression;
	static ArrayList<String> bassRange;
	ArrayList<String> sopranoRange;
	ArrayList<String> altoRange;
	ArrayList<String> tenorRange;
	ArrayList<String> totalRange;
	ArrayList<Integer> bassRangeNumbers;
	ArrayList<Integer> tenorRangeNumbers;
	ArrayList<Integer> altoRangeNumbers;
	ArrayList<Integer> sopranoRangeNumbers;
	
	ArrayList<ArrayList<Integer>> chordsAsNumbers;
	
	ArrayList<String> bassVoice;
	ArrayList<String> sopranoVoice;
	ArrayList<String> altoVoice;
	ArrayList<String> tenorVoice;
	int bassTonicIndex; int sopranoTonicIndex; int altoTonicIndex; int tenorTonicIndex;
	public static int numberOfChords;
	
	String bassLine; String tenorLine; String altoLine; String sopranoLine;
	
	ArrayList<String> linesAsArray;
	static ArrayList<Boolean> nonharmonics;
	public static int numberOfPassingTones;
	public static int numberOfNeighboringTones;
	public static int numberOfSuspensions;
	public static int numberOfEscapeTones;
	public static int numberOfAppoggiaturas;
	public static int numberOfAnticipations;
	public static int instrument;
	
	static ArrayList<Integer> nonharmonicProbability;
	public static int tempo;
	public Song() {//constructor for song
		keys = new ArrayList<ArrayList<String>>();//required to compile
		fillKeysArray();
		currentKey = keys.get((int) (Math.random()*keys.size()));
		key = currentKey.get(0);
		beatsPerMeasure = 4;
		numberOfMeasures = 8;
		numberOfChords = beatsPerMeasure * numberOfMeasures;
		tempo = 70;
		instrument = 0;
		chordProgression = generateInitialChordProgression(); //BEN
		//chordProgression = generateInitialChordProgression2(); //JOE
		bassRange = findBassRange();
		sopranoRange = findSopranoRange();
		tenorRange = findTenorRange();
		altoRange = findAltoRange();
		totalRange = findTotalRange();
		bassTonicIndex = findTonicIndex(bassRange);
		sopranoTonicIndex = findTonicIndex(sopranoRange);
		altoTonicIndex = findTonicIndex(altoRange);
		tenorTonicIndex = findTonicIndex(tenorRange);
		bassVoice = new ArrayList<String>();
		altoVoice = new ArrayList<String>();
		tenorVoice = new ArrayList<String>();
		sopranoVoice = new ArrayList<String>();
		
		chordsAsNumbers = generateParts();
		linesAsArray = arrayToLines();
		bassLine = linesAsArray.get(0);
		tenorLine = linesAsArray.get(1);
		altoLine = linesAsArray.get(2);
		sopranoLine = linesAsArray.get(3);

		numberOfPassingTones = 0;numberOfNeighboringTones = 0; numberOfSuspensions = 0; numberOfEscapeTones = 0; numberOfAppoggiaturas = 0; numberOfAnticipations = 0;
		nonharmonics = new ArrayList<Boolean>(numberOfChords);
		nonharmonicProbability = new ArrayList<Integer>(4);
		//PT,NT,SUS,ET,APP
		nonharmonicProbability.add(85);nonharmonicProbability.add(95);nonharmonicProbability.add(70);nonharmonicProbability.add(40);nonharmonicProbability.add(30);
		
		sopranoLine = addNonharmonics(sopranoLine,sopranoRange,false);
		bassLine = addNonharmonics(bassLine,bassRange,true);
		tenorLine = addNonharmonics(tenorLine,tenorRange,false);
		altoLine = addNonharmonics(altoLine,altoRange,false);
	}
	/* same as no-args constructor, except all song info is represented by integers (i.e. 0 = cmajor, 1 = gmajor, etc
	*/
	public Song(int keySig, int userTempo, int userInstrument, ArrayList<Integer> userChordProgression,boolean passing,boolean neighboring,boolean suspension,boolean escape,boolean appoggiatura){		
		keys = new ArrayList<ArrayList<String>>(); //required to compile
		instrument = userInstrument;
		fillKeysArray();
		currentKey = keys.get(keySig);
		key = currentKey.get(0);
		beatsPerMeasure = 4;
		numberOfMeasures = 8;
		numberOfChords = 32;
		tempo = userTempo;
		chordProgression = userChordProgression;
		bassRange = findBassRange();
		sopranoRange = findSopranoRange();
		tenorRange = findTenorRange();
		altoRange = findAltoRange();
		totalRange = findTotalRange();
		bassTonicIndex = findTonicIndex(bassRange);
		sopranoTonicIndex = findTonicIndex(sopranoRange);
		altoTonicIndex = findTonicIndex(altoRange);
		tenorTonicIndex = findTonicIndex(tenorRange);
		bassVoice = new ArrayList<String>();
		altoVoice = new ArrayList<String>();
		tenorVoice = new ArrayList<String>();
		sopranoVoice = new ArrayList<String>();
		chordsAsNumbers = generateParts();
		linesAsArray = arrayToLines();
		bassLine = linesAsArray.get(0);
		tenorLine = linesAsArray.get(1);
		altoLine = linesAsArray.get(2);
		sopranoLine = linesAsArray.get(3);
		
		numberOfPassingTones = 0;numberOfAnticipations = 0;numberOfNeighboringTones = 0; numberOfSuspensions = 0; numberOfEscapeTones = 0; numberOfAppoggiaturas = 0;
		nonharmonics = new ArrayList<Boolean>(numberOfChords);
		nonharmonicProbability = new ArrayList<Integer>();
		//PT,NT,SUS,ET,APP
		nonharmonicProbability.add(85);nonharmonicProbability.add(95);nonharmonicProbability.add(70);nonharmonicProbability.add(40);nonharmonicProbability.add(30);
		if(!passing)
			nonharmonicProbability.set(0, -1000);
		if(!neighboring)
			nonharmonicProbability.set(1, -1000);
		if(!suspension)
			nonharmonicProbability.set(2, -1000);
		if(!escape)
			nonharmonicProbability.set(3, -1000);
		if(!appoggiatura)
			nonharmonicProbability.set(4, -1000);
		sopranoLine = addNonharmonics(sopranoLine,sopranoRange,false);
		bassLine = addNonharmonics(bassLine,bassRange, true);
		tenorLine = addNonharmonics(tenorLine,tenorRange,false);
		altoLine = addNonharmonics(altoLine,altoRange,false);
	}
	public ArrayList<String> findTotalRange(){
		ArrayList<String> totalRange = new ArrayList<String>();
		for(int i = 0; i < bassRange.size(); i++){
			totalRange.add(bassRange.get(i));
		}
		for(int i = 0; i < tenorRange.size(); i++){
			if(!totalRange.contains(tenorRange.get(i))){
				totalRange.add(tenorRange.get(i));
			}
		}
		for(int i = 0; i < altoRange.size(); i++){
			if(!totalRange.contains(altoRange.get(i))){
				totalRange.add(altoRange.get(i));
			}
		}
		for(int i = 0; i < sopranoRange.size(); i++){
			if(!totalRange.contains(sopranoRange.get(i))){
				totalRange.add(sopranoRange.get(i));
			}
		}
		return totalRange;
	}
	
	public static String addNonharmonics(String line, ArrayList<String> range, boolean isBass){
		String firstFullNote = ""; String secondFullNote = ""; String thirdFullNote = ""; int secondNoteDistance = 0; int thirdNoteDistance = 0;
		String firstNoteDuration; String secondNoteDuration; String thirdNoteDuration; 
		String firstNote; String secondNote; String thirdNote;
		int firstNoteIndex; int secondNoteIndex; int thirdNoteIndex; int secondNoteSafetyIndex = -1; int thirdNoteSafetyIndex = -1;
		String tempLine = "";
		int charCount = 0;
		for(int i = 1; i <= numberOfChords; i++){
			nonharmonics.add(false);
		}
		for(int chord = 1; chord < numberOfChords - 2; chord++){
			while(line.substring(charCount,charCount+1).equals("|") || line.substring(charCount,charCount+1).equals(" "))
				charCount++;
			while(!line.substring(charCount,charCount+1).equals("|") && !line.substring(charCount,charCount+1).equals(" ")){
				firstFullNote += line.substring(charCount,charCount+1);
				charCount++;
			}	
			while(line.substring(charCount,charCount+1).equals("|") || line.substring(charCount,charCount+1).equals(" "))
				charCount++;
			secondNoteSafetyIndex = charCount;
			while(!line.substring(charCount,charCount+1).equals("|") && !line.substring(charCount,charCount+1).equals(" ")){
				secondFullNote += line.substring(charCount,charCount+1);
				charCount++;
			}
			while(line.substring(charCount,charCount+1).equals("|") || line.substring(charCount,charCount+1).equals(" "))
				charCount++;
			thirdNoteSafetyIndex = charCount;
			while(!line.substring(charCount,charCount+1).equals("|") && !line.substring(charCount,charCount+1).equals(" ")){
				thirdFullNote += line.substring(charCount,charCount+1);
				charCount++;
			}
			
			firstNoteDuration = firstFullNote.substring(firstFullNote.length() - 1); 
			secondNoteDuration = secondFullNote.substring(secondFullNote.length() - 1); 
			thirdNoteDuration = thirdFullNote.substring(thirdFullNote.length() - 1); 
			
			firstNote = firstFullNote.substring(0, firstFullNote.length() - 2);
			secondNote = secondFullNote.substring(0, secondFullNote.length() - 2);
			thirdNote = thirdFullNote.substring(0, thirdFullNote.length() - 2);
			
			firstNoteIndex = range.indexOf(firstFullNote.substring(0,firstFullNote.length() -1 ));
			secondNoteIndex = range.indexOf(secondFullNote.substring(0,secondFullNote.length() -1 ));
			thirdNoteIndex = range.indexOf(thirdFullNote.substring(0,thirdFullNote.length() -1 ));
			
			secondNoteDistance = secondNoteIndex - firstNoteIndex;
			thirdNoteDistance = thirdNoteIndex - firstNoteIndex;
			
			if(Math.abs(secondNoteDistance) == 2 && (firstNoteDuration.equals("q") && (secondNoteDuration.equals("q"))) && (nonharmonics.get(chord) == false)) {//PASSING TONE
				if(Math.random() * 100 < nonharmonicProbability.get(0)){
					String initialTone; String passingTone; 
					initialTone = firstFullNote.substring(0,firstFullNote.length()-1) + "i ";
					int middleIndex = -1;
					if(firstNoteIndex < secondNoteIndex)
						middleIndex = firstNoteIndex + 1; 
					else if(firstNoteIndex > secondNoteIndex)
						middleIndex = firstNoteIndex - 1;
					passingTone = range.get(middleIndex) + "i ";
					tempLine = tempLine + initialTone + passingTone;
					numberOfPassingTones = numberOfPassingTones + 1;
					nonharmonics.set(chord, true);
					int probability = (int) (Math.random() * 2);
					if(probability == 0)
						probability = 10;
					else //if probability = -1;
						probability = -10;
					nonharmonicProbability.set(0, nonharmonicProbability.get(0) + probability);
					charCount = secondNoteSafetyIndex;
				}
				else{
					tempLine += firstFullNote + " ";
					charCount = secondNoteSafetyIndex;
					nonharmonicProbability.set(0,nonharmonicProbability.get(0) + 5);
				}

			}
			else if(Math.abs(secondNoteDistance) == 0 && (firstNoteDuration.equals("q") && secondNoteDuration.equals("q"))&& (nonharmonics.get(chord) == false)) {//NEIGHBORING TONE
				if(Math.random() * 100 < nonharmonicProbability.get(1) && firstNoteIndex + 1 < range.size()){
					String initialTone = firstFullNote.substring(0,firstFullNote.length()-1) + "i ";
					String neighboringTone = range.get(firstNoteIndex + 1) + "i ";
					tempLine = tempLine + initialTone + neighboringTone;
					numberOfNeighboringTones = numberOfNeighboringTones + 1;
					nonharmonics.set(chord, true);
					charCount = secondNoteSafetyIndex;
					int probability = (int) (Math.random() * 2);
					if(probability == 0)
						probability = 10;
					else //if probability = -1;
						probability = -10;
					nonharmonicProbability.set(1, nonharmonicProbability.get(1) + probability);
					charCount = secondNoteSafetyIndex;
				}
				else{
					tempLine += firstFullNote + " ";
					charCount = secondNoteSafetyIndex;
					nonharmonicProbability.set(1,nonharmonicProbability.get(1) + 5);
				}

			}
			else if(secondNoteDistance == -1 && chord < numberOfChords - 3 && firstNoteIndex - 1 >= 0 && (firstNoteDuration.equals("q") && secondNoteDuration.equals("q")) && isBass == false && (nonharmonics.get(chord + 1) == false) && (nonharmonics.get(chord) == false) ){
				if(Math.random() * 100 < nonharmonicProbability.get(2)){
					String preperationAndSuspension = firstFullNote.substring(0,firstFullNote.length()-1) + "q. ";
					String resolution = secondFullNote.substring(0,secondFullNote.length()- 1) + "i ";
					tempLine = tempLine + preperationAndSuspension;
					if(chord % 4 == 0)
						tempLine += "| ";
					chord++;
					tempLine += resolution;
					numberOfSuspensions += 1;
					nonharmonics.set(chord, true);
					nonharmonics.set(chord + 1, true);
					charCount = thirdNoteSafetyIndex;
					int probability = (int) (Math.random() * 2);
					if(probability == 0)
						probability = 10;
					else //if probability = -1;
						probability = -10;
					nonharmonicProbability.set(2, nonharmonicProbability.get(2) + probability);
				}
				else{
					tempLine += firstFullNote + " ";
					charCount = secondNoteSafetyIndex;
					nonharmonicProbability.set(2,nonharmonicProbability.get(2) + 5);
				}

			}
			else if (secondNoteDistance == -1 && firstNoteIndex - 1 > 0 && (firstNoteDuration.equals("q") && (secondNoteDuration.equals("q") && nonharmonics.get(chord) == false))){ //ESCAPE TONE
				if(Math.random() * 100 < nonharmonicProbability.get(3)){
					String initialTone = firstFullNote.substring(0,firstFullNote.length()-1) + "i ";
					String escapeTone;
					if (firstNoteIndex > secondNoteIndex && firstNoteIndex + 1 < range.size() - 1)
						escapeTone = range.get(firstNoteIndex + 1) + "i ";
					else //if (firstNoteIndex < secondNoteIndex)
						escapeTone = range.get(firstNoteIndex - 1) + "i ";
					tempLine = tempLine + initialTone + escapeTone;
					nonharmonics.set(chord, true);
					numberOfEscapeTones++;
					charCount = secondNoteSafetyIndex;
					int probability = (int) (Math.random() * 2);
					if(probability == 0)
						probability = 10;
					else //if probability = -1;
						probability = -10;
					nonharmonicProbability.set(3, nonharmonicProbability.get(3) + probability);
				}
				else{
					tempLine += firstFullNote + " ";
					charCount = secondNoteSafetyIndex;
					nonharmonicProbability.set(3,nonharmonicProbability.get(3) + 5);
				}
			}
			else if (secondNoteDistance == 1 && firstNoteIndex - 2 > 0 && firstNoteIndex + 2 <= range.size() - 1 && (firstNoteDuration.equals("q") && (secondNoteDuration.equals("q") && nonharmonics.get(chord) == false))) {//APPOGGIATURA
				if(Math.random() * 100 < nonharmonicProbability.get(4)){
					String initialTone = firstNote + "i ";
					String appoggiatura = "";
					if (firstNoteIndex > secondNoteIndex && firstNoteIndex - 2 > 0)
						appoggiatura = range.get(firstNoteIndex - 2) + "i ";
					else if (firstNoteIndex < secondNoteIndex && firstNoteIndex + 2 <= range.size() - 1)
						appoggiatura = range.get(firstNoteIndex + 2) + "i ";
					tempLine = tempLine + initialTone + appoggiatura;
					nonharmonics.set(chord, true);
					numberOfAppoggiaturas++;
					charCount = secondNoteSafetyIndex;
					charCount = secondNoteSafetyIndex;
					int probability = (int) (Math.random() * 2);
					if(probability == 0)
						probability = 10;
					else //if probability = -1;
						probability = -10;
					nonharmonicProbability.set(4, nonharmonicProbability.get(4) + probability);
				}
				else{
					tempLine += firstFullNote + " ";
					charCount = secondNoteSafetyIndex;
					nonharmonicProbability.set(4,nonharmonicProbability.get(4) + 5);
				}
			}
			else{
				tempLine += firstFullNote + " ";
				charCount = secondNoteSafetyIndex;
			}
			if(firstNoteDuration.equals("h")){
				chord++;
			}
			if(chord % 4 == 0){
				tempLine += "| ";
			}
			//charCount = secondNoteSafetyIndex;
			firstFullNote = "";
			secondFullNote = "";
			thirdFullNote = "";
		}
		for(int chord = numberOfChords - 2; chord < numberOfChords; chord++){
			while(line.substring(charCount,charCount+1).equals("|") || line.substring(charCount,charCount+1).equals(" ")){
				charCount++;
			}
			while(!line.substring(charCount,charCount+1).equals("|") && !line.substring(charCount,charCount+1).equals(" ")){
				firstFullNote += line.substring(charCount,charCount+1);
				charCount++;
			}	
			if(chord == numberOfChords - 2 && Math.random() * 100 < 50){
				String tonicNote = currentKey.get(1);
				int charCountRisk = charCount;
				while(line.substring(charCountRisk,charCountRisk+1).equals("|") || line.substring(charCountRisk,charCountRisk+1).equals(" ")){
					charCountRisk++;
				}
				while(!line.substring(charCountRisk,charCountRisk+1).equals("|") && !line.substring(charCountRisk,charCountRisk+1).equals(" ")){
					secondFullNote += line.substring(charCountRisk,charCountRisk+1);
					charCountRisk++;
				}	
				if(secondFullNote.contains(tonicNote) && numberOfAnticipations == 0){
					String anticipation = secondFullNote.substring(0,secondFullNote.length() - 1);
					tempLine = tempLine + firstFullNote.substring(0,firstFullNote.length()-1) + "i " + anticipation + "i ";
					numberOfAnticipations++;
					nonharmonics.set(chord,true);
				}
				else 
					tempLine += firstFullNote + " ";
			}
			else
				tempLine += firstFullNote + " ";
			if(chord % 4 == 0)
				tempLine += "| ";
			firstFullNote = "";
		}
		return tempLine;
	}
	public ArrayList<String> arrayToLines(){
		String bassLine = ""; String tenorLine = ""; String altoLine = ""; String sopranoLine = "";
		  ArrayList<ArrayList<Integer>> chords = chordsAsNumbers;
		  int cadenceCount = 0;
		  for(int chordCount = 1; chordCount <= numberOfChords; chordCount++){
			  ArrayList<Integer> chord = chords.get(chordCount);
			  int bassNoteIndex = chord.get(0);
			  int tenorNoteIndex = chord.get(1);
			  int altoNoteIndex = chord.get(2);
			  int sopranoNoteIndex = chord.get(3);
			  
			  String bassNote = bassRange.get(bassNoteIndex);
			  String tenorNote = tenorRange.get(tenorNoteIndex);
			  String altoNote = altoRange.get(altoNoteIndex);
			  String sopranoNote = sopranoRange.get(sopranoNoteIndex);
			  
			  if((chordCount - cadenceCount)% 15 == 0) {//cadence
				  bassLine += bassNote + "h ";
				  tenorLine += tenorNote + "h ";
				  altoLine += altoNote + "h ";
				  sopranoLine += sopranoNote + "h ";
				  cadenceCount += 1;
				  chordCount+=1;
				  
				  bassVoice.add(bassNote + "h ");
				  tenorVoice.add(tenorNote + "h ");
				  altoVoice.add(altoNote + "h ");
				  sopranoVoice.add(sopranoNote + "h ");
;			  }
			  else{
				  bassLine += bassNote + "q ";
				  tenorLine += tenorNote + "q ";
				  altoLine += altoNote + "q ";
				  sopranoLine += sopranoNote + "q ";
				  bassVoice.add(bassNote + "q ");
				  tenorVoice.add(tenorNote + "q ");
				  altoVoice.add(altoNote + "q ");
				  sopranoVoice.add(sopranoNote + "q");
			  }
			  if(chordCount % 4 == 0){
				  bassLine += "| ";
				  tenorLine += "| ";
				  altoLine += "| ";
				  sopranoLine += "| ";
			  }
		  }
		  ArrayList<String> tempLines = new ArrayList<String>();
		  tempLines.add(bassLine); tempLines.add(tenorLine); tempLines.add(altoLine); tempLines.add(sopranoLine);
		  return tempLines;
	}
	public int chooseBeatsPerMeasure() {//change later if beatsPerMeasure becomes randomized
		return 4;
	}
	public int chooseNumberOfMeasures() {//returns int as number of measures; probably a multiple of 4
		return 16;
	}
	public int findTonicIndex(ArrayList<String> voiceRange){
		String tonic = currentKey.get(1); 
		int tonicIndex = 0; 
		for(int i = voiceRange.size() - 1; i > 0; i--){
			if(voiceRange.get(i).contains(tonic))
				tonicIndex = i;
		}
		return tonicIndex;
	}
	public static ArrayList<Integer> getChordProgression(){
		return chordProgression;
	}
	public ArrayList<ArrayList<Integer>> generateParts(){
		ArrayList<ArrayList<Integer>> chords = new ArrayList<ArrayList<Integer>>();
		/*
		 * ORGANIZED BY CHORD NUMBER
		 * each sub array 
		 * 0 == bass 
		 * 1 == tenor 
		 * 2 == alto 
		 * 3 == soprano
		 */
		ArrayList<Integer> possibleBassNotes = new ArrayList<Integer>();
		ArrayList<Integer> possibleSopranoNotes = new ArrayList<Integer>();
		ArrayList<Integer> possibleTenorNotes = new ArrayList<Integer>();
		ArrayList<Integer> possibleAltoNotes = new ArrayList<Integer>();
		ArrayList<Integer> emptyArray = new ArrayList<Integer>(); emptyArray.add(0);
		chords.add(emptyArray);
		int lastChordNumeral; 
		int lastBassNote; int lastSopranoNote; int lastTenorNote; int lastAltoNote; 
		ArrayList<Integer> currentChord = new ArrayList<Integer>(); int currentChordNumeral = 0; 
		int currentBassNote = 0; int currentSopranoNote = 0; int currentTenorNote = 0; int currentAltoNote = 0; 
		boolean firstInversion = false;
		//for(int i = 1; i <= numberOfMeasures * beatsPerMeasure; i++)
		for(int i = 1; i <= 32; i++){
			if(i == 1){ //adds root position chord
				currentChordNumeral = chordProgression.get(i);
				currentBassNote = bassTonicIndex;
				int random;
				if(currentBassNote + 7 < bassRange.size() -1){
					random = (int) (Math.random() * 100);
					if(random < 70)
						currentBassNote += 7;
				}
				if(tenorTonicIndex + 9 < tenorRange.size() - 1)
					currentTenorNote = tenorTonicIndex + 9;
				else if(tenorTonicIndex + 16 < tenorRange.size())
					currentTenorNote = tenorTonicIndex + 16;
				else
					currentTenorNote = tenorTonicIndex + 2; 
				if(altoTonicIndex + 13 < altoRange.size() - 1)
					currentAltoNote = altoTonicIndex + 11;
				else
					currentAltoNote = altoTonicIndex + 4; 
				if(sopranoTonicIndex + 7 < sopranoRange.size() - 1)
					currentSopranoNote = sopranoTonicIndex + 7;
				else
					currentSopranoNote = sopranoTonicIndex;
				currentChord.add(currentBassNote);
				currentChord.add(currentTenorNote);
				currentChord.add(currentAltoNote);
				currentChord.add(currentSopranoNote);
			}
			else {//(i > 1)
				lastChordNumeral = chordProgression.get(i - 1);
				currentChordNumeral = chordProgression.get(i);
				lastBassNote = chords.get(i -1).get(0);
				lastTenorNote = chords.get(i -1).get(1);
				lastAltoNote = chords.get(i-1).get(2);
				lastSopranoNote = chords.get(i-1).get(3);
				if(lastChordNumeral + 4 == currentChordNumeral || currentChordNumeral - 4 == lastChordNumeral) //parallel 5ths in bass; invert chord
					firstInversion = true; 
				else
					firstInversion = false;
				int root = 0; int third = 2; int fifth = 4; int leastDistance;
				boolean rootAvailable = true; boolean thirdAvailable = true; boolean fifthAvailable = true;
				int lowestNumeral;
				possibleBassNotes = findIndexes(bassRange,currentChordNumeral);
				currentBassNote = findBestNote(lastBassNote,possibleBassNotes);
				lowestNumeral = currentChordNumeral; 
				if(lowestNumeral == 7){
					if(currentBassNote < bassRange.size() - 4)
						currentBassNote += 2; //invert)s 7 diminished to first inversion
					else
						currentBassNote-=5;
				}
				if(firstInversion == true){
					lowestNumeral = currentChordNumeral + 2;
					if(lowestNumeral > 7)
						lowestNumeral -= 7;
					possibleBassNotes = findIndexes(bassRange,lowestNumeral);
					currentBassNote = findBestNote(lastBassNote,possibleBassNotes);
				}
				lowestNumeral = currentChordNumeral + root;
				if(lowestNumeral > 7)
					lowestNumeral -=7;
				possibleSopranoNotes = findIndexes(sopranoRange,lowestNumeral);
				leastDistance = findLeastDistance(lastSopranoNote, possibleSopranoNotes);
				if(leastDistance < 4){
					currentSopranoNote = findBestNote(lastSopranoNote,possibleSopranoNotes);
					rootAvailable = false;
				}
				lowestNumeral = currentChordNumeral + third;
				if(lowestNumeral > 7)
					lowestNumeral -=7;
				possibleSopranoNotes = findIndexes(sopranoRange,lowestNumeral);
				leastDistance = findLeastDistance(lastSopranoNote, possibleSopranoNotes);	
				if(leastDistance < 4 && rootAvailable != false){
					currentSopranoNote = findBestNote(lastSopranoNote,possibleSopranoNotes);
					thirdAvailable = false;
				}
				if((rootAvailable && thirdAvailable) == true) {//fifth
					lowestNumeral = currentChordNumeral + fifth;
					if(lowestNumeral > 7)
						lowestNumeral -=7;
					possibleSopranoNotes = findIndexes(sopranoRange,lowestNumeral);
					currentSopranoNote = findBestNote(lastSopranoNote,possibleSopranoNotes);
					fifthAvailable = false;
				}
				//TENOR 
				if(rootAvailable == true){
					lowestNumeral = currentChordNumeral + root;
					if(lowestNumeral > 7)
						lowestNumeral -=7;
					possibleTenorNotes = findIndexes(tenorRange,lowestNumeral);
					leastDistance = findLeastDistance(lastTenorNote,possibleTenorNotes);
					if(leastDistance < 4){
						currentTenorNote = findBestNote(lastTenorNote, possibleTenorNotes); 
						rootAvailable = false;
					}
				}
				if((thirdAvailable == true) && ((rootAvailable || fifthAvailable) == true)){
					lowestNumeral = currentChordNumeral + third;
					if(lowestNumeral > 7)
						lowestNumeral -=7;
					possibleTenorNotes = findIndexes(tenorRange,lowestNumeral);
					leastDistance = findLeastDistance(lastTenorNote,possibleTenorNotes);
					if(leastDistance < 4){
						currentTenorNote = findBestNote(lastTenorNote, possibleTenorNotes); 
						thirdAvailable = false;
					}
				}
				if((fifthAvailable == true) && ((thirdAvailable || rootAvailable) == true)){
					lowestNumeral = currentChordNumeral + fifth;
					if(lowestNumeral > 7)
						lowestNumeral -=7;
					possibleTenorNotes = findIndexes(tenorRange,lowestNumeral);
					leastDistance = findLeastDistance(lastTenorNote,possibleTenorNotes);
					currentTenorNote = findBestNote(lastTenorNote, possibleTenorNotes); 
					fifthAvailable = false;
				}
				//ALTO
				if(rootAvailable == true){
					lowestNumeral = currentChordNumeral + root;
					if(lowestNumeral > 7){
						lowestNumeral -=7;
					}
					possibleAltoNotes = findIndexes(altoRange,lowestNumeral);
					currentAltoNote = findBestNote(lastAltoNote, possibleAltoNotes);
					rootAvailable = false;
				}
				if(thirdAvailable == true){
					lowestNumeral = currentChordNumeral + third;
					if(lowestNumeral > 7)
						lowestNumeral -=7;
					possibleAltoNotes = findIndexes(altoRange,lowestNumeral);
					currentAltoNote = findBestNote(lastAltoNote, possibleAltoNotes);
					rootAvailable = false;
				}
				if(fifthAvailable == true){
					lowestNumeral = currentChordNumeral + fifth;
					if(lowestNumeral > 7)
						lowestNumeral -=7;
					possibleAltoNotes = findIndexes(altoRange,lowestNumeral);
					currentAltoNote = findBestNote(lastAltoNote, possibleAltoNotes);
					fifthAvailable = false;
				}
			}
			currentChord.add(currentBassNote);
			currentChord.add(currentTenorNote);
			currentChord.add(currentAltoNote);
			currentChord.add(currentSopranoNote);
			chords.add(currentChord);
			currentChord = new ArrayList<Integer>();
			
		}

		return chords;
	}
	public int findDistance(int lastNote, int nextNote){
		return Math.abs(lastNote - nextNote);
	}
	public int findLeastDistance(int lastNote, ArrayList<Integer> possibleNotes){
		int leastDistance; 
		leastDistance = Math.abs(possibleNotes.get(0) - lastNote);
		if(Math.abs(lastNote - possibleNotes.get(0)) < leastDistance)
			leastDistance = Math.abs(lastNote - possibleNotes.get(0));
		for(int i = 1; i < possibleNotes.size(); i++){
			if(((Math.abs(possibleNotes.get(i) - lastNote)) < leastDistance))
				leastDistance = (Math.abs(possibleNotes.get(i) - lastNote));
			else if(Math.abs(lastNote - possibleNotes.get(i)) < leastDistance)
				leastDistance = Math.abs(lastNote - possibleNotes.get(i));
		}
		return leastDistance;
	}
	public int findBestNote(int lastNote, ArrayList<Integer> possibleNotes){
		int leastDistance; int bestIndex = 0; 
		leastDistance = Math.abs(possibleNotes.get(0) - lastNote);
		if(Math.abs(lastNote - possibleNotes.get(0)) < leastDistance)
			leastDistance = Math.abs(lastNote - possibleNotes.get(0));
		for(int i = 1; i < possibleNotes.size(); i++){
			if(((Math.abs(possibleNotes.get(i) - lastNote)) < leastDistance) || (Math.abs(lastNote - possibleNotes.get(i)) < leastDistance))
				bestIndex = i;
		}
		return possibleNotes.get(bestIndex);
	}
	public ArrayList<Integer> findIndexes(ArrayList<String> voiceRange, int note) {//finds the indexes of the note in the given range
		ArrayList<Integer> tempIndexes = new ArrayList<Integer>();
		for(int i = 1; i < voiceRange.size(); i++){
			if((voiceRange.get(i)).contains(currentKey.get(note)))
				tempIndexes.add(i);
		}
		return tempIndexes;
	}
	
	public void fillKeysArray() {//made void from ArrayList<String>
		ArrayList<String> cMajor = new ArrayList<String>(); 
		cMajor.add("cMajor"); cMajor.add("C"); cMajor.add("D"); cMajor.add("E"); cMajor.add("F"); cMajor.add("G"); cMajor.add("A"); cMajor.add("B"); 
		ArrayList<String> gMajor = new ArrayList<String>();
		gMajor.add("gMajor"); gMajor.add("G"); gMajor.add("A"); gMajor.add("B"); gMajor.add("C"); gMajor.add("D"); gMajor.add("E"); gMajor.add("F#"); 
		ArrayList<String> dMajor = new ArrayList<String>();
		dMajor.add("dMajor");dMajor.add("D");dMajor.add("E");dMajor.add("F#");dMajor.add("G");dMajor.add("A");dMajor.add("B");dMajor.add("C#");
		ArrayList<String> aMajor = new ArrayList<String>();
		aMajor.add("aMajor");aMajor.add("A");aMajor.add("B");aMajor.add("C#");aMajor.add("D");aMajor.add("E");aMajor.add("F#");aMajor.add("G#");
		ArrayList<String> eMajor = new ArrayList<String>();
		eMajor.add("eMajor");eMajor.add("E");eMajor.add("F#");eMajor.add("G#");eMajor.add("A");eMajor.add("B");eMajor.add("C#");eMajor.add("D#");
		ArrayList<String> bMajor = new ArrayList<String>();
		bMajor.add("bMajor");bMajor.add("B");bMajor.add("C#");bMajor.add("D#");bMajor.add("E");bMajor.add("F#");bMajor.add("G#");bMajor.add("A#");
		ArrayList<String> fSharpMajor = new ArrayList<String>();
		fSharpMajor.add("fSharpMajor");fSharpMajor.add("F#");fSharpMajor.add("G#");fSharpMajor.add("A#");fSharpMajor.add("B");fSharpMajor.add("C#");fSharpMajor.add("D#");fSharpMajor.add("E#");
		ArrayList<String> cSharpMajor = new ArrayList<String>();
		cSharpMajor.add("cSharpMajor");cSharpMajor.add("C#");cSharpMajor.add("D#");cSharpMajor.add("E#");cSharpMajor.add("F#");cSharpMajor.add("G#");cSharpMajor.add("A#");cSharpMajor.add("B#");
		ArrayList<String> fMajor = new ArrayList<String>();
		fMajor.add("fMajor");fMajor.add("F");fMajor.add("G");fMajor.add("A");fMajor.add("Bb");fMajor.add("C");fMajor.add("D");fMajor.add("E");
		ArrayList<String> bFlatMajor = new ArrayList<String>();
		bFlatMajor.add("bFlatMajor");bFlatMajor.add("Bb");bFlatMajor.add("C");bFlatMajor.add("D");bFlatMajor.add("Eb");bFlatMajor.add("F");bFlatMajor.add("G");bFlatMajor.add("A");
		ArrayList<String> eFlatMajor = new ArrayList<String>();
		eFlatMajor.add("eFlatMajor");eFlatMajor.add("Eb");eFlatMajor.add("F");eFlatMajor.add("G");eFlatMajor.add("Ab");eFlatMajor.add("Bb");eFlatMajor.add("C");eFlatMajor.add("D");
		ArrayList<String> aFlatMajor = new ArrayList<String>();
		aFlatMajor.add("aFlatMajor");aFlatMajor.add("Ab");aFlatMajor.add("Bb");aFlatMajor.add("C");aFlatMajor.add("Db");aFlatMajor.add("Eb");aFlatMajor.add("F");aFlatMajor.add("G");
		ArrayList<String> dFlatMajor = new ArrayList<String>();
		dFlatMajor.add("dFlatMajor");dFlatMajor.add("Db");dFlatMajor.add("Eb");dFlatMajor.add("F");dFlatMajor.add("Gb");dFlatMajor.add("Ab");dFlatMajor.add("Bb");dFlatMajor.add("C");
		ArrayList<String> gFlatMajor = new ArrayList<String>();
		gFlatMajor.add("gFlatMajor");gFlatMajor.add("Gb");gFlatMajor.add("Ab");gFlatMajor.add("Bb");gFlatMajor.add("Cb");gFlatMajor.add("Db");gFlatMajor.add("Eb");gFlatMajor.add("F");
		ArrayList<String> cFlatMajor = new ArrayList<String>();
		cFlatMajor.add("cFlatMajor");cFlatMajor.add("Cb");cFlatMajor.add("Db");cFlatMajor.add("Eb");cFlatMajor.add("Fb");cFlatMajor.add("Gb");cFlatMajor.add("Ab");cFlatMajor.add("Bb");
		keys.add(cMajor); keys.add(gMajor); keys.add(dMajor);keys.add(aMajor);keys.add(eMajor);keys.add(bMajor);keys.add(fSharpMajor);keys.add(cSharpMajor);
		keys.add(fMajor);keys.add(bFlatMajor);keys.add(eFlatMajor);keys.add(aFlatMajor);keys.add(dFlatMajor);keys.add(gFlatMajor); keys.add(cFlatMajor);
        
	}
	public ArrayList<Integer> generateInitialChordProgression(){ //BEN AND JOE's VERSION
		int numberOfChords = numberOfMeasures * beatsPerMeasure;
		ArrayList<Integer> tempChordProgression = new ArrayList<Integer>(numberOfChords);
		int randProg = 0; int chordQuant; int changeProg;
		tempChordProgression.add(0);
		int random; int count = 0;
		boolean fourToSevenRecent = false; int fourToSevenCount = 0;
		for(int i = 1; i <= numberOfChords; i++){
			if(i == 1){
				int random2 = (int) (Math.random() * 100);
				if(random2 < 70)
					tempChordProgression.add(1);
				else if(random2 < 90){
					tempChordProgression.add(5);
					tempChordProgression.add(1);
					i++;
				}
				else{
					tempChordProgression.add(7);
					tempChordProgression.add(1);
					i++;
				}
			}
			else if((i - count) % 14 == 0){ //cadence (add two notes
				count+=2;
				if(i < numberOfChords - 3){ //not end of song
				    random = (int) (Math.random() * 100);
					if(random < 33){
						tempChordProgression.add(4);
						tempChordProgression.add(5);
						tempChordProgression.add(5);
						i = i + 2;
					}
					else if(random < 66){
						tempChordProgression.add(1);
						tempChordProgression.add(5);
						tempChordProgression.add(5);
						i = i + 2;
					}
					else{
						tempChordProgression.add(5);
						tempChordProgression.add(6);
						tempChordProgression.add(6);
						i = i + 2;
					}
				}
				else {//end of song
					random = (int) (Math.random() * 100);
					if(random < 15)
						tempChordProgression.add(7);
					else
						tempChordProgression.add(5);
					tempChordProgression.add(1);
					tempChordProgression.add(1);
					i=i+2;
				}
			}
			else {//everything else
				chordQuant = tempChordProgression.get(i - 1);
				if(chordQuant == 7)
					tempChordProgression.add(1);
				else if (randProg == 0) {//as4
					chordQuant = tempChordProgression.get(i-1); //stay between 1-7
					if (chordQuant <= 3) 
						tempChordProgression.add(chordQuant + 4);
					else //chordQuant > 3
						tempChordProgression.add(chordQuant - 3);
					changeProg = (int) (Math.random() * 100); //0-99
					if (changeProg < 38) //40% chance to change progression type
						randProg = (int) (Math.random() * 4); //change progression type
					if(chordQuant == 4 && fourToSevenCount < 1 && fourToSevenCount < 2) {//HELPS AVOID REPEATING 7 1 4 7 patter
						randProg = (int) (Math.random() * 3 + 1);
						//fourToSevenRecent = true;
						fourToSevenCount++;
					}
					else {//fourToSeven recently
						randProg = (int) (Math.random() * 4);
						//fourToSevenRecent = false;
					}
				}
				else if (randProg == 1) {//as5
					chordQuant = tempChordProgression.get(i-1); //stay between 1-7
					if (chordQuant <= 4)
						tempChordProgression.add(chordQuant + 3);
					else //chordQuant > 4
						tempChordProgression.add(chordQuant - 4);
					
					changeProg = (int) (Math.random() * 100); //0-99
					if (changeProg < 40) //30% chance to change progression type
						randProg = (int) (Math.random() * 4); //change progression type
				}
				else if (randProg == 2) {//ds3
					chordQuant = tempChordProgression.get(i-1); //stay between 1-7
					if (chordQuant <= 2) 
						tempChordProgression.add(chordQuant + 5);
					else //chordQuant > 2
						tempChordProgression.add(chordQuant - 2);
					changeProg = (int) (Math.random() * 100); //0-99
					if (changeProg < 69) //40% chance to change progression type
						randProg = (int) (Math.random() * 4); //change progression type
				}
				else {// (randProg == 3) //as2
					chordQuant = tempChordProgression.get(i-1); //stay between 1-7
					if (chordQuant <= 6) 
						tempChordProgression.add(i,chordQuant + 1);
					else //chordQuant > 6
						tempChordProgression.add(i,chordQuant - 6);
					changeProg = (int) (Math.random() * 100); //0-99
					if (changeProg < 80) //60% chance to change progression type
						randProg = (int) (Math.random() * 4); //change progression type
				}
			}
		}
		return tempChordProgression;
	}
	public ArrayList<Integer> generateInitialChordProgression2(){ //Version 2
		int numberOfChords = numberOfMeasures * beatsPerMeasure;
		ArrayList<Integer> tempChordProgression = new ArrayList<Integer>(numberOfChords);
		tempChordProgression.add(0);
		int random; int count = 0;
		for(int i = 1; i <= numberOfChords; i++){
			if(i == 1)
				tempChordProgression.add(1);
			else if(i % 16 == 0)
				tempChordProgression.add(tempChordProgression.get(i - 1));
			else{
				int lastChord = tempChordProgression.get(i - 1);
				int nextChord = 0;
				if(lastChord == 1){
					random = (int) (Math.random() * 7 + 1);
					nextChord = random;
				}
				else if(lastChord ==2){
					random = (int) (Math.random() * 2);
					if(random == 0)
						nextChord = 5;
					else
						nextChord = 7;
				}
				else if(lastChord == 3){
					random = (int) (Math.random() *100);
					if(random < 45)
						nextChord = 4;
					else if(random < 90)
						nextChord = 6;
					else
						nextChord = 1;
				}
				else if(lastChord ==4){
					random = (int) (Math.random() * 100);
					if(random < 60)
						nextChord = 5;
					else if(random < 75)
						nextChord = 2;
					else
						nextChord = 1;
				}
				else if(lastChord == 5){
					random = (int) Math.random() * 100;
					if(random < 40)
						nextChord = 1;
					else if(random < 100)
						nextChord = 6;
				}
				else if(lastChord == 6){
					random = (int) (Math.random() * 100);
					if(random < 20)
						nextChord = 1;
					else if(random < 40)
						nextChord = 2;
					else if(random < 60)
						nextChord = 3;
					else if(random < 80)
						nextChord = 4;
					else
						nextChord = 5;
				}
				else
					nextChord = 1;
				tempChordProgression.add(nextChord);
			}
		}
		return tempChordProgression;
	}
	
	public ArrayList<String> findBassRange(){
		ArrayList<String> bassNoteRange = new ArrayList<String>(14); //Range of E2 to C4
		bassNoteRange.add("ERROR");
		int lowNoteIndex = 0; int highNoteIndex = 0;
		for(int i = 1; i < currentKey.size(); i++) {//finds index of F (lowest bass) and C highest bass
			String currentNote = currentKey.get(i);
			if(currentNote.contains("E"))
				lowNoteIndex = i;
			else if(currentNote.contains("C"))
				highNoteIndex = i;
		}
		int currentKeyIndex = lowNoteIndex;
		while(!(currentKey.get(currentKeyIndex).contains("C"))) {//adds E2 --> B2{
			bassNoteRange.add(currentKey.get(currentKeyIndex) + "3");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		bassNoteRange.add(currentKey.get(currentKeyIndex) + "4"); //adds C3 or C#3
		currentKeyIndex++;
		if(currentKeyIndex > 7)
			currentKeyIndex = 1;
		while(!currentKey.get(currentKeyIndex).contains("C")){
			bassNoteRange.add(currentKey.get(currentKeyIndex) + "4");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		bassNoteRange.add(currentKey.get(currentKeyIndex) + "5"); //adds C4
		return bassNoteRange;
	}
	public ArrayList<String> findSopranoRange()
	{
		ArrayList<String> sopranoNoteRange = new ArrayList<String>(14); //Range of C4 to A5
		sopranoNoteRange.add("ERROR");
		int lowNoteIndex = 0; int highNoteIndex = 0;
		for(int i = 1; i < currentKey.size(); i++) {//finds index of F (lowest bass) and C highest bass
			String currentNote = currentKey.get(i);
			if(currentNote.contains("B"))
				lowNoteIndex = i;
			else if(currentNote.contains("A"))
				highNoteIndex = i;
		}
		int currentKeyIndex = lowNoteIndex;
		while(!(currentKey.get(currentKeyIndex).contains("C"))){ //adds B3
			sopranoNoteRange.add(currentKey.get(currentKeyIndex) + "4");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		sopranoNoteRange.add(currentKey.get(currentKeyIndex) + "5"); //adds C4 or C#4
		currentKeyIndex++;
		if(currentKeyIndex > 7)
			currentKeyIndex = 1;
		while(!(currentKey.get(currentKeyIndex).contains("C"))) {//D4-B4
			sopranoNoteRange.add(currentKey.get(currentKeyIndex) + "5");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		if(currentKeyIndex > 7)
			currentKeyIndex = 1;
		sopranoNoteRange.add(currentKey.get(currentKeyIndex) + "6");
		currentKeyIndex++;
		if(currentKeyIndex > 7)
			currentKeyIndex = 1;
		while(!currentKey.get(currentKeyIndex).contains("B")){ //D5 --> A5
			sopranoNoteRange.add(currentKey.get(currentKeyIndex) + "6");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		return sopranoNoteRange;
	}
	public ArrayList<String> findTenorRange(){
		ArrayList<String> tenorNoteRange = new ArrayList<String>(14); //Range of C3 to A4
		tenorNoteRange.add("ERROR");
		int lowNoteIndex = 0; int highNoteIndex = 0;
		for(int i = 1; i < currentKey.size(); i++) {//finds index of C (lowest bass) and G highest bass
			String currentNote = currentKey.get(i);
			if(currentNote.contains("C"))
				lowNoteIndex = i;
			else if(currentNote.contains("G"))
				highNoteIndex = i;
		}
		int currentKeyIndex = lowNoteIndex;
		tenorNoteRange.add(currentKey.get(currentKeyIndex) + "4");
		currentKeyIndex++;
		if(currentKeyIndex > 7)
			currentKeyIndex = 1; 
		while(!(currentKey.get(currentKeyIndex).contains("C"))){ //adds D3-B3
			tenorNoteRange.add(currentKey.get(currentKeyIndex) + "4");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		tenorNoteRange.add(currentKey.get(currentKeyIndex) + "5"); //adds C3 or C#3
		currentKeyIndex++;
		if(currentKeyIndex > 7)
			currentKeyIndex = 1;
		while(!currentKey.get(currentKeyIndex).contains("B")) {//up to B4
			tenorNoteRange.add(currentKey.get(currentKeyIndex) + "5");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		return tenorNoteRange;
	}
	public ArrayList<String> findAltoRange(){
		ArrayList<String> altoNoteRange = new ArrayList<String>(14); //Range of F3 to D4
		altoNoteRange.add("ERROR");
		int lowNoteIndex = 0; int highNoteIndex = 0;
		for(int i = 1; i < currentKey.size(); i++) {//finds index of F (lowest bass) and C highest bass
			String currentNote = currentKey.get(i);
			if(currentNote.contains("F"))
				lowNoteIndex = i;
			else if(currentNote.contains("C"))
				highNoteIndex = i;
		}
		int currentKeyIndex = lowNoteIndex;
		while(!(currentKey.get(currentKeyIndex).contains("C"))) {
			altoNoteRange.add(currentKey.get(currentKeyIndex) + "4");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		altoNoteRange.add(currentKey.get(currentKeyIndex) + "5"); //adds C3 or C#3
		currentKeyIndex++;
		if(currentKeyIndex > 7)
			currentKeyIndex = 1;
		while(!currentKey.get(currentKeyIndex).contains("C")){
			altoNoteRange.add(currentKey.get(currentKeyIndex) + "5");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		while(!currentKey.get(currentKeyIndex).contains("E")){
			altoNoteRange.add(currentKey.get(currentKeyIndex) + "6");
			if(currentKeyIndex < 7)
				currentKeyIndex++;
			else
				currentKeyIndex = 1; 
		}
		return altoNoteRange;
	}
	static int max(int length, int length2, int length3, int length4) {
		if(length < length2)
			length = length2;
		if(length < length3)
			length = length3;
		if(length < length4)
			length = length4;
		return length;
	}  
	static String ArrayToLine(ArrayList<String> notes)
	{
		String line = ""; 
		ArrayList<String> notesInMeasure = new ArrayList<String>();
		int note = 0;
		for(int measure = 0; measure < 8; measure++){
			while(note < notes.size() && !notes.get(note).equals("|")){
				notesInMeasure.add(notes.get(note));
				note++;
			}
			for(int i = 0; i < notesInMeasure.size(); i++){
				String thisNote = notesInMeasure.get(i);
				if(thisNote.contains("i"))
					line += addSpaces(thisNote, 5);
				else if(thisNote.contains("q."))
					line += addSpaces(thisNote, 15);
				else if(thisNote.contains("q"))
					line += addSpaces(thisNote,10);
				else if(thisNote.contains("|"));
				else //thisNote.contains("h")
					line += addSpaces(thisNote,20);
			}
			line += "| ";
			note++;
			
		}
		return line;
	}
	private static String addSpaces(String s, int length){
		int extra = length - s.length();
		for(int i = 0; i < extra; i++)
			s += " ";
		return s;
	}
	public static int numeralToInteger(String numeral){
		String[] numerals = new String[]{"I","ii","iii","IV","V","vi","vii"};
		for(int i = 0; i < numerals.length; i++){
			if(numerals[i].equals(numeral)){
				return i + 1;
			}
		}
		return -1;
	}
}

