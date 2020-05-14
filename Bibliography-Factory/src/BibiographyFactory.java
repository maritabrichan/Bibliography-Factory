//Importing the classes necessary for the program
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

//-----------------------------------------------------
//Written by: Marita Brichan and Mohona Mazumdar 
//-----------------------------------------------------
/*
 *This program is designed to generate references from given input files in the form 
 *"latexi.bib" where i ranges from 1 to 10. The end result gives 3 different output files 
 *corresponding to the style of the reference, IEEE, ACM and NJ. In short, each input file has 
 *3 output files.
 */

public class BibiographyFactory {

	//Static method that takes in an array of input files and returns a string that contains the reference to the article
	public static void processFilesForValidation(File[] files) {

		//Declaring variables and initializing variables
		//lines represent each line read by the program at a time
		String lines = "";
		//numArticles is the number of articles in each file
		int[] numArticles = new int[files.length];
		//articleNum is the number of articles 
		int articlesNum = 0;
		//fileValid is a boolean to know which files are valid and which are not
		boolean [] fileValid = new boolean[files.length];
		//latexFiles are the files opened with scanner
		Scanner[] latexFiles = new Scanner[files.length];
		//latexFile2 are a copy of latexFiles for each time a file
		Scanner latexFile2 = null;
		//valid is the number of valid files
		int valid = files.length;
		//invalid if the number of invalid files
		int invalid = 0;
		//st is the string tokenizer
		StringTokenizer st = new StringTokenizer("");
		
		//Creating 3 arrays of files that are later used for each PrintWriter array
				File[] IEEE=new File[files.length];
				File[] ACM=new File[files.length];
				File[] NJ=new File[files.length];


		//For loop that creates a Scanner object for each input file and stores in the array latexFiles 
		for(int i=0; i<files.length;i++) {
			fileValid[i]=true;
			try {
				latexFiles[i] = new Scanner(new FileInputStream(files[i]));
			}
			//Catch block if the input file cannot be opened for reading
			catch(FileNotFoundException e){
				System.out.println("Files could not be created");

			}
		}

		//For loop to keep in track the number of articles each file has
		for(int i=0; i<files.length;i++) {
			//Using the useDelimiter method to break down the articles in the file
			latexFiles[i].useDelimiter("\\{|\n|@");

			while(latexFiles[i].hasNext()) {
				lines=latexFiles[i].next();
				if(lines.contentEquals("ARTICLE")) {
					//Each time the scanner reads ARTICLE, it increments the number of articles for this file
					numArticles[i]++;
				}
			}	
		}
		
		for(int k = 0; k<files.length;k++) {

					//Creating the files for each type of reference
					IEEE[k]= new File("IEEE"+(k+1)+".json");
					ACM[k]= new File("ACM"+(k+1)+".json");
					NJ[k]= new File("NJ"+(k+1)+".json");
		}

		//Creating an 2D array of type string to store the different fields provided in the article
		//The size of the array is determined by the number of input files
		String[][] author = new String[files.length][];
		String[][] journal = new String[files.length][];
		String[][] title = new String[files.length][];
		String[][] year = new String[files.length][];
		String[][] volume = new String[files.length][];
		String[][] number = new String[files.length][];
		String[][] pages = new String[files.length][];
		String[][] keywords = new String[files.length][];
		String[][] doi = new String[files.length][];
		String[][] ISSN = new String[files.length][];
		String[][] month = new String[files.length][];

		//For loop to determine the size of the second array in the 2D array. This is done by the 
		//number of articles each file has
		for (int i=0;i<files.length;i++) {
			author[i]=new String[numArticles[i]];
			journal[i]=new String[numArticles[i]];
			title[i]=new String[numArticles[i]];
			year[i]=new String[numArticles[i]];
			volume[i]=new String[numArticles[i]];
			pages[i]=new String[numArticles[i]];
			number[i]=new String[numArticles[i]];
			keywords[i]=new String[numArticles[i]];
			doi[i]=new String[numArticles[i]];
			ISSN[i]=new String[numArticles[i]];
			month[i]=new String[numArticles[i]];
		}

		//enter a loop in order to read each line and know what it has and if its content are valid
		for(int i =0; i<files.length;i++) {
			try {
				//Initialize the number of articles for each file at 0
				articlesNum = 0;
				try {
					//open the latex file in question
					latexFile2 = new Scanner(new FileInputStream(files[i]));
				}
				//exit the program if the file could not be opened and display an exiting message
				catch(FileNotFoundException e) {
					System.out.println(files[i] + "could not be opened. Exiting the program.");
					System.exit(0);
				}
				//use a delimiter with { , \n @ because this is what we need in order to seperate the text
				latexFile2.useDelimiter("\\{|,|\n|@");
				//read the first line
				lines=latexFile2.next();
				//enter a loop that does not stop until there are no more lines to read from in the latex file
				while(latexFile2.hasNext()){
					//read the next line
					lines=latexFile2.next();

					/*see if the line is putting the author information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					if(lines.contentEquals("author=")) {
						author[i][articlesNum] = latexFile2.next();
						if(author[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"author\" is empty.");
						}
						else {
							author[i][articlesNum] = author[i][articlesNum].substring(0,(author[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the journal information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("journal=")) {
						journal[i][articlesNum] = latexFile2.next();
						if(journal[i][articlesNum].contentEquals("}")){
							invalid++;
							throw new FileInvalidException("Field \"journal\" is empty.");
						}
						else {
							journal[i][articlesNum] = journal[i][articlesNum].substring(0,(journal[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the title information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("title=")) {
						title[i][articlesNum] = latexFile2.next();
						if(title[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"title\" is empty.");
						}
						else {
							title[i][articlesNum] = title[i][articlesNum].substring(0,(title[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the year information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("year=")) {
						year[i][articlesNum] = latexFile2.next();
						if(year[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"year\" is empty.");
						}
						else {
							year[i][articlesNum] = year[i][articlesNum].substring(0,(year[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the volume information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("volume=")) {
						volume[i][articlesNum] = latexFile2.next();
						if(volume[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"volume\" is empty.");
						}
						else {
							volume[i][articlesNum] = volume[i][articlesNum].substring(0,(volume[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the pages information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("pages=")) {
						pages[i][articlesNum] = latexFile2.next();
						if(pages[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"pages\" is empty.");	
						}
						else {
							pages[i][articlesNum] = pages[i][articlesNum].substring(0,(pages[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the number information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("number=")) {
						number[i][articlesNum] = latexFile2.next();
						if(number[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"number\" is empty.");
						}
						else {
							number[i][articlesNum] = number[i][articlesNum].substring(0,(number[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the keywords information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("keywords=")) {
						keywords[i][articlesNum] = latexFile2.next();
						if(keywords[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"keywords\" is empty.");
						}
						else {
							keywords[i][articlesNum] = keywords[i][articlesNum].substring(0,(keywords[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the doi information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("doi=")) {
						doi[i][articlesNum] = latexFile2.next();
						if(doi[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"doi\" is empty.");
						}
						else {
							doi[i][articlesNum] = doi[i][articlesNum].substring(0,(doi[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the ISSN information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("ISSN=")) {
						ISSN[i][articlesNum] = latexFile2.next();
						if(ISSN[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"ISSN\" is empty.");
						}
						else {
							ISSN[i][articlesNum] = ISSN[i][articlesNum].substring(0,(ISSN[i][articlesNum].length()-1));
						}
					}

					/*see if the line is putting the month information, if so try to see if it's empty or not, in case it is empty, throw it into a fileInvalidException and count it, and in case it is valid,
					 * take the whole string, after the brackets, but not the last character, which should be the closing brckette (without the comma, since the comma is a delimiter)
					 */
					else if(lines.contentEquals("month=")) {
						month[i][articlesNum] = latexFile2.next();
						if(month[i][articlesNum].contentEquals("}")) {
							invalid++;
							throw new FileInvalidException("Field \"month\" is empty.");
						}
						else {
							month[i][articlesNum] = month[i][articlesNum].substring(0,(month[i][articlesNum].length()-1));
						}
					}

					//see if the line has ARTICLE in it, which would indicate the start of a new article, so increment the article number
					else if(lines.contentEquals("ARTICLE")) {
						articlesNum++;
					}
				}
			}
			//Catch block that catches the exception if the file is invalid and outputs a message indicating the reason
			catch (FileInvalidException e) {
				//An array that keeps track of which file is valid or not
				fileValid[i]=false;
				System.out.print("\n\nError: Detected Empty Field!\n============================\n\nProblem detected with input file: " + files[i]+". " + e.getMessage() + " Processing stopped at this point. Other empty fields may be present as well!");	
					
					//delete files
					if (IEEE[i].delete()&&ACM[i].delete()&&NJ[i].delete()) {
						System.out.println("\nAll output files corresponding to this input file were deleted");
					} 
					else {
						// File failed to delete
						System.out.println("File failed to be deleted");
					}
					
			}
		}

		//calculate the number of valid and invalid files and show it to the user
		valid -= invalid;
		System.out.print( "\n\nA total of "+invalid+" files were invalid, and could not be processed. All other "+valid+" \"Valid\" files have been created.");

		//Creating 3 arrays type PrintWriter which stores the PrintWriter objects
		//The length of the array is determined by the number of input files
		PrintWriter[] pwIEEE= new PrintWriter[files.length];
		PrintWriter[] pwACM= new PrintWriter[files.length];
		PrintWriter[] pwNJ= new PrintWriter[files.length];

		//For loop that iterates through the number of input files and creates PrintWriter objects for each valid file
		for(int k = 0; k<files.length;k++) {
			try {
				if(fileValid[k]){

					//Creating the PrintWriter objects for each array
					pwIEEE[k]= new PrintWriter(new FileOutputStream("IEEE"+(k+1)+".json"));
					pwACM[k]= new PrintWriter(new FileOutputStream("ACM"+(k+1)+".json"));
					pwNJ[k]= new PrintWriter(new FileOutputStream("NJ"+(k+1)+".json"));


					//For loop that iterates through the number of articles each file has 
					for(int j=0;j<author[k].length;j++) {

						//Writing the IEEE reference for the valid files using the information of the fields stored in their respective arrays
						pwIEEE[k].println("["+(j+1)+"] "+ author[k][j]+". \""+title[k][j]+"\", "+journal[k][j]+", vol. "+volume[k][j]+",no."+number[k][j]+
								", p."+ pages[k][j]+","+month[k][j]+""+year[k][j]+".\n");


						//Writing the NJ reference
						pwNJ[k].println("["+(j+1)+"] "+author[k][j]+". "+title[k][j]+". "+journal[k][j]+". "+volume[k][j]+", "+pages[k][j]+" ("+year[k][j]+").\n");


						//Creating a StringTokenizer object that breaks the string "author" by the delimiter "and"
						st = new StringTokenizer(author[k][j],"and");

						//Writing the ACM reference
						pwACM[k].println("["+(j+1)+"] "+st.nextToken()+ " et al. "+year[k][j]+ title[k][j]+". "+journal[k][j]+". "+volume[k][j]+","+number[k][j]+" ("+year[k][j]+
								"), "+pages[k][j]+". DOI:https://doi.org/"+doi[k][j] + ".\n");

					}
					//Closing the corresponding output files
					pwIEEE[k].close();
					pwNJ[k].close();
					pwACM[k].close();
				}

			}

			//Catch block that catches the exception when the output file cannot be created
			catch(FileNotFoundException e) 			
			{
				System.out.println("Could not create a file to write to.");				   		   
			}

		}

	}


