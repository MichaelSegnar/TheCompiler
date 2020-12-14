import java.io.*;
import java.util.*;

public class SyntaxAnalyzer {

	public static int i;
	public static int j;
	public static LexicalAnalyzer.Token tok;
	public static SMBL2 test;
	public static int size;
	public static int numOfLists;
	public static ArrayList<LexicalAnalyzer.Token> list;
	public static ArrayList<LexicalAnalyzer.Burn> S_List;
	public static ArrayList<SMBL2> IDmarker = new ArrayList<SMBL2>();
	
	public static class SMBL2{
		public final String a; public final String b; public final String c; public final String d;
		public SMBL2(String a, String b, String c, String d)
		{
			this.a = a;
			this.b = b; 
			this.c = c;
			this.d = d;
		}
	}
	
	public static boolean unused(LexicalAnalyzer.Token token)
	{
		for(SMBL2 i : IDmarker)
		{
			if(i.c.equals(token.c))
			{
				return false;
			}
		}
		return true;
	}
	
	public static String IDType(LexicalAnalyzer.Token token)
	{
		for(SMBL2 i : IDmarker)
		{
			if(i.c.equals(token.c))
			{
				return i.d;
			}
		}
		return "No Type";
	}
	
	
	public static void Start(ArrayList<LexicalAnalyzer.Burn> check)
	{
		S_List = check;
		numOfLists = check.size();
		i = 0; j =0;

		list = S_List.get(j).a;
		size = list.size();
		while(size < 1)
		{
			nextTok();
		}
		tok = list.get(i);
		
		P();
	}
	
	public static void nextTok()
	{
		i++;
		if(i < size) {tok = list.get(i);}
		else {
			i=0; j++;
			if(j < numOfLists)
			{
				LexicalAnalyzer.announce();
				list = S_List.get(j).a;
				size = list.size();
				if(size < 1)
				{
					nextTok();
				}
				else
				{
					tok = list.get(i);
				}
			}
			else
			{
				tok = new LexicalAnalyzer.Token("E", "E", "E");
			}
		}
	}
	
