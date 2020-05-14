//-----------------------------------------------------
// Written by: Marita Brichan and Mohona Mazumdar
//-----------------------------------------------------

/*
 * *This program is designed to generate references from given input files in the form 
 *"latexi.bib" where i ranges from 1 to 10. The end result gives 3 different output files 
 *corresponding to the style of the reference, IEEE, ACM and NJ. In short, each input file has 
 *3 output files.
 *
 *
 */
public class FileInvalidException extends Exception {

	/**
	 * This constructor takes the exception and outputs a default message
	 */
	public FileInvalidException() {
		super("Error: Input file cannot be parsed due to missing information (i.e. month={}, title={}, etc. ");
	}
	
	/**
	 * 
	 * this constructor takes the exceptiong and outputs a message chosen
	 * @param s attribute
	 */
	public FileInvalidException(String s) {
		super(s);
	}
	
	/**
	 * this is an accessor method
	 */
	public String getMessage(){	
		return super.getMessage();
	}
}

