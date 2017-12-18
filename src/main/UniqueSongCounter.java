/*
 * FYI I wrote this class before I learned about Hashing
 * Haven't gotten around to reimplimenting this with a hash map and .data files
 */

package main;

import java.util.ArrayList;

public class UniqueSongCounter {

	public static void main(String[] args){
		Song song = new Song();
		ArrayList<Song> pastSongs = new ArrayList<Song>();
		int numberOfUniqueSongs = 1; 
		int numberOfSongsTested = 1;
		pastSongs.add(song);
		while(true){
			song = new Song();
			numberOfSongsTested++;
			if(unique(song,pastSongs)){
				numberOfUniqueSongs++;
				pastSongs.add(song);
				System.out.println("Unique Songs: " + numberOfUniqueSongs + " Number of Songs Tested: " + numberOfSongsTested);
			}
		}
	}
	public static boolean unique(Song song, ArrayList<Song> pastSongs){
		for(int i = 0; i < pastSongs.size(); i++){
			if(pastSongs.get(i).equals(song)){
				return false;
			}
		}
		return true;
	}
	private static boolean different(Song song1,Song song2){
		
		if(song1.bassLine.equals(song2.bassLine) && song1.tenorLine.equals(song2.tenorLine) &&song1.altoLine.equals(song2.altoLine) &&song1.sopranoLine.equals(song2.sopranoLine))
			return false;
		return true;
	}
}