	public static void main(String[] args) {

		//Declaring and initializing variables
		//fileChosen is the name of the file that the user wants to see
		String fileChosen= null;
		String reading;
		Scanner kb= new Scanner(System.in);
		Scanner[] sc = new Scanner [10];
		File[] files=new File[10];

		//display a welcome message
		System.out.println("Welcome to the Bibliography Factory!");


		//For loop to initialize every Scanner object to null
		for (int i=0; i<10;i++) 
		{
			sc [i] = null;

		}


		//For loop that creates Scanner object for each input file
		for (int i=0; i<10; i++)
		{


			try {
				files[i]=new File("Latex"+(i+1)+".bib");	
				sc[i] = new Scanner(new FileInputStream("Latex"+(i+1)+".bib"));
			}
			//Catch block that catches the exception when a file cannot be open for reading
			catch(FileNotFoundException e) {
				System.out.println("Could not open input file Latex"+(i+1)+".bib for reading.\n\n Please check if it exists! Program will terminate after closing the opened files.");

				//For loop that closes all input files that were created before
				for (int j=i;j>0;j--) {

					sc[j].close();
				}

				System.exit(0);
			}

		}
		
		//Creating 3 arrays type PrintWriter which stores the PrintWriter objects
		//The length of the array is determined by the number of input files
		PrintWriter[] pwIEEE= new PrintWriter[files.length];
		PrintWriter[] pwACM= new PrintWriter[files.length];
		PrintWriter[] pwNJ= new PrintWriter[files.length];

		//Creating 3 arrays of files that are later used for each PrintWriter array
		File[] IEEE=new File[files.length];
		File[] ACM=new File[files.length];
		File[] NJ=new File[files.length];
		for(int k = 0; k<files.length;k++) {
			try {

				//Creating the files for each type of reference
				IEEE[k]= new File("IEEE"+(k+1)+".json");
				ACM[k]= new File("ACM"+(k+1)+".json");
				NJ[k]= new File("NJ"+(k+1)+".json");


				//Creating the PrintWriter objects for each array
				pwIEEE[k]= new PrintWriter(new FileOutputStream(IEEE[k]));
				pwACM[k]= new PrintWriter(new FileOutputStream(ACM[k]));
				pwNJ[k]= new PrintWriter(new FileOutputStream(NJ[k]));
					
				//Closing the corresponding output files
				pwIEEE[k].close();
				pwNJ[k].close();
				pwACM[k].close();
			}


		//Catch block that catches the exception when the output file cannot be created
		catch(FileNotFoundException e) 			
		{
			System.out.println("Could not open/create a file to write to. All created files will be deleted and the program will terminate");	
			for(int s = 0; s<=k;s++) {
				
				//delete all the IEE files
				if (IEEE[s].delete()) {
					System.out.println("File was deleted");
				} 
				else {
					// File failed to delete
					System.out.println("File failed to be deleted");
				}
				
				//Delete all the ACM files
				if (ACM[s].delete()) {
					System.out.println("File was deleted");
				} 
				else {
					// File failed to delete
					System.out.println("File failed to be deleted");
				}
				
				//Delete all the NJ files
				if (NJ[s].delete()) {
					System.out.println("File was deleted");
				} 
				else {
					// File failed to delete
					System.out.println("File failed to be deleted");
				}
				
				//close all opened input files
				pwIEEE[s].close();
				pwNJ[s].close();
				pwACM[s].close();
			}
			
			//exit the program
			System.exit(0);
		}

	}

		//Using the static method and outputting the reference
		processFilesForValidation(files);

		//While that prompts the user for the output file to read

		System.out.println("\nPlease enter the name of one of the files that you need to review.\n\nIf you would like to exit the program, press q");
		fileChosen = kb.next();

		//If the user enters "q", the program exits
		if(fileChosen.equals("q")) {
			System.out.println("Thank you for using our program Bibliography Factory to generate references for your articles!");
			System.exit(0);
		}

		try {
			System.out.println("Here are the contents of the successfully created Jason File:" + fileChosen);
			//Creating a BufferedReader object that reads the output file entered by the user
			BufferedReader reader = new BufferedReader(new FileReader(fileChosen));
			while((reading= reader.readLine())!= null) {
				System.out.println(reading);
			}
			reader.close();
		}
		//Catch block that catches the exception in the case where the file does not exist
		catch(FileNotFoundException e) {
			{

				//User gets a second chance to enter another output file
				System.out.println("Could not open input file. File does not exist; possibly it could not be created!\n");
				System.out.println("However, you will be allowed another chance to enter another file name.");	
				System.out.print("Please enter the name of one of the files that you need to review: ");
				fileChosen = kb.next();
			}

			try {
				System.out.println("Here are the contents of the successfully created Jason File:" + fileChosen);
				BufferedReader reader = new BufferedReader(new FileReader(fileChosen));
				while((reading= reader.readLine())!= null) {
					System.out.println(reading);
				}
				reader.close();
			}
			//Catch block that catches the exception when the file does not exist
			catch(FileNotFoundException e1){
				System.out.println("\nCould not open input file again! Either file does not exist or could not be created.");
				System.out.println("Sorry I am unable to display your desired files! Program will exit!\n");
				System.exit(0);
			}
			//Catches block that catches the exception that might occur when reading the output file
			catch(IOException e2) {
				System.out.println("Problem deteced while reading the file " + fileChosen + "\n The program will terminate shortly \n");
				System.exit(0);
			}

		}
		//Catches block that catches the exception that might occur when reading the output file
		catch(IOException e3) {
			System.out.println("Problem detected while reading the file " + fileChosen + ".\n The program will terminate shortly \n");
			System.exit(0);
		}

		//print a goodbye message to indicate the ending od the program to the user
		System.out.println("Goodbye! Hope you have enjoyed creating the needed files using BiblographyFactory.");

		//close the kb scanner
		kb.close();
	}

}