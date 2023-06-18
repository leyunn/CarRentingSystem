package com.example.demo.service;

import java.io.IOException;
import java.util.Random;
import java.util.Random.*;
/**
 * 
 */
/**
 * @author allem40306
 *
 */
public class distance{
	public static Double[] generate(Double lat, Double lng) throws IOException{
		Double nextLat, nextLng;
		do
		{
			nextLat = lat + (Math.random() - 0.5) * 0.001;
			nextLng = lng + (Math.random() - 0.5) * 0.001;
		}while(nextLat < 25.026708 || nextLat > 25.068277 || nextLng < 121.511162 || nextLng > 121.567045);
		return new Double[]{nextLat, nextLng};
	}
	
	public static void main(String[] args) throws IOException{
		Double[] arr= generate(25.04, 121.511162);
		System.out.println(arr[0]);
		System.out.println(arr[1]);
	}
}