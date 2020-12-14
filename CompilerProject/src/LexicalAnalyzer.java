import java.io.*;
import java.util.*;


public class LexicalAnalyzer {
	
	public static String [] Integers = {"0","1","2","3","4","5","6","7","8","9"};
	public static ArrayList<String> SetIntegers = new ArrayList<String>();
	public static String [] Letters = {"a","A","b","B","c","C","d","D","e","E","f","F","g","G","h","H","i","I","j","J","k","K","l","L","m","M","n","N","o","O","p","P","q","Q","r","R","s","S","t","T","u","U","v","V","w","W","x","X","y","Y","z","Z","_"};
	public static ArrayList<String> SetLetters = new ArrayList<String>();
	public static String [] catchWords = {"and","or","mod","div"};
	public static ArrayList<String> potCatch = new ArrayList<String>();
	
	public static ArrayList<SMBLTable> Symbols = new ArrayList<SMBLTable>();
	public static ArrayList<Index> Reference = new ArrayList<Index>();
	public static ArrayList<ReservedWordFile> RWF = new ArrayList<ReservedWordFile>();
	public static ArrayList<String> TokenFile = new ArrayList<String>();
	
	public static ArrayList<Token> results = new ArrayList<Token>();
	public static ArrayList<Burn> UltraStorage = new ArrayList<Burn>();
	
	public static int phase = 1;
	public static ArrayList<String> theLine = new ArrayList<String>();
	public static ArrayList<String> LEX = new ArrayList<String>();
	public static ArrayList<SuperLEX> UltraLEX = new ArrayList<SuperLEX>();
	
	public static String buffer [] = new String [72];
	public static int b = 0;
	public static int IDpoint = 40;
	
	public static class Token{
		public final String a; public final String b; public final String c;
		
		public Token(String a, String b, String c)
		{
			this.a = a;
			this.b = b;
			this.c = c;	
		}		
	}
	
	public static class Burn{
		public final ArrayList<Token> a;
		public Burn(ArrayList<Token> a)
		{
			ArrayList<Token> b = new ArrayList<Token>();
			for(Token i : a) {b.add(new Token(i.a,i.b,i.c));}
			this.a = b;
		}
	}
	
	public static class SuperLEX{
		public final ArrayList<String> a;
		public SuperLEX(ArrayList<String> a)
		{
			ArrayList<String> b = new ArrayList<String>();
			for(String i : a) {b.add(new String(i));}
			this.a = b;
		}
	}

	public static class SMBLTable{
		public final String a; public final loc b;
		public SMBLTable(String a, loc b)
		{
			this.a = a;
			this.b = b;
		}
	}
	
	public static class loc{

	}
	
	public static class ReservedWordFile{
		public final String lexeme; public final String a; public final int an; public final String b; public final int bn;
		public ReservedWordFile(String lexeme, String a, int an, String b, int bn)
		{
			this.lexeme = lexeme;
			this.a = a;
			this.an = an;
			this.b = b;
			this.bn = bn;
		}
	}
	
	public static class Index{
		public final String name; public final int number;
		public Index(String name, int number)
		{
			this.name = name;
			this.number = number;
		}
	}
	
