package Cracker;

import java.io.IOException;

/*
 * Gregory Pellegrin
 * pellegrin.gregory.work@gmail.com
 * 
 * Trouve le mot de passe d'une archive .zip ou .rar
 */
public final class Cracker
{
	private char dictionaryAll [] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private char dictionaryNumber [] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private char dictionaryTextAll [] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private char dictionaryTextLower [] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	private char dictionaryTextUpper [] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	private char dictionary [];
	private String pathFile;
	private String command;
	private double oldTime;
	private int minLengthPassword;
	private int maxLengthPassword;
	private int displayTime;
	private int totalFrame;
	private int frame;
	
	public static final int ZIP = 0;
	public static final int RAR = 1;
	
	public static final int ALL = 0;
	public static final int NUMBER = 1;
	public static final int TEXT_ALL = 2;
	public static final int TEXT_LOWER = 3;
	public static final int TEXT_UPPER = 4;
	
	/*
	 * pathFile : Si chemin relatif, partir de Cracker/ ("./src/main/resources/CrackerPackage/fichier.zip")
	 * typeFile : Extension du fichier (.zip) (.rar)
	 * typeCrack : Caracteres du password (Chiffres + Minuscules + Majuscules) (Chiffres) (Minuscules + Majuscules) (Minuscules) (Majuscules)
	 * minLengthPassword : Taille minimale du password (azerty = 1,2,3,4,5,6 minLengthPassword)
	 * maxLengthPassword : Taille maximale du password (azerty = 6 maxLengthPassword)
	 * (Calcul du password de minLengthPassword a maxLengthPassword)
	 * displayTime : Temps d'apparition des statistiques (Millisecondes)
	 */
	public Cracker (String pathFile, int typeFile, int typeCrack, int minLengthPassword, int maxLengthPassword, int displayTime)
	{
		this.pathFile = " " + pathFile;
		
		if (typeFile == Cracker.ZIP)
			this.command = "unzip -P ";
		else if (typeFile == Cracker.RAR)
			this.command = "unrar x -p";
		
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
		this.totalFrame = 0;
		this.frame = 0;
	}
	
	public void start ()
	{
		this.update("", this.maxLengthPassword);
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
		//System.out.println(this.command + password + this.pathFile);
		
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
			this.totalFrame = this.totalFrame + this.frame;

			/* abcdefg [666 password/sec] [6666 password/total] */
			System.out.println(password + " [" + (this.frame / (this.displayTime / 1000)) + " password/sec] [" + this.totalFrame + " password/total]");
			
			this.oldTime = System.currentTimeMillis();
			this.frame = 0;
		}
	}
	
	public static void main (String [] args)
	{
		Cracker crack = new Cracker ("./src/main/resources/Cracker/fichier.zip", Cracker.ZIP, Cracker.TEXT_LOWER, 5, 6, 100000);
		
		crack.start();
	}
}