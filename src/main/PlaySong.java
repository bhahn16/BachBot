package main;

import org.jfugue.Player;

public class PlaySong {
	public static void playSong(Song song, Player player){
		player.play("T[" + song.tempo +"]" +  " V0 " + "I" + song.instrument + " " +song.bassLine + " "  +" V1 I" + song.instrument +" " + song.tenorLine+ " V2 I" +  song.instrument + " " +  song.altoLine +  " V3 " + " I" +song.instrument + " " + song.sopranoLine);
	}
}
