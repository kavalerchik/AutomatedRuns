package sandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class basicJavaConcepts {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// create HashMap
		HashMap<Integer, List<String>> hmap = new HashMap<Integer, List<String>>();
				
		List<String> list1 = new ArrayList<String>();
		list1.add(0,"123");
		list1.add(1,"sda");
		list1.add(2,"33");
		list1.add(3,"last error");
		
		
		hmap.put(0,list1); // WONT be printed
		hmap.put(1, list1);
		
		
		
		
		// Println HashMap
		//Set set = hmap.entrySet(); // SET ==> a Collection that cannot contain duplicate elements. 
		Iterator iterator = list1.iterator(); // Iterator ==> to cycle through the elements in a collection
		while (iterator.hasNext()){
		//	Map.Entry mEntry = (Map.Entry)iterator.next();	// MAP ==>  The keys are unique and thus, no duplicate keys are allowed.	
			String element = (String) iterator.next();
			System.out.println("key is: "+element);
			//System.out.println("key is: "+mEntry.getKey()+" & value is: "+mEntry.getValue());
		}
		
	}

}
