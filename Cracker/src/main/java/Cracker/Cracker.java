/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 * 
 * Trouve le mot de passe d'une archive .zip ou .rar
 */

package Cracker;

import java.io.IOException;

public final class Cracker
{
	private final char dictionaryAll [] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private final char dictionaryNumber [] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private final char dictionaryTextAll [] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private final char dictionaryTextLower [] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	private final char dictionaryTextUpper [] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private char dictionary [];
	private final String pathFile;
	private final String command;
	private double oldTime;
	private double totalPassword;
	private double totalPasswordDone;
	private double frame;
	private final int minLengthPassword;
	private final int maxLengthPassword;
	private final int displayTime;
	
	public static final String ZIP = "unzip -P ";
	public static final String RAR = "unrar x -p";
	
	public static final int ALL = 0;
	public static final int NUMBER = 1;
	public static final int TEXT_ALL = 2;
	public static final int TEXT_LOWER = 3;
	public static final int TEXT_UPPER = 4;
	
	/*
	 * pathFile : Si chemin relatif, partir de Cracker/ ("./src/main/resources/CrackerPackage/fichier.zip")
	 * typeCrack : Caracteres du password (Chiffres + Minuscules + Majuscules) (Chiffres) (Minuscules + Majuscules) (Minuscules) (Majuscules)
	 * minLengthPassword : Taille minimale du password (azerty = 1,2,3,4,5,6 minLengthPassword)
	 * maxLengthPassword : Taille maximale du password (azerty = 6 maxLengthPassword)
	 * (Calcul du password de minLengthPassword a maxLengthPassword)
	 * displayTime : Temps d'apparition des statistiques (Millisecondes)
	 */
	public Cracker (String pathFile, int typeCrack, int minLengthPassword, int maxLengthPassword, int displayTime)
	{
		this.pathFile = " " + pathFile;
		
		if (pathFile.endsWith(".zip"))
			this.command = Cracker.ZIP;
		else if (pathFile.endsWith(".rar"))
			this.command = Cracker.RAR;
		else
			this.command = "Not a zip or rar";
		
		if (typeCrack == Cracker.ALL)
			this.dictionary = this.dictionaryAll;
		else if (typeCrack == Cracker.NUMBER)
			this.dictionary = this.dictionaryNumber;
		else if (typeCrack == Cracker.TEXT_ALL)
			this.dictionary = this.dictionaryTextAll;
		else if (typeCrack == Cracker.TEXT_LOWER)
			this.dictionary = this.dictionaryTextLower;
		else if (typeCrack == Cracker.TEXT_UPPER)
			this.dictionary = this.dictionaryTextUpper;
		
		this.minLengthPassword = maxLengthPassword - minLengthPassword;
		this.maxLengthPassword = maxLengthPassword;
		this.displayTime = displayTime;
		this.oldTime = 0;
		this.totalPasswordDone = 0;
		this.frame = 0;
		
		this.totalPassword = this.dictionary.length;
		for (int i = 1; i < this.maxLengthPassword; i++)
			this.totalPassword = this.totalPassword * this.dictionary.length;
	}
	
	public void start ()
	{
		this.start("");
	}
	
	public void start (String password)
	{
		this.update(password, this.maxLengthPassword);
	}
	
	private void update (String password, int maxLengthPassword)
	{
		if (maxLengthPassword > 0)
			for (int i = 0; i < this.dictionary.length; i++)
				this.update(password + this.dictionary[i], maxLengthPassword - 1);
		
		if (maxLengthPassword <= this.minLengthPassword)
		{
			this.execute(password);
			this.stat(password);
		}
	}
	
	private void execute (String password)
	{
		try
		{
			Runtime.getRuntime().exec(this.command + password + this.pathFile);
		}
		catch (IOException e)
		{
		}
	}
	
	private void stat (String password)
	{
		this.frame = this.frame + 1;
		
		if ((System.currentTimeMillis() - this.oldTime) >= this.displayTime)
		{
			this.totalPasswordDone = this.totalPasswordDone + this.frame;
			
			double passwordSec = this.frame / (this.displayTime / 1000);
			double passwordRestant = this.totalPassword - this.totalPasswordDone;
			double secRestant = (this.totalPassword - this.totalPasswordDone) / passwordSec;
			
			/* abcdefg [unzip -p] [666 sec] [6666 total] [66666 restant] [666666 sec restant]*/
			System.out.println(password + " " +
								"[" + this.command + "] " +
								"[" + passwordSec + " sec] " +
								"[" + this.totalPasswordDone + " total] " +
								"[" + passwordRestant + " restant] " +
								"[" + secRestant + " sec restant]");
			
			this.oldTime = System.currentTimeMillis();
			this.frame = 0;
		}
	}
	
	public static void main (String [] args)
	{
		if (args.length == 5)
		{
			Cracker crack = new Cracker (args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
			
			crack.start();
		}
		else if (args.length == 6)
		{
			Cracker crack = new Cracker (args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
			
			crack.start(args[5]);
		}
		else
		{
			System.out.println("1 : Nom du fichier");
			System.out.println("2 : Choix du dictionnaire (0 Tout, 1 Nombre, 2 Lettres, 3 Minuscules, 4 Majuscules)");
			System.out.println("3 : Taille minimum du mot de passe");
			System.out.println("4 : Taille Maximum du mot de passe");
			System.out.println("5 : Temps entre les affichages des statistiques");
			System.out.println("6 : Mot de passe par ou commencer");
			
			System.out.println("java -jar cracker fichier.zip|rar 3 4 6 100000 [abdge]");
		}
	}
}