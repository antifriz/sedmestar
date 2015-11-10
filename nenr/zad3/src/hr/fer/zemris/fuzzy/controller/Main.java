package hr.fer.zemris.fuzzy.controller;
import javafx.util.Pair;

import java.io.*;
import java.util.Scanner;

public class Main {


    public static final int RUDDER = 40;
    public static final int AKCEL = 1000;

    public static void main(String[] args) throws IOException {
           BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	    int L=0,D=0,LK=0,DK=0,V=0,S=0,akcel,kormilo;
	    String line = null;
		while(true){
			if((line = input.readLine())!=null){
				if(line.charAt(0)=='K') break;
				Scanner s = new Scanner(line);
				L = s.nextInt();
				D = s.nextInt();
				LK = s.nextInt();
				DK = s.nextInt();
				V = s.nextInt();
				S = s.nextInt();
	        }


            Pair<Integer, Integer> infer = FuzzySystem.infer(L - D, LK - DK, V);

            // fuzzy magic ...
	        akcel = infer.getKey();
            kormilo = infer.getValue();
            //akcel = 100; kormilo = (int) Math.log10(D-L)*100;
	        System.out.println(akcel + " " + kormilo);
	        System.out.flush();
	   }
    }

}

