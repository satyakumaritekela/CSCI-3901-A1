import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DataTransformer {

	// global variables that are used across the program 
	
	Map<String,ArrayList<Object>> fileData;
	int totalDataRows;
	int totalDataColumns;
	List<String> columnNamesArray;
	List<String> columnData;
	
	/**** constructor for initializing variables ****/
	
	// creating a hash map for storing file data in a order
	// lists for storing the column data
	
	public DataTransformer() {		
		fileData = new LinkedHashMap<String,ArrayList<Object>>();
		totalDataRows = 0;
		totalDataColumns = 0;
		columnNamesArray = null;
		columnData = null;		
	}
	
	/**** constructor ends ****/
	
	/**** method starts that will clear all the data in the object ****/
	
	public boolean clear() {
		// checking the data structure stored whether is loaded or not and return the function
		if(fileData.isEmpty()) {
	    	System.out.println("File has no data to clear");
			return false;
	    }
		// if data is stored then delete all the data present
		else {
			columnNamesArray = null;
			fileData.clear();
			return true;
	    }		
	}
	
	/**** method ends ****/
	
	
	/**** method starts that reads the content of the file to the object 
	 @throws FileNotFoundException  ****/
	
	public Integer read(String filename) {
		// check the filename whether its empty by calculating the length or null
		if(filename != null && filename.length() > 0) {
			
			File fileName = new File(filename);		// creating a file object to access the file
			boolean isExist = fileName.exists();
			
			// checking file if its exists or not
			if(isExist) {
				File filePath = new File(filename);		//creating a file object that needs the file path
			    try {
					@SuppressWarnings("resource")
					Scanner fileReader = new Scanner(filePath);
					
					// reading each line using hasNext function
					if(fileReader.hasNext()) {
						String firstLine = fileReader.nextLine();	//reading columnsNames Row
						
						// checking the first line length to restrict to 80 characters
						if(firstLine.length() > 80) {
							System.out.println("No Line should be more than 80 characters");
							columnNamesArray = null;
				    		fileData.clear();
							return 0;
						}
						// loading the columnNames into hash keys and storing respective column Data in ArrayList
						else {
							columnNamesArray = new ArrayList<>(Arrays.asList(firstLine.split("\t")));	// retrieving columnNames using split function
						    totalDataColumns = columnNamesArray.size();
						    // restrict total number of columns to 10
							if(totalDataColumns <= 10) {
								for(String columnNameAttribute : columnNamesArray){
									// created a validateColumnName to validate the necessary conditions
							    	if(!validateColumnName(columnNameAttribute, new ArrayList<Object>(), "old")) {
							    		return 0;
							    	};
						        }
							}
							else {
								System.out.println("The number of columns should be at most of 10");
							}
						}
						// check the columnData whether present or not by scanning through hasNext function
						if(!fileReader.hasNext()) {
							System.out.println("The file is having only Column Name. It should have column Data");
							columnNamesArray = null;
				    		fileData.clear();
							return 0;
						}
					    
						while(fileReader.hasNextLine()) {
							String nextLine = fileReader.nextLine();
							// restrict the line data to 80 characters
							if(nextLine.length() > 80) {
								System.out.println("No Line should be more than 80 characters");
								columnNamesArray = null;
					    		fileData.clear();
								return 0;
							}
							else {
								columnData = new ArrayList<>(Arrays.asList(nextLine.split("\t")));	// retrieving Column Data of each row using split function
								if(columnData.size() != totalDataColumns) {
									System.out.println("There are improper columns/rows of data. Please check the data,It shouldn't be null");
									columnNamesArray = null;
						    		fileData.clear();
						    		return 0;
								}
								// checking the valid type of the data and loading in arrayList to the respective columnNames
							    for(int i = 0; i < columnData.size(); i++){
							    	try {
							    		// integer numbers can be negative or positive and cannot contain alphabets in between
								    	if(columnData.get(i).matches("^-?[0-9]\\d*(\\d+)?$")) {
							                fileData.get(columnNamesArray.get(i)).add(Integer.parseInt(columnData.get(i)));
								    	}
								    	else if(columnData.get(i).matches("^[a-z\\sA-Z]*$")){
							                fileData.get(columnNamesArray.get(i)).add(columnData.get(i));							    		
								    	}
								    	// if the data is null or empty, handle the case by not allowing the data to read
								    	else {
									    	System.out.println("There are improper coloumns/rows of data,Data should contains only alphabets or integers / data shouldn't be null value");
											columnNamesArray = null;
								    		fileData.clear();
											return 0;
								    	}							    		
							    	}
							    	// If the data is null or not in number format, handle using these catch statements
							    	catch(NumberFormatException ne) {
								    	System.out.println("There are improper coloumns/rows of data,Data should contains only alphabets or integers / data shouldn't be null value");
										columnNamesArray = null;
							    		fileData.clear();	
							    		return 0;
							    	}
							    	catch(NullPointerException ne) {
								    	System.out.println("There are improper coloumns/rows of data,Data should contains only alphabets or integers / data shouldn't be null value");
										columnNamesArray = null;
							    		fileData.clear();
							    		return 0;
							    	}
					            }
							    // increment the number of rows after loading
							    totalDataRows++;
							}
						}			
					}
					// handle if data is not present
					else {
						System.out.println("File has no data/ File is Empty to read");
					}
					// close the fileReader from stopping to read the file further
					fileReader.close();	
			    }
			    // handle whether if the file do not found
			    catch(FileNotFoundException fe) {
			    	System.out.println("FileName Doesn't Exists/FileName is Empty");
			    }
			    catch(IndexOutOfBoundsException ie) {
			    	System.out.println("There are improper coloumns/rows of data, data shouldn't be null value");
			    }
			}
			else {
				System.out.println("FileName Doesn't Exists/FileName is Empty. Please check the location correctly");
			}
		}
		else {
			System.out.println("FileName/FilePath has sent Null or No filename provided");
		}
		// return total number of rows that are loaded into the data structure		
		return totalDataRows;		
	}
	
	/**** method ends ****/
	
	
	/**** method starts that creates a new column with new name ****/
	// validate the columnName satisfying the conditions and load the default columnData and load the new column to the data structure created
	public boolean newColumn(String columnName) {
		ArrayList<Object> newColumnList = new ArrayList<Object>();
		//validate the columnName if it is unique
		if(validateColumnName(columnName, newColumnList, "new")) {
			// checking if columnData is present or not
			if(columnNamesArray == null) {
				System.out.println("Cannot add new Column Name, because no data exists");
				return false;
			}
			else {
				// restrict the number of columns to 10
				if(columnNamesArray.size() < 10) {
					columnNamesArray.add(columnName);	
					if(checkLineStatus(columnNamesArray)) {		// check the line restricting to 80 characters
						columnNamesArray.remove(columnName);
						System.out.println("Number of characters in each line must be no more than 80");
						return false;	
					}
					for(int i = 0; i < totalDataRows; i++) {
						newColumnList.add(0);
					}		
					fileData.put(columnName, newColumnList);	// if all the conditions satisfy add the new column data to the data structure and return true
					System.out.println("Successfully added new column");
					return true;				
				}
				else {
					System.out.println("Number of Columns must be no more than 10.Cannot be added new column");
					return false;				
				}				
			}
		}
		else {
			System.out.println("Cannot be added new column");
			return false;
		}		
	}
	
	/**** method ends ****/
	
	
	/**** method starts that applies the given equation to the data in the object ****/
	
	//	applying the equation to the data object by checking the conditions and returning the number of rows that are calculated
	// 	using switch case statement and do respective operations on the column Data
	// extract the equation to the list and do operations on the column Data
	public Integer calculate(String equation) {
		// equation should not be empty or null
		if(equation.trim().isEmpty() == true || equation == null) {
			System.out.println("The equation should not be empty or null");
			return 0;
		}
		if(fileData.isEmpty()) {
			System.out.println("Please load the object properly, The Data Object is empty (or) There are improper coloumns/rows of data, data shouldn't be null value");
			return 0;
		}
		// creating the array list to store the operands and operators
		List<String> equationAttributes = new ArrayList<>(Arrays.asList(equation.trim().split("\\s+")));	
		
		if(!equationAttributes.contains("=")) {		// equation must contain = sign in order to perform the operation
			System.out.println("Please check the equation. It should contain '=' in order to complete the equation");
			return 0;
		}
				
		if(equationAttributes.size() == 3 || equationAttributes.size() == 5) {
			
			String equationColumnName = presentOrNot(equationAttributes.get(0));
			
			ArrayList<Integer> equationColumnDataOne = new ArrayList<Integer>();
			ArrayList<Integer> equationColumnDataTwo = new ArrayList<Integer>();
			
			int equationOperandOneInt = 0;
			int equationOperandTwoInt = 0;
			String operandOneType = null;
			boolean canDoOperation = false;
			  
			// evaluating the operands to the right conditions must be integers or strings and check the spaces in between
			if(equationAttributes.get(2).matches("^-?[a-zA-Z]*$")) {
				if(!equationAttributes.get(2).matches("^[a-zA-Z]*$")) {
					System.out.println("Check the first operand used in the equation cannot be negative");	// string cannot be negative in the equation
					return 0;
				}
				String equationOperandOne = equationAttributes.get(2);
				operandOneType = "s";
				try {
					// check the columnNames used in the equation present in data or not using presentOrNot method
					String returnedequationOperandOne = presentOrNot(equationOperandOne);
					String returnedequationColumnName = presentOrNot(equationColumnName);
					
					if((returnedequationOperandOne != "") && (returnedequationColumnName  !="")) {
						for(int i = 0 ; i < totalDataRows ; i++) {
							equationColumnDataOne.add(i,(int)(fileData.get(returnedequationOperandOne).get(i)));
						}						
					}	
					else {
				    	System.out.println("The column name used as a first operand in the equation not found in column Data. Please check the equation once");
			    		return 0;						
					}
				}
		    	catch(NullPointerException ne) {
			    	System.out.println("There are improper coloumns/rows of data, Data should contains only alphabets or integers / data shouldn't be null value");
		    		return 0;
		    	}
				catch(ClassCastException ce) {
					System.out.println("The data should be Integer cannot be other format to perform operations");
					return 0;
				}
			}
			else if(equationAttributes.get(2).matches("^-?[0-9]\\d*(\\d+)?$")) {
				equationOperandOneInt = Integer.parseInt(equationAttributes.get(2));
	            operandOneType = "i";
	    	}
			else {
				System.out.println("Please check the equation. Check the spaces in between or operators used which can be only integers or strings");
				return 0;
			}
			
			if(equationAttributes.size() > 3) {

				String equationOperator = equationAttributes.get(3);
				String operandTwoType = null;
				
				if(equationAttributes.get(4).matches("^-?[a-zA-Z]*$")) {
					if(!equationAttributes.get(4).matches("^[a-zA-Z]*$")) {
						System.out.println("Check the second operand used in the equation cannot be negative");
						return 0;
					}
					String equationOperandTwo = equationAttributes.get(4);
					operandTwoType = "s";

					// check the columnNames used in the equation present in data or not using presentOrNot method
					String returnedequationOperandTwo = presentOrNot(equationOperandTwo);
					String returnedequationColumnName = presentOrNot(equationColumnName);
					
					if((returnedequationOperandTwo != "") && (returnedequationColumnName  !="")) {
						for(int i = 0 ; i < totalDataRows ; i++) {
							equationColumnDataTwo.add(i,(int)(fileData.get(returnedequationOperandTwo).get(i)));
						}						
					}	
					else {
				    	System.out.println("The column name used as a second operand in the equation not found in column Data. Please check the equation once");
			    		return 0;						
					}					
				}
				else if(equationAttributes.get(4).matches("^-?[0-9]\\d*(\\d+)?$")) {
					equationOperandTwoInt = Integer.parseInt(equationAttributes.get(4));
		            operandTwoType = "i";
		    	}
		    	else {
			    	System.out.println("The second operand in the equation not found in column Data. Please check the equation once. It should be integer or string");
					return 0;
		    	}	
				// switch case for doing the operations on each column data
				// check the type of data to do operation on same type of data by using validatecolumnDataTypes function
				// store the calculated data to the data structure created
				switch(operandOneType+equationOperator+operandTwoType) {
					
					case "s+s" : for(int i = 0 ; i < totalDataRows ; i++) {	
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationColumnDataOne.get(i), equationColumnDataTwo.get(i)) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationColumnDataOne.get(i) + equationColumnDataTwo.get(i));							
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;
					case "s+i" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationColumnDataOne.get(i), equationOperandTwoInt) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationColumnDataOne.get(i) + equationOperandTwoInt);						
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;
					case "i+s" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationOperandOneInt, equationColumnDataTwo.get(i)) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationOperandOneInt + equationColumnDataTwo.get(i));						
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;
					case "i+i" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationOperandOneInt, equationOperandTwoInt) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationOperandOneInt + equationOperandTwoInt);						
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;								
					case "s-s" : for(int i = 0 ; i < totalDataRows ; i++) {	
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationColumnDataOne.get(i), equationColumnDataTwo.get(i)) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationColumnDataOne.get(i) - equationColumnDataTwo.get(i));					
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}	
								 }
								 break;
					case "s-i" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationColumnDataOne.get(i), equationOperandTwoInt) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationColumnDataOne.get(i) - equationOperandTwoInt);					
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;
					case "i-s" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationOperandOneInt, equationColumnDataTwo.get(i)) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationOperandOneInt - equationColumnDataTwo.get(i));				
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;
					case "i-i" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationOperandOneInt, equationOperandTwoInt) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationOperandOneInt - equationOperandTwoInt);			
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;								
					case "s*s" : for(int i = 0 ; i < totalDataRows ; i++) {		
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationColumnDataOne.get(i), equationColumnDataTwo.get(i)) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationColumnDataOne.get(i) * equationColumnDataTwo.get(i));			
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;
					case "s*i" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationColumnDataOne.get(i), equationOperandTwoInt) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationColumnDataOne.get(i) * equationOperandTwoInt);			
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;
					case "i*s" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationOperandOneInt, equationColumnDataTwo.get(i)) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationOperandOneInt * equationColumnDataTwo.get(i));		
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;
					case "i*i" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationOperandOneInt, equationOperandTwoInt) ? true : false;
									if(canDoOperation) {
										fileData.get(equationColumnName).set(i,equationOperandOneInt * equationOperandTwoInt);		
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}
								 }
								 break;								
					case "s/s" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationColumnDataOne.get(i), equationColumnDataTwo.get(i)) ? true : false;
									if(canDoOperation) {
										try {	// the calculated resulted should rounded to nearest integer
											fileData.get(equationColumnName).set(i,Math.round((float)equationColumnDataOne.get(i) / (float)equationColumnDataTwo.get(i)));
										}
										catch(ArithmeticException ae) {	// handle with catch for the numbers divided by zero
											System.out.println("Number cannot be divided by zero. Please change the divisor");
											return 0;
										}	
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}								
								 }
								 break;
					case "s/i" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationColumnDataOne.get(i), equationOperandTwoInt) ? true : false;
									if(canDoOperation) {
										try {
											fileData.get(equationColumnName).set(i,Math.round((float)equationColumnDataOne.get(i) / (float)equationOperandTwoInt));
										}
										catch(ArithmeticException ae) {
											System.out.println("Number cannot be divided by zero. Please change the divisor");
											return 0;
										}	
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}								
								 }
								 break;
					case "i/s" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationOperandOneInt, equationColumnDataTwo.get(i)) ? true : false;
									if(canDoOperation) {
										try {
											fileData.get(equationColumnName).set(i,Math.round((float)equationOperandOneInt / (float)equationColumnDataTwo.get(i)));
										}
										catch(ArithmeticException ae) {
											System.out.println("Number cannot be divided by zero. Please change the divisor");
											return 0;
										}	
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}								
								 }
								 break;
					case "i/i" : for(int i = 0 ; i < totalDataRows ; i++) {
									canDoOperation = validateColumnDataTypes(fileData.get(equationColumnName).get(i),equationOperandOneInt, equationOperandTwoInt) ? true : false;
									if(canDoOperation) {
										try {
											fileData.get(equationColumnName).set(i,Math.round((float)equationOperandOneInt / (float)equationOperandTwoInt));
										}
										catch(ArithmeticException ae) {
											System.out.println("Number cannot be divided by zero. Please change the divisor");
											return 0;
										}	
									}
									else {
										System.out.println("Can do operations on same type. please check the equation and columnData");
										return 0;
									}								
								 }
								 break;
					default : System.out.println("Please enter a right operator for the calculations. It should be among +, -, *, / only");
							  return 0;
				}
			}
			else {
				if(operandOneType == "s") {
					for(int i = 0 ; i < totalDataRows ; i++) {	
						canDoOperation = validateColumnDataType(fileData.get(equationColumnName).get(i),equationColumnDataOne.get(i)) ? true : false;
						if(canDoOperation) {
							fileData.get(equationColumnName).set(i,equationColumnDataOne.get(i));							
						}
						else {
							System.out.println("Can do operations on same type. please check the equation and columnData");
							return 0;
						}
					 }
				}
				else if(operandOneType == "i") {
					for(int i = 0 ; i < totalDataRows ; i++) {	
						canDoOperation = validateColumnDataType(fileData.get(equationColumnName).get(i),equationOperandOneInt) ? true : false;	
						if(canDoOperation) {						
							fileData.get(equationColumnName).set(i,equationOperandOneInt);
						}
						else {
							System.out.println("Can do operations on same type. please check the equation and columnData");
							return 0;
						}
					}
				}			
			}
			// returning total number of rows that are calculated
			return totalDataRows;					
		}
		else {
			System.out.println("Please check the equation and number of operands/operators and spaces in between");
			return 0;
		}
		
	}
	
	/**** method ends ****/
	
	
	/**** method starts that prints the 5 rows to the screen ****/
	// check the data having columnNames present or not and print the top 5 rows
	public void top() {
		try {
			if(columnNamesArray.size() == 0) {
				System.out.println("File has no data/ File is Empty to print top rows");
			}
			else {
				try {
					if(fileData.isEmpty()) {
				    	totalDataRows = 0;
						System.out.println("The file is having Column Name.There are improper coloumns/rows of data. It should have column Data");
						columnNamesArray = null;
			    		fileData.clear();
				    }		    
				    else {		
						for(String columnName : columnNamesArray) {
					    	System.out.print(columnName+"\t"); 
					    }
					    System.out.println();
					    
				    	int topRows = totalDataRows > 5 ? 5 : totalDataRows;	
					    for(int i = 0; i < topRows; i++) {
					    	for(String columnName : columnNamesArray) {
						    	System.out.print(fileData.get(columnName).get(i)+"\t");
						    }
					    	System.out.println();
					    }		    	
				    }			
				}
		    	catch(NumberFormatException ne) {
			    	System.out.println("There are improper coloumns/rows of data,Data should contains only alphabets or integers / data shouldn't be null value");
					columnNamesArray = null;
		    		fileData.clear();							    		
		    	}
		    	catch(NullPointerException ne) {
			    	System.out.println("There are improper coloumns/rows of data,Data should contains only alphabets or integers / data shouldn't be null value");
					columnNamesArray = null;
		    		fileData.clear();
		    	}				
			}
		}
		catch(NullPointerException ne) {
			System.out.println("File has no data/ File is Empty to print top rows");
		}
	}
	
	/**** method ends ****/
	
	
	/**** method starts that prints the all rows to the screen ****/
	
	public void print() {
		try {
			if(columnNamesArray.size() == 0) {
				System.out.println("File has no data/ File is Empty to print all rows");
			}
			else {
				try {
					if(fileData.isEmpty()) {
				    	totalDataRows = 0;
						System.out.println("The file is having Column Name.There are improper coloumns/rows of data. It should have column Data");
						columnNamesArray = null;
			    		fileData.clear();
				    }
				    else {
						for(String columnName : columnNamesArray) {
					    	System.out.print(columnName+"\t"); 
					    }
					    System.out.println();
					    
					    for(int i = 0; i < totalDataRows; i++) {
					    	for(String columnName : columnNamesArray) {
						    	System.out.print(fileData.get(columnName).get(i)+"\t");
						    }  
					    	System.out.println();
					    }		    	
				    }				
				}	
		    	catch(NumberFormatException ne) {
			    	System.out.println("There are improper coloumns/rows of data,Data should contains only alphabets or integers / data shouldn't be null value");
					columnNamesArray = null;
		    		fileData.clear();							    		
		    	}
		    	catch(NullPointerException ne) {
			    	System.out.println("There are improper coloumns/rows of data,Data should contains only alphabets or integers / data shouldn't be null value");
					columnNamesArray = null;
		    		fileData.clear();
		    	}
			}
		}
		catch(NullPointerException ne) {
			System.out.println("File has no data/ File is Empty to print all rows");
		}
	}
	
	/**** method ends ****/
	
	
	/**** method starts that writes the contents of the object to the file 
	 * @throws Exception ****/
	
	public Integer write(String filename) {
		try {
			if(columnNamesArray.size() == 0) {
				System.out.println("File has no data/ File is Empty to write or having improper data columns to read and write");
				return 0;
			}			
		}
		catch(NullPointerException ne) {
			System.out.println("File has no data/ File is Empty to write or having improper data columns to read and write");
			return 0;
		}
		if(filename != null) {
			
			File fileName = new File(filename);
			boolean isExist = fileName.exists();
			
			if(isExist) {
				try {
					FileWriter filePath = new FileWriter(filename);
					
					PrintWriter fileWriter = new PrintWriter(filePath);
				    
				    if(fileData.isEmpty()) {
				    	totalDataRows = 0;
						System.out.println("The file is having Column Name.There are improper coloumns/rows of data. It should have column Data");
						columnNamesArray = null;
			    		fileData.clear();
					    fileWriter.close();
					    return 0;
				    }
				    else {						
					    for(String columnName : columnNamesArray) {
					    	fileWriter.print(columnName+"\t"); 
					    }
					    fileWriter.println();
					    
					    for(int i = 0; i < totalDataRows; i++) {
					    	for(String columnName : columnNamesArray) {
						    	fileWriter.print(fileData.get(columnName).get(i)+"\t");
						    }  
					    	fileWriter.println();
					    }		    	
				    }
				    
				    fileWriter.close();
				}
				catch(FileNotFoundException fe) {
					System.out.println("FileName is Empty / File Not Found");
				}  
				catch(NullPointerException ne) {
					System.out.println("File has no data/ File is Empty/ data shouldn't be null value");
				}
			    catch(IndexOutOfBoundsException ie) {
			    	System.out.println("There are improper coloumns/rows of data, data shouldn't be null value");
			    }
				catch(IOException io) {
					System.out.println("FileName Doesn't Exists/FileName is Emptyass");
				}
			}
			else {
				System.out.println("FileName Doesn't Exists/FileName is Empty.please create it");
			}		
		}
		else {
			System.out.println("FileName/FilePath has sent Null");
		}
		
		return totalDataRows;
	}
	
	/**** method ends ****/
	
	public boolean checkLineStatus(List<String> columnNamesArray) {
		int lineLength = 0;
		for(String columnName : columnNamesArray){
			lineLength+=columnName.length()+4;
		}
		return lineLength > 80 ? false : true;		
	}
	
	public boolean validateColumnName(String columnNameAttribute, ArrayList<Object> arrayList, String type) {
		if(type == "old") {
			if(columnNameAttribute.contains(" ") || !columnNameAttribute.matches("^[a-zA-Z]*$")) {
	    		System.out.println("Column Names shouldn't contain space / should contain only alphabets and does not contains integers in between");
	    		columnNamesArray = null;
	    		fileData.clear();
	    		return false;
	    	}
			else if(presentOrNot(columnNameAttribute) == "") {
	    		System.out.println("Column Name already exists/ Column Names must be unique");
	    		columnNamesArray = null;  
	    		fileData.clear();
	    		return false;						    		
	    	}
	    	else {
	            fileData.put(columnNameAttribute,arrayList);		
	            return true;
	    	}
		}
		else if(type == "new") {
			if(columnNameAttribute.contains(" ") || !columnNameAttribute.matches("^[a-zA-Z]*$")) {
	    		System.out.println("Column Names shouldn't contain space / should contain only alphabets and does not contains integers in between");
	    		return false;
	    	}
			else if(presentOrNot(columnNameAttribute) != "") {
	    		System.out.println("Column Name already exists/ Column Names must be unique");
	    		return false;						    		
	    	}
	    	else {
	            fileData.put(columnNameAttribute,arrayList);		
	            return true;
	    	}
		}
		return false;
	}		

	public boolean validateColumnDataType(Object columnData,Object anotherColumnData) {		
		return columnData.getClass() == anotherColumnData.getClass();
	}
	
	public boolean validateColumnDataTypes(Object columnName,Object columnData,Object anotherColumnData) {
		return (columnName.getClass() == columnData.getClass()) && (columnData.getClass() == anotherColumnData.getClass());
	}
	
	public String presentOrNot(String equationColumnName) {
		if(columnNamesArray == null) {
			return "";
		}
		for(String columnName : columnNamesArray) {
		    if(columnName.equalsIgnoreCase(equationColumnName)) {
		    	return columnName;
		    }
		}
		return "";
	}	
}