	public static boolean locate(String me)
	{
		for(SMBLTable i : Symbols)
		{
			if(i.a.equals(me))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean find(String me)
	{
		for(Index i : Reference)
		{
			if(i.name.equals(me))
			{
				return true;
			}
		}
		return false;
	}
	
	public static String address(String me)
	{
		for(SMBLTable i : Symbols)
		{
			if(i.a.equals(me))
			{
				return i.b.toString();
			}
		}
		return "no address";
	}
	
	public static String valueOf(String me)
	{
		for(Index i : Reference)
		{
			if(i.name.equals(me))
			{
				return String.valueOf(i.number);
			}
		}
		return String.valueOf(0);
	}
	
	public static void initRef()
	{
		//Token-Type
		Reference.add(new Index("relop", 1));
		Reference.add(new Index("NUM", 2));
		Reference.add(new Index("LEXERR", 3));
		Reference.add(new Index("EOF", 4));
		Reference.add(new Index("AddOP", 5));
		Reference.add(new Index("MulOP", 6));
		Reference.add(new Index("ID", 7));
		Reference.add(new Index("Period", 9));
		Reference.add(new Index("AssignOP", 33));
		Reference.add(new Index("Comma", 37));
		Reference.add(new Index("Semicolon", 38));
		Reference.add(new Index("Colon", 39));
		Reference.add(new Index("LParen", 40));
		Reference.add(new Index("RParen", 41));
		Reference.add(new Index("LBrack", 42));
		Reference.add(new Index("RBrack", 43));
		Reference.add(new Index("DoublePeriod", 44));
		
		//Attribute
		Reference.add(new Index("LE", 10));
		Reference.add(new Index("NE", 11));
		Reference.add(new Index("LT", 12));
		Reference.add(new Index("EQ", 13));
		Reference.add(new Index("GE", 14));
		Reference.add(new Index("GT", 15));
		Reference.add(new Index("INT", 16));
		Reference.add(new Index("ExtraLongInteger", 17));
		Reference.add(new Index("ExtraLongFloat", 18));
		Reference.add(new Index("ExtraLongExponent", 19));
		Reference.add(new Index("LeadingZero", 20));
		Reference.add(new Index("TrailingZero", 21));
		Reference.add(new Index("ExtraLongID", 22));
		Reference.add(new Index("NULL", 23));
		Reference.add(new Index("PLUS", 24));
		Reference.add(new Index("MINUS", 25));
		Reference.add(new Index("OR", 26));
		Reference.add(new Index("AND", 27));
		Reference.add(new Index("Divide", 28));
		Reference.add(new Index("Mult", 29));
		Reference.add(new Index("REAL", 30));
		Reference.add(new Index("LONGREAL", 31));
		Reference.add(new Index("Unrecognized", 32));
		Reference.add(new Index("Assign", 34));
		Reference.add(new Index("DIV", 35));
		Reference.add(new Index("MOD", 36));
	}
	
	
	public static void main(String args[]) throws IOException
	{
		initRef();
		
		for(int i = 0; i < Integers.length; i++)
		{
			SetIntegers.add(Integers[i]);
		}
		for(int i = 0; i < Letters.length; i++)
		{
			SetLetters.add(Letters[i]);
		}
		for(int i = 0; i < catchWords.length; i++)
		{
			potCatch.add(catchWords[i]);
		}
		
		String lines;
		int lineCounter = 1;
		
		File myFile = new File("keywordFile");
		File myOtherFile = new File("testme");
		
		Scanner input = new Scanner(myFile);	
		
		while(input.hasNextLine())
		{
			String l = input.next();
			RWF.add(new ReservedWordFile(l.substring(1, l.length()-1),input.next(),input.nextInt(),input.next(),input.nextInt()));
		}

		input = new Scanner(myOtherFile);
		
		while(input.hasNextLine())
		{
			lines = input.nextLine().toString();
			theLine.add(lineCounter + "   " + lines);
			//System.out.println(lineCounter + "   " + lines);
			for(int i = 0; i < buffer.length; i++)
			{
				if(i < lines.length())
				{buffer[i] = lines.substring(i, i+1);}
				else
				{buffer[i] = " ";}
			}
			

			b=0;
			while(b < buffer.length)
			{
				//System.out.println(b + " !" + buffer[b]+ "!");
				//Debugger Above
				IDres();
			}
			
			for(Token i : results)
			{
				if(i.a.equals("LEXERR"))
				{
					LEX.add(i.a + ":  " + i.b + ":  " + i.c);
					//System.out.println(i.a + ":  " + i.b + ":  " + i.c);
				}
			}
			
			
			for(Token i : results)
			{
				if(locate(i.c))
				{
					TokenFile.add(lineCounter + " " + i.c + " " + i.a + "(7) "+ i.b + "(ptr)");
				}
				else
				{
					TokenFile.add(lineCounter + " " + i.c + " " + i.a + "(" + valueOf(i.a) + ") "+ i.b + "(" + valueOf(i.b) + ")");
				}
			}
			
			//if(results.size()>0) {SyntaxAnalyzer.Start(results);}
			UltraStorage.add(new Burn(results));
			results.clear();
			
			UltraLEX.add(new SuperLEX(LEX));
			LEX.clear();
			lineCounter++;
		}
		System.out.println(theLine.get(0));
		for(String z : UltraLEX.get(0).a)
		{
			System.out.println(z);
		}
		
		SyntaxAnalyzer.Start(UltraStorage);
		
		TokenFile.add("  " + "eof" + " " + "EOF" + "(" + "4" + ") "+ "NULL" + "(" + "23" + ")");
		System.out.println("Token File: (line/lexeme/tokentype/attribute)");
		for(String i : TokenFile)
		{
			System.out.println(i);
		}
				
	}
	
	public static void announce()
	{
		System.out.println(theLine.get(phase));
		for(String z : UltraLEX.get(phase).a)
		{
			System.out.println(z);
		}
		phase++;
	}
	
	public static void relop()
	{
		int i=b;
			if(buffer[i].equals("<"))
			{
				i++;
				if(i < buffer.length)
				{
					if(buffer[i].equals("="))
					{
						results.add(new Token("relop","LE",buffer[i-1]+buffer[i]));
						i++;
						b=i;
						return;
					}
					else if(buffer[i].equals(">"))
					{
						results.add(new Token("relop","NE",buffer[i-1]+buffer[i]));
						i++;
						b=i;
						return;
					}
					else
					{
						results.add(new Token("relop","LT",buffer[i-1]));
						b=i;
						return;
					}
				}
				else
				{
					results.add(new Token("relop","LT",buffer[i-1]));
					b=i;
					return;
				}	
			}
			
			else if(buffer[i].equals("="))
			{
				results.add(new Token("relop","EQ",buffer[i]));
				i++;
				b=i;
				return;
			}
			
			else if(buffer[i].equals(">"))
			{
				i++;
				if(i < buffer.length)
				{
					if(buffer[i].equals("="))
					{
						results.add(new Token("relop","GE",buffer[i-1]+buffer[i]));
						i++;
						b=i;
						return;
					}
					else
					{
						results.add(new Token("relop","GE",buffer[i-1]));
						b=i;
						return;
					}
				}
				else
				{
					results.add(new Token("relop","GE",buffer[i-1]));
					b=i;
					return;
				}
			}
			else
			{
				results.add(new Token("LEXERR","Unrecognized",buffer[i]));
				i++;
				b=i;
				return;
			}
		
	}
	
	public static void whitespace()
	{
		int i=b;
			if((i+1)<buffer.length)
			{
				/*
				if((buffer[i]+buffer[i+1]).equals("\\n"))
				{
					b=buffer.length;
					return;
				}
				*/
				if((buffer[i]+buffer[i+1]).equals("\\t"))
				{
					i=i+2;
					while(i<buffer.length)
					{
						if((i+1)<buffer.length)
						{
							if((buffer[i]+buffer[i+1]).equals("\\t"))
							{
								i=i+2;
							}
							else if(buffer[i].equals(" ")||buffer[i].equals("	"))
							{
								i++;
							}
							else
							{
								break;
							}
						}
						else if(buffer[i].equals(" ")||buffer[i].equals("	"))
						{
							i++;
						}
						else
						{
							b=i;
							return;
						}
					}
					b=i;
					return;
				}
				else if(buffer[i].equals(" ")||buffer[i].equals("	"))
				{
					i++;
					while(i<buffer.length)
					{
						if((i+1)<buffer.length)
						{
							if((buffer[i]+buffer[i+1]).equals("\\t"))
							{
								i=i+2;
							}
							else if(buffer[i].equals(" ")||buffer[i].equals("	"))
							{
								i++;
							}
							else
							{
								break;
							}
						}
						else if(buffer[i].equals(" ")||buffer[i].equals("	"))
						{
							i++;
						}
						else
						{
							break;
						}
					}
					b=i;
					return;
				}
				else
				{
					catchAll();
				}
			}
			else if(buffer[i].equals(" ")||buffer[i].equals("	"))
			{
				i++;
				b=i;
				return;
			}
			else
			{
				catchAll();
			}
	}
	
	public static void intMachine()
	{
		String number = "";
		int i = b;
		boolean LeadError = false;
		boolean overInt = false;
			if(SetIntegers.contains(buffer[i]))
			{
				number += buffer[i];
				i++;
				
				while(i < buffer.length)
				{
					if(SetIntegers.contains(buffer[i]))
					{
						number += buffer[i];
						i++;
					}
					else
					{
						break;
					}
				}
				
				if(number.substring(0, 1).equals("0") && number.length() > 1)
				{
					LeadError = true;
				}
				
				if(number.length() >= 10)
				{			
					overInt = true;
				}
				
				if(!(LeadError||overInt))
				{
					results.add(new Token("NUM", "INT", number));
					b=i;
					return;
				}
				else
				{
					b=i;
					if(LeadError)
					{
						results.add(new Token("LEXERR", "LeadingZero", number));
					}
					if(overInt)
					{
						results.add(new Token("LEXERR", "ExtraLongInteger", number));
					}
					return;
				}
				
			}
			else
			{
				relop();
			}
		
	}
	
	public static void IDres()
	{
		String ID = "";
		int i=b;
		
			if(SetLetters.contains(buffer[i]))
			{
				ID += buffer[i];
				i++;
				
				while(i<buffer.length)
				{
					if((SetLetters.contains(buffer[i])||SetIntegers.contains(buffer[i])))
					{
						ID += buffer[i];
						i++;
					}
					else
					{
						break;
					}
					
				}
				
				if(ID.length() <= 10)
				{
					if(potCatch.contains(ID))//avoid using catchAll as ID
					{
						catchAll();
						return;
					}
					
					if(!keyword(ID))
					{
						if(locate(ID))
						{
							results.add(new Token(ID,address(ID),ID));
						}
						else
						{
							Symbols.add(new SMBLTable(ID, new loc()));
							results.add(new Token(ID,address(ID),ID));

							IDpoint++;
						}

						
						b=i;
						return;
					}
					else
					{
						b=i;
						return;
					}
				}
				else
				{
					results.add(new Token("LEXERR","ExtraLongID",ID));
					b=i;
					return;
				}
			}
			else
			{
				whitespace();
			}
		}
	public static boolean keyword(String ID)
	{
		for(ReservedWordFile i : RWF)
		{
			if(ID.equals(i.lexeme))
			{
				if(!find(i.a))
				{
					Reference.add(new Index(i.a, i.an));
				}
				if(!find(i.b))
				{
					Reference.add(new Index(i.b, i.bn));
				}
				results.add(new Token(i.a,i.b,ID));
				return true;
				
			}
		}
		return false;
	}
	
	public static void real()
	{
		int i = b;
		String number = "";
		int numCounter = 0;
		boolean LeadError = false;
		boolean TrailError = false;
		boolean overInt = false;
		boolean overDec = false;
		
		if(SetIntegers.contains(buffer[i]))
		{
			number += buffer[i];
			i++;
			
			while(i < buffer.length)
			{
				if(SetIntegers.contains(buffer[i]))
				{
					number += buffer[i];
					i++;
				}
				else
				{
					break;
				}
			}
			
			if(number.substring(0, 1).equals("0") && number.length() > 1)
			{
				LeadError = true;
			}
			
			if(number.length() >= 10)
			{			
				overInt = true;
			}
			
		}
		else
		{
			intMachine();
			return;
		}
		
		if((i+1)<buffer.length)
		{
			if(buffer[i].equals(".")&&SetIntegers.contains(buffer[i+1]))
			{
				number += (buffer[i] + buffer[i+1]);
				i = i+2;
				numCounter++;
				
				while(i < buffer.length)
				{
					if(SetIntegers.contains(buffer[i]))
					{
						number += buffer[i];
						i++;
						numCounter++;
					}
					else
					{
						break;
					}
				}
				
				if(numCounter > 5) {overDec = true;}
				if(number.substring(number.length()-1, number.length()).equals("0")) {TrailError = true;}
			}
			else
			{
				intMachine();
				return;
			}
		}
		else
		{
			intMachine();
			return;
		}
		
		if(!(LeadError||overInt||TrailError||overDec))
		{
			results.add(new Token("NUM", "REAL", number));
			b=i;
			return;
		}
		else
		{
			b=i;
			if(overDec||overInt)
			{
				results.add(new Token("LEXERR", "ExtraLongFloat", number));
			}
			if(LeadError)
			{
				results.add(new Token("LEXERR", "LeadingZero", number));
			}
			if(TrailError)
			{
				results.add(new Token("LEXERR", "TrailingZero", number));
			}
			return;
		}
	}
	
	public static void longreal()
	{
		int i = b;
		String number = "";
		int numCounter = 0;
		boolean LeadError = false;
		boolean TrailError = false;
		boolean overInt = false;
		boolean overDec = false;
		boolean overExp = false;
		
		if(SetIntegers.contains(buffer[i]))
		{
			number += buffer[i];
			i++;
			
			while(i < buffer.length)
			{
				if(SetIntegers.contains(buffer[i]))
				{
					number += buffer[i];
					i++;
				}
				else
				{
					break;
				}
			}
			
			if(number.substring(0, 1).equals("0") && number.length() > 1)
			{
				LeadError = true;
			}
			
			if(number.length() >= 10)
			{			
				overInt = true;
			}
			
		}
		else
		{
			real();
			return;
		}
		
		if((i+1)<buffer.length)
		{
			if(buffer[i].equals(".")&&SetIntegers.contains(buffer[i+1]))
			{
				number += (buffer[i] + buffer[i+1]);
				i = i+2;
				numCounter++;
				
				while(i < buffer.length)
				{
					if(SetIntegers.contains(buffer[i]))
					{
						number += buffer[i];
						i++;
						numCounter++;
					}
					else
					{
						break;
					}
				}
				
				if(numCounter > 5) {overDec = true;}
				if(number.substring(number.length()-1, number.length()).equals("0")) {TrailError = true;}
			}
			else
			{
				real();
				return;
			}
		}
		else
		{
			real();
			return;
		}
		
		if(i+1<buffer.length)
		{
			numCounter = 0;
			if(buffer[i].equals("E"))
			{
				number += buffer[i];
				i++;
				
				if(SetIntegers.contains(buffer[i]))
				{
					if(buffer[i].equals("0")) {LeadError = true;}
					number += buffer[i];
					i++;
					numCounter++;
					
					while(i < buffer.length)
					{
						if(SetIntegers.contains(buffer[i]))
						{
							number += buffer[i];
							i++;
							numCounter++;
						}
						else
						{
							break;
						}
					}
				}
				else if(buffer[i].contentEquals("+")||buffer[i].contentEquals("-"))
				{
					number += buffer[i];
					i++;
					
					if(i<buffer.length)
					{
						if(SetIntegers.contains(buffer[i]))
						{
							if(buffer[i].equals("0")) {LeadError = true;}
							number += buffer[i];
							i++;
							numCounter++;
							
							while(i < buffer.length)
							{
								if(SetIntegers.contains(buffer[i]))
								{
									number += buffer[i];
									i++;
									numCounter++;
								}
								else
								{
									break;
								}
							}
						}
						else
						{
							real();
							return;
						}
					}
					else
					{
						real();
						return;
					}
				}
				else
				{
					real();
					return;
				}
			}
			else
			{
				real();
				return;
			}
			
			if(numCounter > 2) {overExp = true;}
		}
		else
		{
			real();
			return;
		}
		
		if(!(LeadError||overInt||TrailError||overDec||overExp))
		{
			results.add(new Token("NUM", "LONGREAL", number));
			b=i;
			return;
		}
		else
		{
			b=i;
			if(overDec||overInt)
			{
				results.add(new Token("LEXERR", "ExtraLongFloat", number));
			}
			if(overExp)
			{
				results.add(new Token("LEXERR", "ExtraLongExponent", number));
			}
			if(LeadError)
			{
				results.add(new Token("LEXERR", "LeadingZero", number));
			}
			if(TrailError)
			{
				results.add(new Token("LEXERR", "TrailingZero", number));
			}
			return;
		}
		
		
	}
	
	
	public static void catchAll()
	{
		int i = b;
			if((i+2)<buffer.length)
			{
				String threeStep = buffer[i]+buffer[i+1]+buffer[i+2];
				
				if(threeStep.equals("div"))
				{
					results.add(new Token("MULOP","DIV",threeStep));
					i = i+3;
					b=i;
					return;
				}
				else if(threeStep.equals("and"))
				{
					results.add(new Token("MULOP","AND",threeStep));
					i = i+3;
					b=i;
					return;
				}
				else if(threeStep.equals("mod"))
				{
					results.add(new Token("MULOP","MOD",threeStep));
					i = i+3;
					b=i;
					return;
				}
			}
			if((i+1)<buffer.length)
			{
				String twoStep = buffer[i]+buffer[i+1];

				if(twoStep.equals("or"))
				{
					results.add(new Token("AddOP","OR",twoStep));
					i=i+2;
					b=i;
					return;
				}
				else if(twoStep.contentEquals(":="))
				{
					results.add(new Token("AssignOP","Assign",twoStep));
					i=i+2;
					b=i;
					return;
				}
				else if(twoStep.contentEquals(".."))
				{
					results.add(new Token("DoublePeriod","NULL",twoStep));
					i=i+2;
					b=i;
					return;
				}
			}
			if(buffer[i].equals("+"))
			{

						results.add(new Token("AddOP","PLUS",buffer[i]));
						i++;
						b=i;
						return;				
			}
			else if(buffer[i].equals("-"))
			{
						results.add(new Token("AddOP","MINUS",buffer[i]));
						i++;
						b=i;
						return;
			}
			else if(buffer[i].equals("*"))
			{
				results.add(new Token("MulOP","Mult",buffer[i]));
				i++;
				b=i;
				return;
			}
			else if(buffer[i].equals("/"))
			{
				results.add(new Token("MulOP","Divide",buffer[i]));
				i++;
				b=i;
				return;
			}
			else if(buffer[i].equals("."))
			{
						results.add(new Token("Period","NULL",buffer[i]));
						i++;
						b=i;
						return;
			}
			else if(buffer[i].equals(","))
			{
						results.add(new Token("Comma","NULL",buffer[i]));
						i++;
						b=i;
						return;
			}
			else if(buffer[i].equals(";"))
			{
						results.add(new Token("Semicolon","NULL",buffer[i]));
						i++;
						b=i;
						return;
			}
			else if(buffer[i].equals(":"))
			{
						results.add(new Token("Colon","NULL",buffer[i]));
						i++;
						b=i;
						return;
			}
			else if(buffer[i].equals("("))
			{
						results.add(new Token("LParen","NULL",buffer[i]));
						i++;
						b=i;
						return;
			}
			else if(buffer[i].equals(")"))
			{
						results.add(new Token("RParen","NULL",buffer[i]));
						i++;
						b=i;
						return;
			}else if(buffer[i].equals("["))
			{
				results.add(new Token("LBrack","NULL",buffer[i]));
				i++;
				b=i;
				return;
			}else if(buffer[i].equals("]"))
			{
				results.add(new Token("RBrack","NULL",buffer[i]));
				i++;
				b=i;
				return;
			}
			else
			{
				longreal();
			}	
		}
}
