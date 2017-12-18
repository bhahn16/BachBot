package main;

import org.jfugue.Player;

public class ConsoleRunner {
	public static void main(String[] args){
		Song song = new Song();
		System.out.println(DisplaySong.displaySong(song));
		Player player = new Player();
		PlaySong.playSong(song, player);
	}
}
