package main;
import java.util.ArrayList;


public class DisplaySong {
	
	public static String displaySong(Song song){
		//returns array of strings
		//0 = chordProgression, 1 = soprano 2=alto 3=tenor 4=bass
		String display;
		String chordProgression = "";
		for(int i = 0; i < song.getChordProgression().size(); i++)	{
			if((i - 1) % 4 == 0)
				chordProgression += ("| ");
			chordProgression += song.getChordProgression().get(i) + " ";
		}
		String sopranoLine = song.sopranoLine + " ";
		String altoLine = song.altoLine + " ";
		String tenorLine = song.tenorLine + " ";
		String bassLine = song.bassLine + " ";
		int maxLength = Song.max(sopranoLine.length(),altoLine.length(),tenorLine.length(),bassLine.length());
		int indexS = 0; int indexA = 0; int indexT = 0; int indexB = 0;
		 
		ArrayList<String> notesS = new ArrayList<String>();
		ArrayList<String> notesA = new ArrayList<String>();
		ArrayList<String> notesT = new ArrayList<String>();
		ArrayList<String> notesB = new ArrayList<String>();
		String tempS = "";String tempA = ""; String tempT = ""; String tempB = "";
		while(indexS < sopranoLine.length()){
			while(!sopranoLine.substring(indexS, indexS + 1).equals(" ")){
				tempS += sopranoLine.substring(indexS, indexS + 1);
				indexS++;
			}
			indexS++;
			notesS.add(tempS + " ");
			tempS = "";
		}
		while(indexA < altoLine.length()){
			while(!altoLine.substring(indexA, indexA + 1).equals(" ")){
				tempA += altoLine.substring(indexA, indexA + 1);
				indexA++;
			}
			notesA.add(tempA + " ");
			tempA = "";
			indexA++;
		}
		while(indexT < tenorLine.length()){
			while(!tenorLine.substring(indexT, indexT + 1).equals(" ")){
				tempT += tenorLine.substring(indexT, indexT + 1);
				indexT++;
			}
			notesT.add(tempT + " ");
			tempT = "";
			indexT++;
		}
		while(indexB < bassLine.length()){
			while(!bassLine.substring(indexB, indexB + 1).equals(" ")){
				tempB += bassLine.substring(indexB, indexB + 1);
				indexB++;
			}
			notesB.add(tempB + " ");
			tempB = "";
			indexB++;
		}
		sopranoLine = Song.ArrayToLine(notesS);
		altoLine = Song.ArrayToLine(notesA);
		tenorLine = Song.ArrayToLine(notesT);
		bassLine = Song.ArrayToLine(notesB);
		display = chordProgression + "\n" + sopranoLine + "\n" + altoLine + "\n" + tenorLine + "\n" + bassLine;
		return display;
	}
}