	public static void P()
	{
		if(tok.a.equals("program"))
		{
			nextTok();
			if(LexicalAnalyzer.locate(tok.a))
			{
				IDmarker.add(new SMBL2(tok.a, tok.b, tok.c, "Program"));
				nextTok();
				if(tok.a.equals("LParen"))
				{
					nextTok();
					IL();
					if(tok.a.equals("RParen"))
					{
						nextTok();
						if(tok.a.equals("Semicolon"))
						{
							nextTok();
							P1();
						}
						else
						{
							System.out.println("SYNERR: Expecting Semicolon. Received " + tok.a + ".");
							while(!(tok.a.equals("E")))
							{
								nextTok();
							}
						}
					}
					else
					{
						System.out.println("SYNERR: Expecting RParen. Received " + tok.a + ".");
						while(!(tok.a.equals("E")))
						{
							nextTok();
						}
					}
				}
				else
				{
					System.out.println("SYNERR: Expecting LParen. Received " + tok.a + ".");
					while(!(tok.a.equals("E")))
					{
						nextTok();
					}
				}
			}
			else
			{
				System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
				while(!(tok.a.equals("E")))
				{
					nextTok();
				}
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting program. Received " + tok.a + ".");
			while(!(tok.a.equals("E")))
			{
				nextTok();
			}
		}
	}
	
	public static void P1()
	{
		if(tok.a.equals("var"))
		{
			D();
			P2();
		}
		else if(tok.a.equals("procedure"))
		{
			SDS();
			CS();
			if(tok.a.equals("Period"))
			{
				nextTok();
				for(SMBL2 i : IDmarker) {System.out.println(i.c + " is a " + i.d + " variable.");}
			}
			else
			{
				System.out.println("SYNERR: Expecting Period. Received " + tok.a + ".");
				while(!(tok.a.equals("E")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("begin"))
		{
			CS();
			if(tok.a.equals("Period"))
			{
				nextTok();
				for(SMBL2 i : IDmarker) {System.out.println(i.c + " is a " + i.d + " variable.");}
			}
			else
			{
				System.out.println("SYNERR: Expecting Period. Received " + tok.a + ".");
				while(!(tok.a.equals("E")))
				{
					nextTok();
				}
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting one of var, begin, procedure. Received " + tok.a + ".");
			while(!(tok.a.equals("E")))
			{
				nextTok();
			}
		}
	}
	
	public static void P2()
	{
		if(tok.a.equals("procedure"))
		{
			SDS();
			CS();
			if(tok.a.equals("Period"))
			{
				nextTok();
				for(SMBL2 i : IDmarker) {System.out.println(i.c + " is a " + i.d + " variable.");}
			}
			else
			{
				System.out.println("SYNERR: Expecting Period. Received " + tok.a + ".");
				while(!(tok.a.equals("E")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("begin"))
		{
			CS();
			if(tok.a.equals("Period"))
			{
				nextTok();
			}
			else
			{
				System.out.println("SYNERR: Expecting Period. Received " + tok.a + ".");
				while(!(tok.a.equals("E")))
				{
					nextTok();
				}
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting one of procedure, begin. Received " + tok.a + ".");
			while(!(tok.a.equals("E")))
			{
				nextTok();
			}
		}
	}
	
	public static void IL()
	{
		if(LexicalAnalyzer.locate(tok.a))
		{
			if(unused(tok))
			{
				IDmarker.add(new SMBL2(tok.a, tok.b, tok.c, "Parameter"));
			}
			else
			{
				System.out.println("SEMERR: Expecting Parameter ID. Received " + IDType(tok) + " ID.");
			}
			nextTok();
			IL1();
		}
		else
		{
			System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("RParen")))
			{
				nextTok();
			}
		}
	}
	
	public static void IL1()
	{
		if(tok.a.equals("Comma"))
		{
			nextTok();
			if(LexicalAnalyzer.locate(tok.a))
			{
				if(unused(tok))
				{
					IDmarker.add(new SMBL2(tok.a, tok.b, tok.c, "Parameter"));
				}
				else
				{
					System.out.println("SEMERR: Expecting Parameter ID. Received " + IDType(tok) + " ID.");
				}
				nextTok();
				IL1();
			}
			else
			{
				System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("RParen")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("E")||tok.a.equals("RParen"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of Comma, RParen. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("RParen")))
			{
				nextTok();
			}
		}
	}
	
	public static void D()
	{
		if(tok.a.equals("var"))
		{
			nextTok();
			if(LexicalAnalyzer.locate(tok.a))
			{
				String sub1 = tok.a;
				String sub2 = tok.b;
				String sub3 = tok.c;
				boolean New = true;
				if(!unused(tok))
				{
					New = false;
					System.out.println("SEMERR: Expecting Integer or Real ID. Received " + IDType(tok) + " ID.");
				}
				nextTok();
				if(tok.a.equals("Colon"))
				{
					nextTok();
					if(New)
					{
						IDmarker.add(new SMBL2(sub1,sub2,sub3,T()));
					}
					else
					{
						T();
					}
					if(tok.a.equals("Semicolon"))
					{
						nextTok();
						D1();
					}
					else
					{
						System.out.println("SYNERR: Expecting Semicolon. Received " + tok.a + ".");
						while(!(tok.a.equals("E")||tok.a.equals("procedure")||tok.a.equals("begin")))
						{
							nextTok();
						}
					}
				}
				else
				{
					System.out.println("SYNERR: Expecting Colon. Received " + tok.a + ".");
					while(!(tok.a.equals("E")||tok.a.equals("procedure")||tok.a.equals("begin")))
					{
						nextTok();
					}
				}
			}
			else
			{
				System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("procedure")||tok.a.equals("begin")))
				{
					nextTok();
				}
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting var. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("procedure")||tok.a.equals("begin")))
			{
				nextTok();
			}
		}
	}
	
	public static void D1()
	{
		if(tok.a.equals("var"))
		{
			nextTok();
			if(LexicalAnalyzer.locate(tok.a))
			{
				String sub1 = tok.a;
				String sub2 = tok.b;
				String sub3 = tok.c;
				boolean New = true;
				if(!unused(tok))
				{
					New = false;
					System.out.println("SEMERR: Expecting Integer or Real ID. Received " + IDType(tok) + " ID.");
				}
				nextTok();
				if(tok.a.equals("Colon"))
				{
					nextTok();
					if(New)
					{
						IDmarker.add(new SMBL2(sub1,sub2,sub3,T()));
					}
					else
					{
						T();
					}
					if(tok.a.equals("Semicolon"))
					{
						nextTok();
						D1();
					}
					else
					{
						System.out.println("SYNERR: Expecting Semicolon. Received " + tok.a + ".");
						while(!(tok.a.equals("E")||tok.a.equals("procedure")||tok.a.equals("begin")))
						{
							nextTok();
						}
					}
				}
				else
				{
					System.out.println("SYNERR: Expecting Colon. Received " + tok.a + ".");
					while(!(tok.a.equals("E")||tok.a.equals("procedure")||tok.a.equals("begin")))
					{
						nextTok();
					}
				}
			}
			else
			{
				System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("procedure")||tok.a.equals("begin")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("E")||tok.a.equals("procedure")||tok.a.equals("begin"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of var, procedure, begin. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("procedure")||tok.a.equals("begin")))
			{
				nextTok();
			}
		}
	}
	
	public static String T()
	{
		if(tok.a.equals("NUM"))
		{
			return ST();
		}
		else if(tok.a.equals("array"))
		{
			nextTok();
			if(tok.a.equals("LBrack"))
			{
				nextTok();
				if(tok.a.equals("NUM"))
				{
					nextTok();
					if(tok.a.equals("DoublePeriod"))
					{
						nextTok();
						if(tok.a.equals("NUM"))
						{
							nextTok();
							if(tok.a.equals("RBrack"))
							{
								nextTok();
								if(tok.a.equals("of"))
								{
									nextTok();
									return ST() + " array";
								}
								else
								{
									System.out.println("SYNERR: Expecting of. Received " + tok.a + ".");
									while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("RParen")))
									{
										nextTok();
									}
									return "ERROR";
								}
							}
							else
							{
								System.out.println("SYNERR: Expecting RBrack. Received " + tok.a + ".");
								while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("RParen")))
								{
									nextTok();
								}
								return "ERROR";
							}
						}
						else
						{
							System.out.println("SYNERR: Expecting NUM. Received " + tok.a + ".");
							while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("RParen")))
							{
								nextTok();
							}
							return "ERROR";
						}
					}
					else
					{
						System.out.println("SYNERR: Expecting DoublePeriod. Received " + tok.a + ".");
						while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("RParen")))
						{
							nextTok();
						}
						return "ERROR";
					}
				}
				else
				{
					System.out.println("SYNERR: Expecting NUM. Received " + tok.a + ".");
					while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("RParen")))
					{
						nextTok();
					}
					return "ERROR";
				}
			}
			else
			{
				System.out.println("SYNERR: Expecting LBrack. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("RParen")))
				{
					nextTok();
				}
				return "ERROR";
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting one of NUM, array. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("RParen")))
			{
				nextTok();
			}
			return "ERROR";
		}
	}
	
	public static String ST()
	{
		if(tok.b.equals("INT")||tok.b.equals("REAL"))
		{
			String sub = tok.b;
			nextTok();
			return sub;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of INT, REAL. Received " + tok.b + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("RParen")))
			{
				nextTok();
			}
			return "ERROR";
		}
	}
	
	public static void SDS()
	{
		SD();
		if(tok.a.equals("Semicolon"))
		{
			nextTok();
			SDS1();
		}
		else
		{
			System.out.println("SYNERR: Expecting Semicolon. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("begin")))
			{
				nextTok();
			}
		}
	}
	
	public static void SDS1()
	{
		if(tok.a.equals("procedure"))
		{
			SD();
			if(tok.a.equals("Semicolon"))
			{
				nextTok();
				SDS1();
			}
			else
			{
				System.out.println("SYNERR: Expecting Semicolon. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("begin")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("E")||tok.a.equals("begin"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of procedure, begin. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("begin")))
			{
				nextTok();
			}
		}
	}
	
	public static void SD()
	{
		if(tok.a.equals("procedure"))
		{
			SH();
			SD1();
		}
		else
		{
			System.out.println("SYNERR: Expecting procedure. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")))
			{
				nextTok();
			}
		}
	}
	
	public static void SD1()
	{
		if(tok.a.equals("var"))
		{
			D(); SD2();
		}
		else if(tok.a.equals("procedure"))
		{
			SDS(); CS();
		}
		else if(tok.a.equals("begin"))
		{
			CS();
		}
		else
		{
			System.out.println("SYNERR: Expecting one of var, procedure, begin. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")))
			{
				nextTok();
			}
		}
	}
	
	public static void SD2()
	{
		if(tok.a.equals("procedure"))
		{
			SDS(); CS();
		}
		else if(tok.a.equals("begin"))
		{
			CS();
		}
		else
		{
			System.out.println("SYNERR: Expecting one of procedure, begin. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")))
			{
				nextTok();
			}
		}
	}
	
	public static void SH()
	{
		if(tok.a.equals("procedure"))
		{
			nextTok();
			if(LexicalAnalyzer.locate(tok.a))
			{
				if(unused(tok))
				{
					IDmarker.add(new SMBL2(tok.a, tok.b, tok.c, "Procedure"));
				}
				else
				{
					System.out.println("SEMERR: Expecting Procedure ID. Received " + IDType(tok) + " ID.");
				}
				nextTok();
				SH1();
			}
			else
			{
				System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("var")||tok.a.equals("procedure")||tok.a.equals("begin")))
				{
					nextTok();
				}
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting procedure. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("var")||tok.a.equals("procedure")||tok.a.equals("begin")))
			{
				nextTok();
			}
		}
	}
	
	public static void SH1()
	{
		if(tok.a.equals("LParen"))
		{
			A();
		}
		if(tok.a.equals("Semicolon"))
		{
			nextTok();
		}
		else
		{
			System.out.println("SYNERR: Expecting one of LParen, Semicolon. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("var")||tok.a.equals("procedure")||tok.a.equals("begin")))
			{
				nextTok();
			}
		}
	}
	
	public static void A()
	{
		if(tok.a.equals("LParen"))
		{
			nextTok();
			PL();
			if(tok.a.equals("RParen"))
			{
				nextTok();
			}
			else
			{
				System.out.println("SYNERR: Expecting RParen. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("Semicolon")))
				{
					nextTok();
				}
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting LParen. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("var")||tok.a.equals("procedure")||tok.a.equals("begin")))
			{
				nextTok();
			}
		}
	}
	
	public static void PL()
	{
		if(LexicalAnalyzer.locate(tok.a))
		{
			String sub1 = tok.a;
			String sub2 = tok.b;
			String sub3 = tok.c;
			boolean New = true;
			if(!unused(tok))
			{
				New = false;
				System.out.println("SEMERR: Expecting Integer or Real ID. Received " + IDType(tok) + " ID.");
			}
			nextTok();
			if(tok.a.equals("Colon"))
			{
				nextTok();
				if(New)
				{
					IDmarker.add(new SMBL2(sub1,sub2,sub3,"Parameter " + T()));
				}
				else
				{
					T();
				}
				PL1();
			}
			else
			{
				System.out.println("SYNERR: Expecting Colon. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("RParen")))
				{
					nextTok();
				}
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("RParen")))
			{
				nextTok();
			}
		}
	}
	
	public static void PL1()
	{
		if(tok.a.equals("Semicolon"))
		{
			nextTok();
			if(LexicalAnalyzer.locate(tok.a))
			{
				String sub1 = tok.a;
				String sub2 = tok.b;
				String sub3 = tok.c;
				boolean New = true;
				if(!unused(tok))
				{
					New = false;
					System.out.println("SEMERR: Expecting Integer or Real ID. Received " + IDType(tok) + " ID.");
				}
				nextTok();
				if(tok.a.equals("Colon"))
				{
					nextTok();
					if(New)
					{
						IDmarker.add(new SMBL2(sub1,sub2,sub3,"Parameter " + T()));
					}
					else
					{
						T();
					}
					PL1();
				}
				else
				{
					System.out.println("SYNERR: Expecting Colon. Received " + tok.a + ".");
					while(!(tok.a.equals("E")||tok.a.equals("RParen")))
					{
						nextTok();
					}
				}
			}
			else
			{
				System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("RParen")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("E")||tok.a.equals("RParen"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of RParen, Semicolon. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("RParen")))
			{
				nextTok();
			}
		}
	}
	
	public static void CS()
	{
		if(tok.a.equals("begin"))
		{
			nextTok();
			CS1();
		}
		else
		{
			System.out.println("SYNERR: Expecting begin. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Period")||tok.a.equals("Semicolon")||tok.a.equals("else")||tok.a.equals("end")))
			{
				nextTok();
			}
		}
	}
	
	public static void CS1()
	{
		if(tok.a.equals("end"))
		{
			nextTok();
		}
		else if(LexicalAnalyzer.locate(tok.a)||tok.a.equals("begin")||tok.a.equals("if")||tok.a.equals("while")||tok.a.equals("call"))
		{
			SL();
			if(tok.a.equals("end"))
			{
				nextTok();
			}
			else
			{
				System.out.println("SYNERR: Expecting end. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("Period")||tok.a.equals("Semicolon")||tok.a.equals("else")||tok.a.equals("end")))
				{
					nextTok();
				}
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting one of ID, begin, if, while, end, call. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Period")||tok.a.equals("Semicolon")||tok.a.equals("else")||tok.a.equals("end")))
			{
				nextTok();
			}
		}
	}
	
	public static void SL()
	{
		if(LexicalAnalyzer.locate(tok.a)||tok.a.equals("begin")||tok.a.equals("if")||tok.a.equals("while")||tok.a.equals("call"))
		{
			S();
			SL1();
		}
		else
		{
			System.out.println("SYNERR: Expecting one of ID, begin, if, while, call. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("end")))
			{
				nextTok();
			}
		}
	}
	
	public static void SL1()
	{
		if(tok.a.equals("Semicolon"))
		{
			nextTok();
			if(LexicalAnalyzer.locate(tok.a)||tok.a.equals("begin")||tok.a.equals("if")||tok.a.equals("while"))
			{
				S();
				SL1();
			}
			else
			{
				System.out.println("SYNERR: Expecting one of ID, begin, if, while. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("end")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("E")||tok.a.equals("end"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of Semicolon, end. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("end")))
			{
				nextTok();
			}
		}
	}
	
	public static void S()
	{
		if(LexicalAnalyzer.locate(tok.a))
		{
			String tokA = tok.a; String tokB = tok.b; String tokC = tok.c;
			String sub = V();
			if(tok.a.equals("AssignOP"))
			{
				nextTok();
				//String EtokA = tok.a; String EtokB = tok.b; String EtokC = tok.c;
				String sub2 = E();
				if(sub.equals("New Variable"))
				{
					if(!(sub2.equals("ERROR")||sub2.equals("Program")||sub2.equals("Parameter")))
					{
						IDmarker.add(new SMBL2(tokA, tokB, tokC, sub2));
					}
				}
				else if(!sub.equals(sub2) && !sub2.equals("ERROR"))
				{
					System.out.println("SEMERR: Cannot match " + sub + " with " + sub2 + ".");
				}
			}
			else
			{
				System.out.println("SYNERR: Expecting AssignOP. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("begin"))
		{
			CS();
		}
		else if(tok.a.equals("if"))
		{
			nextTok();
			String sub = E();
			if(!sub.equals("Bool"))
			{
				System.out.println("SEMERR: Expecting Bool recieved " + sub + ".");
			}
			if(tok.a.equals("then"))
			{
				nextTok();
				S();
				S1();
			}
			else
			{
				System.out.println("SYNERR: Expecting then. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("while"))
		{
			nextTok();
			String sub = E();
			if(!sub.equals("Bool"))
			{
				System.out.println("SEMERR: Expecting Bool recieved " + sub + ".");
			}
			if(tok.a.equals("do"))
			{
				nextTok();
				S();
			}
			else
			{
				System.out.println("SYNERR: Expecting do. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("call"))
		{
			PS();
		}
		else
		{
			System.out.println("SYNERR: Expecting one of ID, call, while, if, begin. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")))
			{
				nextTok();
			}
		}
	}
	
	public static void S1()
	{
		if(tok.a.equals("else"))
		{
			nextTok();
			S();
		}
		else if(tok.a.equals("E")||tok.a.equals("end"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of else, end. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")))
			{
				nextTok();
			}
		}
	}
	
	public static String V()
	{
		if(!unused(tok))
		{
			String sub = IDType(tok);
			nextTok();
			V1();
			return sub;
		}
		else if(LexicalAnalyzer.locate(tok.a))
		{
			nextTok();
			V1();
			return "New Variable";
		}
		else
		{
			System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("AssignOP")))
			{
				nextTok();
			}
			return "ERROR";
		}
	}
	
	public static void V1()
	{
		if(tok.a.equals("LBrack"))
		{
			nextTok();
			String sub = E();
			if(!sub.equals("INT"))
			{
				System.out.println("SEMERR: Expecting INT recieved " + sub + ".");
			}
			if(tok.a.equals("RBrack"))
			{
				nextTok();
			}
			else
			{
				System.out.println("SYNERR: Expecting RBrack. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("AssignOP")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("E")||tok.a.equals("AssignOP"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of AssignOP, LBrack. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("AssignOP")))
			{
				nextTok();
			}
		}
	}
	
	public static void PS()
	{
		if(tok.a.equals("call"))
		{
			nextTok();
			if(LexicalAnalyzer.locate(tok.a))
			{
				if(!IDType(tok).equals("Procedure"))
				{
					System.out.println("SEMERR: Expecting Procdure ID. Received " + IDType(tok) + " ID.");
				}
				nextTok();
				PS1();
			}
			else
			{
				System.out.println("SYNERR: Expecting ID. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")))
				{
					nextTok();
				}
			}
		}
		else
		{
			System.out.println("SYNERR: Expecting call. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")))
			{
				nextTok();
			}
		}
	}
	
	public static void PS1()
	{
		if(tok.a.equals("LParen"))
		{
			nextTok();
			EL();
			if(tok.a.equals("RParen"))
			{
				nextTok();
			}
			else
			{
				System.out.println("SYNERR: Expecting RParen. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")))
				{
					nextTok();
				}
			}
		}
		else if(tok.a.equals("E")||tok.a.equals("end")||tok.a.equals("else"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of LParen, end, else. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")))
			{
				nextTok();
			}
		}
	}
	
	public static void EL()
	{
		E();
		EL1();
	}
	
	public static void EL1()
	{
		if(tok.a.equals("Comma"))
		{
			nextTok();
			E();
			EL1();
		}
		else if(tok.a.equals("E")||tok.a.equals("RParen"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of Comma, RParen. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("RParen")))
			{
				nextTok();
			}
		}
	}
	
	public static String E()
	{
		String sub = SE();
		String sub2 = E1();
		if(sub2.equals("Bool")) 
		{
			return sub2;
		}
		else
		{
			return sub;
		}
	}
	
	public static String E1()
	{
		if(tok.a.equals("relop"))
		{
			nextTok();
			SE();
			return "Bool";
		}
		else if(tok.a.equals("E")||tok.a.equals("do")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end"))
		{
			return "notBool";
		}
		else
		{
			System.out.println("SYNERR: Expecting one of relop, do, RParen, RBrack, then, else, end. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("Semicolon")||tok.a.equals("end")||tok.a.equals("else")||tok.a.equals("RBrack")||tok.a.equals("RParen")||tok.a.equals("then")||tok.a.equals("do")||tok.a.equals("call")))
			{
				nextTok();
			}
			return "ERROR";
		}
	}
	
	public static String SE()
	{
		if(tok.a.equals("AddOP"))
		{
			Si();
		}
		if(tok.a.equals("NUM")||tok.a.equals("not")||tok.a.equals("LParen")||LexicalAnalyzer.locate(tok.a))
		{
			String sub = Te();
			SE1(sub);
			return sub;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of ID, AddOP, NUM, not, LParen. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("relop")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end")||tok.a.equals("do")||tok.a.equals("call")||tok.a.equals("Semicolon")))
			{
				nextTok();
			}
			return "ERROR";
		}
	}
	
	public static void SE1(String match)
	{
		if(tok.a.equals("AddOP"))
		{
			nextTok();
			String sub = Te();
			if(!sub.equals(match))
			{
				System.out.println("SEMERR: cannot combine " + sub + " and " + match + ".");
			}
			SE1(sub);
			
		}
		else if(tok.a.equals("E")||tok.a.equals("do")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end")||tok.a.equals("relop"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting AddOP, do, RParen, RBrack, then, else, end, relop. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("relop")||tok.a.equals("do")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end")||tok.a.equals("call")||tok.a.equals("Semicolon")))
			{
				nextTok();
			}
		}
	}
	
	public static String Te()
	{
		if(tok.a.equals("NUM")||tok.a.equals("not")||tok.a.equals("LParen")||LexicalAnalyzer.locate(tok.a))
		{
			String sub = F();
			Te1();
			return sub;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of ID, NUM, not, LParen. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("AddOP")||tok.a.equals("do")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end")||tok.a.equals("relop")))
			{
				nextTok();
			}
			return "ERROR";
		}
	}
	
	public static void Te1()
	{
		if(tok.a.equals("MulOP"))
		{
			nextTok();
			Te1();
		}
		else if(tok.a.equals("E")||tok.a.equals("AddOP")||tok.a.equals("do")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end")||tok.a.equals("relop"))
		{
			return;
		}
		else
		{
			System.out.println("SYNERR: Expecting one of MulOP, AddOP, do, RParen, RBrack, then, else, end, relop. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("AddOP")||tok.a.equals("do")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end")||tok.a.equals("relop")))
			{
				nextTok();
			}
		}
	}
	
	public static String F()
	{
		if(tok.a.equals("NUM"))
		{
			String sub = tok.b;
			nextTok();
			return sub;
		}
		else if(LexicalAnalyzer.locate(tok.a))
		{
			String sub = "ERROR";
			if(!unused(tok))
			{
				sub = IDType(tok);
				if(!(sub.equals("array INT")||sub.equals("array REAL")||sub.equals("INT")||sub.equals("REAL")||sub.equals("Bool")))
				{
					System.out.println("SEMERR: Expecting INT, REAL, or Bool. Received " + sub + ".");
				}
			}
			else
			{
				System.out.println("SEMERR: " + tok.c + " is an undefined ID.");
			}
			nextTok();
			if(tok.a.equals("LBrack"))
			{
				nextTok();
				String sub2 = E();
				if(!sub2.equals("INT"))
				{
					System.out.println("SEMERR: Expecting INT recieved " + sub2 + ".");
				}
				if(tok.a.equals("RBrack"))
				{
					nextTok();
				}
				else
				{
					System.out.println("SYNERR: Expecting RBrack. Received " + tok.a + ".");
					while(!(tok.a.equals("E")||tok.a.equals("MulOP")||tok.a.equals("AddOP")||tok.a.equals("do")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end")||tok.a.equals("relop")))
					{
						nextTok();
					}
				}
			}
			return sub;
		}
		else if(tok.a.equals("LParen"))
		{
			nextTok();
			String sub = E();
			if(tok.a.equals("RParen"))
			{
				nextTok();
			}
			else
			{
				System.out.println("SYNERR: Expecting RParen. Received " + tok.a + ".");
				while(!(tok.a.equals("E")||tok.a.equals("MulOP")||tok.a.equals("AddOP")||tok.a.equals("do")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end")||tok.a.equals("relop")))
				{
					nextTok();
				}
				return "ERROR";
			}
			return sub;
		}
		else if(tok.a.equals("not"))
		{
			nextTok();
			String sub = F();

			if(!sub.equals("Bool"))
			{
				System.out.println("SEMERR: Expecting Bool recieved " + sub + ".");
				return "ERROR";
			}
			
			return "Bool";
		}
		else
		{
			System.out.println("SYNERR: Expecting one of NUM, ID, not, LParen. Received " + tok.a + ".");
			while(!(tok.a.equals("E")||tok.a.equals("MulOP")||tok.a.equals("AddOP")||tok.a.equals("do")||tok.a.equals("RParen")||tok.a.equals("RBrack")||tok.a.equals("then")||tok.a.equals("else")||tok.a.equals("end")||tok.a.equals("relop")))
			{
				nextTok();
			}
			return "ERROR";
		}
	}
	
	public static void Si()
	{
		if(tok.b.equals("PLUS")||tok.b.equals("MINUS"))
		{
			nextTok();
		}
		else
		{
			System.out.println("SYNERR: Expecting one of PLUS, MINUS. Received " + tok.b + ".");
			while(!(tok.a.equals("E")||tok.a.equals("NUM")||tok.a.equals("not")||tok.a.equals("LParen")||LexicalAnalyzer.locate(tok.a)))
			{
				nextTok();
			}
		}
	}
}