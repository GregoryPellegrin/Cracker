/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 * 
 * Trouve le mot de passe d'une archive .zip ou .rar
 */

package Cracker;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Cracker
{
	private final char dictionaryAll [] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private final char dictionaryNumber [] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private final char dictionaryTextAll [] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private final char dictionaryTextLower [] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	private final char dictionaryTextUpper [] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private char dictionary [];
	private final String pathFile;
	private final String nameFile;
	private final String command;
	private double oldTime;
	private double totalPassword;
	private double totalPasswordDone;
	private double frame;
	private final int minLengthPassword;
	private final int maxLengthPassword;
	private final int displayTime;
	
	public static final String ZIP = "unzip -P ";
	public static final String ZIP7 = "7z x -yp";
	public static final String RAR = "unrar x -p";
	
	public static final int ALL = 0;
	public static final int NUMBER = 1;
	public static final int TEXT_ALL = 2;
	public static final int TEXT_LOWER = 3;
	public static final int TEXT_UPPER = 4;
	
	/*
	 * pathFile : Si chemin relatif, partir de Cracker/ ("./src/main/resources/CrackerPackage/fichier.zip")
	 * nameFile : Nom d'un fichier contenu dans pathFile (Leger de preference)
	 * typeCrack : Caracteres du password (Chiffres + Minuscules + Majuscules) (Chiffres) (Minuscules + Majuscules) (Minuscules) (Majuscules)
	 * minLengthPassword : Taille minimale du password (azerty = 1,2,3,4,5,6 minLengthPassword)
	 * maxLengthPassword : Taille maximale du password (azerty = 6 maxLengthPassword)
	 * (Calcul du password de minLengthPassword a maxLengthPassword)
	 * displayTime : Temps d'apparition des statistiques (Millisecondes)
	 */
	public Cracker (String pathFile, String nameFile, int typeCrack, int minLengthPassword, int maxLengthPassword, int displayTime)
	{
		this.pathFile = " " + pathFile;
		this.nameFile = " " + nameFile;
		
		if (pathFile.endsWith(".zip"))
			this.command = Cracker.ZIP7;
		else if (pathFile.endsWith(".rar"))
			this.command = Cracker.RAR;
		else
			this.command = "Should be zip or rar";
		
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
		
		this.totalPassword = 0;
		for (int i = this.minLengthPassword; i <= this.maxLengthPassword; i++)
			this.totalPassword = this.totalPassword + Math.pow(this.dictionary.length, i);
	}
	
	public void start ()
	{
		this.update("", this.maxLengthPassword);
	}
	
	public void start (String characterStart)
	{
		int indexStart = 0;
		
		for (int i = 0; i < this.dictionary.length; i++)
			if (characterStart.equals(String.valueOf(this.dictionary[i])))
				indexStart = i;
		
		this.update("", indexStart, this.maxLengthPassword);
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
	
	private void update (String password, int indexStart, int maxLengthPassword)
	{
		if (maxLengthPassword > 0)
			for (int i = indexStart; i < this.dictionary.length; i++)
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
			Process process = Runtime.getRuntime().exec(this.command + password + this.pathFile + this.nameFile);
			
			if (process.waitFor() == 0)
			{
				Process extract = Runtime.getRuntime().exec(this.command + password + this.pathFile);
				
				System.out.println("Password [" + password + "]");
				System.exit(0);
			}
		}
		catch (InterruptedException e)
		{
			System.out.println(e);
		}
		catch (IOException e)
		{
			System.out.println(e);
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
			System.out.printf("[%s] [%s] [%.0f sec] [%.0f total] [%.0f restant] [%.0f sec restant]\n", password, this.command, passwordSec, this.totalPasswordDone, passwordRestant, secRestant);
			
			this.oldTime = System.currentTimeMillis();
			this.frame = 0;
		}
	}
	
	public static void main (String [] args)
	{
		if ((args.length == 6) || (args.length == 7))
		{
			Cracker crack = new Cracker (args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
			
			if (args.length == 7)
				crack.start(args[6]);
			else
				crack.start();
		}
		else
		{
			System.out.println("1 Nom de l'archive");
			System.out.println("2 Nom d'un fichier contenu dans l'archive (Leger de preference)");
			System.out.println("3 Choix du dictionnaire (0 Tout, 1 Nombre, 2 Lettres, 3 Minuscules, 4 Majuscules)");
			System.out.println("4 Taille minimum du mot de passe");
			System.out.println("5 Taille maximum du mot de passe");
			System.out.println("6 Temps entre les affichages des statistiques");
			System.out.println("7 Caractere par lequel commencer");
			
			System.out.println("java -jar cracker fichier.zip|rar 3 4 6 100000 [x]");
		}
	}
}