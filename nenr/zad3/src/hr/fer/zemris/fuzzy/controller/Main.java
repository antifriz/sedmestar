package hr.fer.zemris.fuzzy.controller;
import hr.fer.zemris.fuzzy.controller.inference.AkcelFuzzySystemMin;
import hr.fer.zemris.fuzzy.controller.inference.FuzzySystem;
import hr.fer.zemris.fuzzy.controller.inference.RudderFuzzySystemMin;

import java.io.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException {
           BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	    int L=0,D=0,LK=0,DK=0,V=0,S=0,akcel,kormilo;
	    String line;

		Defuzzifier defuzzifier = new COADefuzzifier();
		FuzzySystem fsAkcel = new AkcelFuzzySystemMin(defuzzifier);
		FuzzySystem fsRudder = new RudderFuzzySystemMin(defuzzifier);

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

	        akcel = fsAkcel.infer(L,D,LK,DK,V,S);
            kormilo = fsRudder.infer(L,D,LK,DK,V,S);

	        System.out.println(akcel + " " + kormilo);
	        System.out.flush();
	   }
    }

}

