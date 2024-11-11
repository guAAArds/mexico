
import java.util.SplittableRandom;
import java.util.Scanner;

import static java.lang.System.*;

/*
 *  The Mexico dice game
 *  See https://en.wikipedia.org/wiki/Mexico_(game)
 *
 */
public class Mexico {

	public static void main(String[] args) {
		new Mexico().program();
	}

	final SplittableRandom rand = new SplittableRandom();
	final Scanner sc = new Scanner(in);
	final int maxRolls = 3; // No player may exceed this
	final int startAmount = 3; // Money for a player. Select any
	final int mexico = 1000; // A value greater than any other

	void program() {
	        //test(); // <----------------- UNCOMMENT to test

		int pot = 0; // What the winner will get
		Player[] players; // The players (array of Player objects)
		Player current; // Current player for round
		
		players = getPlayers();
		current = getRandomPlayer(players);

		out.println("Mexico Game Started");
		statusMsg(players);

		while (players.length > 1) { // Game over when only one player left
		String cmd = getPlayerChoice(current);
		if ("r".equals(cmd) && current.nRolls < maxRolls) {
		    rollDice(current);
			current.nRolls++;
			roundMsg(current);
		}
		else if ("n".equals(cmd)) {
			Player loser = getLoser(players);
			if (allRolled(players)) {
				loser.amount--;
				pot++;
				if(loser.amount == 0){
				    players = removeLoser(players);
				}
				current = next(players, current);
				out.println("----------------------------------");
				out.println("Round done "+ loser.name + " lost!");
				out.println("Next to roll is " + current.name);
				out.println("----------------------------------");
				statusMsg(players);
				clearRoundResults(players);
				}
			else{
				current = next(players, current);
			}
		}
		else {
			out.println("?");
		}

		}
		out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
	}

	// ---- Game logic methods --------------

	boolean allRolled(Player[] ps){
		for(int i = 0; i < ps.length; i++){
			if(ps[i].fstDice == 0){
			return false;
			}
		}
		return true;
		}

    void clearRoundResults(Player[] ps){
	for(int i = 0; i != ps.length; i++){
	    ps[i].fstDice = 0;
	    ps[i].secDice = 0;
	    ps[i].nRolls  = 0;
	}
    }
	
	int getScore(Player p){
		if(p.fstDice > p.secDice){
				return (p.fstDice * 10 + p.secDice);
			}
			else {
				return (p.secDice * 10 + p.fstDice);
			}
		}

	Player next(Player[] ps, Player current){
		for(int i = 0; i != ps.length - 1; i++){
			if(ps[i] == current){
				return (ps[i+1]);
			}
		}
		return ps[0];
	}
	

    void rollDice(Player current){
	current.fstDice = rand.nextInt(6)+1;
	current.secDice = rand.nextInt(6)+1;
    }

   
    
	Player getLoser(Player[] ps){
		Player loser = ps[0];
		for(int i = 0; i != ps.length; i++){
			loser = getLowest(loser, ps[i]);
		}
		return loser;
	}
	Player getLowest (Player p1, Player p2){
		if(getScore(p1) == 21){
			return p2;
		}
		else if (getScore(p2) == 21){
			return p1;
		}
		else if(isDouble(p1) && !isDouble(p2)){
			return p2;
		}
		else if (isDouble(p2) && !isDouble(p1)){
			return p1;
		}
		else if(isDouble(p1) && isDouble(p2)){
			if(p1.fstDice >= p2.fstDice){
				return p2;
			}
			else{
				return p1;
			}
		}
		else if(getScore(p1) >= getScore(p2)){
			return p2;
		}
		else{
			return p1;
		}
	}


	boolean isDouble(Player p){
		if (p.fstDice == p.secDice){
			return true;
		}
		else{
			return false;
		}
	}

	Player[] removeLoser(Player[] ps){
		Player loser = getLoser(ps);
		Player[] players = new Player[(ps.length - 1)];
		int index = 0;
		for(int i = 0; i < ps.length; i++){
			if(ps[i] != loser){
			    players[index] = ps[i];
			    index++;
			}
		    }
		return players;
	}

	Player getRandomPlayer(Player[] players) {
		return players[rand.nextInt(players.length)];
	}

	// ---------- IO methods (nothing to do here) -----------------------

	Player[] getPlayers() {
		Player[] players = new Player[3];
		players[0] = new Player("Olle");
		players[1] = new Player("Fia");
		players[2] = new Player("Lisa");
		return players;
	}

	void statusMsg(Player[] players) {
		out.print("Status: ");
		for (int i = 0; i < players.length; i++) {
			out.print(players[i].name + " " + players[i].amount + " ");
		}
		out.println();
	}

	void roundMsg(Player current) {
		out.println(current.name + " got " + current.fstDice + " and " + current.secDice);
	}

	String getPlayerChoice(Player player) {
		out.print("Player is " + player.name + " > ");
		return sc.nextLine();
	}

	// Class for a player
	class Player {
		String name;
		int amount; // Start amount (money)
		int fstDice; // Result of first dice
		int secDice; // Result of second dice
		int nRolls; // Current number of rolls

		public Player(){

		}
		public Player(String inName){
			name    = inName;
			amount  = startAmount;
			fstDice = 0;
			secDice = 0;
			nRolls  = 0;
		}
	}
	
    
	/**************************************************
	 * Testing
	 *
	 * Test are logical expressions that should evaluate to true (and then be
	 * written out) No testing of IO methods Uncomment in program() to run test
	 * (only)
	 ***************************************************/
	void test() {
		// A few hard coded player to use for test
		// NOTE: Possible to debug tests from here, very efficient!
		Player[] ps = { new Player(), new Player(), new Player() };
		ps[0].fstDice = 2;
		ps[0].secDice = 6;
		ps[1].fstDice = 6;
		ps[1].secDice = 5;
		ps[2].fstDice = 1;
		ps[2].secDice = 1;

		out.println(getScore(ps[0]) == 62);
		out.println(getScore(ps[1]) == 65);
		out.println(next(ps, ps[0]) == ps[1]);
		out.println(getLoser(ps) == ps[0]);
		
		exit(0);
	}

}