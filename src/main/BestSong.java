package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jfugue.Player;

public class BestSong {
	public static void main(String[] args) throws IOException{
		Song song = findBestSong(1000);
		//System.out.println(song.totalRange);
		//System.out.println(song.bassVoice);
		Player player = new Player();
		PlaySong.playSong(song, player);
		File file = new File("C:/Users/josep/Desktop/tempSong1.midi");
		player.saveMidi("T[" + song.tempo +"]" +  " V0 " + "I" + song.instrument + " " +song.bassLine + " "  +" V1 I" + song.instrument +" " + song.tenorLine+ " V2 I" +  song.instrument + " " +  song.altoLine +  " V3 " + " I" +song.instrument + " " + song.sopranoLine, file);
	}
	public static Song findBestSong(int iterations)
	{
		int highScore, currentScore; 
		highScore = -10000;
		Song currentSong, bestSong;
		bestSong = null; //placeholder
		for(int i = 0; i < iterations; i++){
			if(i % 100 == 0)
				System.out.println("Writing and testing song: " + i);
			currentSong = new Song();
			currentScore = 100;
			currentScore = currentScore - (5 * checkForParallels(currentSong.totalRange, currentSong.bassVoice, currentSong.tenorVoice));
			currentScore = currentScore - 5 * checkForParallels(currentSong.totalRange, currentSong.altoVoice, currentSong.sopranoVoice);
			currentScore = currentScore - 5 * checkForParallels(currentSong.totalRange, currentSong.tenorVoice, currentSong.altoVoice);
			currentScore = currentScore - checkForLeaps(currentSong.bassVoice,currentSong.totalRange);
			currentScore = currentScore - checkForLeaps(currentSong.tenorVoice,currentSong.totalRange);
			currentScore = currentScore - checkForLeaps(currentSong.altoVoice,currentSong.totalRange);
			currentScore = currentScore - checkForLeaps(currentSong.sopranoVoice,currentSong.totalRange);
			//System.out.println("Number of leaps:" + checkForLeaps(currentSong.sopranoVoice, currentSong.totalRange));
			if(currentScore > highScore){
				highScore = currentScore;
				bestSong = currentSong;
				System.out.println("NEW BEST SONG! Score of: " + highScore);
			}
		}
		return bestSong;
	}
	
	private static int checkForLeaps(ArrayList<String> part, ArrayList<String> range){
		int leaps = 0; //a 4th counts as 1, 5th or more is 5
		for(int chord = 0; chord < part.size() - 1; chord++){
			int currentNoteIndex = range.indexOf(part.get(chord).trim().substring(0, part.get(chord).trim().length() - 1));
			//System.out.println((part.get(chord)));//.substring(0, part.get(chord).length() - 1)));
			int nextNoteIndex = range.indexOf(part.get(chord + 1).trim().substring(0, part.get(chord + 1).trim().length() - 1));
			//System.out.println(currentNoteIndex + " " + nextNoteIndex);
			if(Math.abs(currentNoteIndex - nextNoteIndex) == 4){
				leaps += 1;
			}
			else if(Math.abs(currentNoteIndex - nextNoteIndex) > 4){
				leaps += 5;
			}
		}
		return leaps;
	}
	private static int checkForParallels(ArrayList<String> totalRange, ArrayList<String> part1, ArrayList<String> part2) {
		ArrayList<Integer> part1Indexes = new ArrayList<Integer>();
		ArrayList<Integer> part2Indexes = new ArrayList<Integer>();
		int numberOfParallels = 0;
		for(int chord = 0; chord < part1.size(); chord++){ //add indexes of each note in line
			part1Indexes.add(totalRange.indexOf(part1.get(chord).substring(0, part1.get(chord).length() - 2)));
			//System.out.println(part1.get(chord).substring(0, part1.get(chord).length() - 2));
			part2Indexes.add(totalRange.indexOf(part2.get(chord).substring(0, part2.get(chord).length() - 2)));
		}
		for(int chord = 0; chord < part1.size() - 1; chord++){ //compare indexes to see if there are perfect fifths
			if((Math.abs(part1Indexes.get(chord) - part2Indexes.get(chord)) == 4 || Math.abs(part1Indexes.get(chord) - part2Indexes.get(chord)) == 7) && (Math.abs(part1Indexes.get(chord + 1) - part2Indexes.get(chord + 1)) == 4 || Math.abs(part1Indexes.get(chord + 1) - part2Indexes.get(chord + 1)) == 7) && !(part1Indexes.get(chord) == part1Indexes.get(chord + 1) && part2Indexes.get(chord) == part2Indexes.get(chord + 1))){
				numberOfParallels++;
				
				
			}
		}
		//System.out.println(numberOfParallels);
		return numberOfParallels; //-5 for each parallel
	}
}
