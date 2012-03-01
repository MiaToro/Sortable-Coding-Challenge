package byjson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.util.Vector;
import java.util.Iterator;

public class Match {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
 
 
 	  Vector<String> listings_vec = new Vector<String>(3, 2); // Listings JSON
 	  Vector<String> products_vec = new Vector<String>(3, 2);  // products JSON
 	  Vector<String> output_vec = new Vector<String>(3, 2);  // final output, JSON Array
      Vector<String> subparts_vec = new Vector<String>(3, 2);  // sub-parts of project name
		
	  String file1 = "D:\\workplace\\challenge_data_20110429\\listings.txt";	  
	  String file2 = "D:\\workplace\\challenge_data_20110429\\products.txt";
 
	  listings_vec = ReadFileToString(file1);	  
      products_vec = ReadFileToString(file2);
 
	  
	// use an iterator to display contents 
	  
	  Iterator<String> listings_itr = listings_vec.iterator();   
	  Iterator<String> products_itr = products_vec.iterator(); 
//	  products_vec.clear();
	  Iterator<String> subparts_itr = subparts_vec.iterator();

	  
 	  
     // check listings by each project
 	  while(products_itr.hasNext()) {

		 String temp_str_product = "";   // Json for each product  
		 String temp_str_product_manu = "";  // the manufacturer of product
		 String temp_str_product_name = "";  // the string between "product_name:" and ","
		 String temp_str_product_subname = "";  // the rest string of temp_str_product_name removed the manufacturer name of product

		 
		 String temp_str_listing = "";	  // Json of each listing  	 
		 String temp_str_listing_manu = "";   // the manufacturer of each listing
		 String temp_str_listing_title = "";   // the title of each listing
		 
		 String temp_str_all = "";  // output Array for each product at the end
		 
		 String[] get_element_rest = {"", ""};  
		 
		 temp_str_product = products_itr.next();
 
		 get_element_rest = getOneElementAndRestPart("\"manufacturer\":\"", "\",", temp_str_product);  // get the string of manufacturer of product
		 temp_str_product_manu =  get_element_rest[0].trim();
 		 
		 get_element_rest = getOneElementAndRestPart("\"product_name\":\"", "\",", temp_str_product);  // get the string of product_name of product
		 temp_str_product_name =  get_element_rest[0].trim();
 
		 temp_str_product_subname = removeFirstPart(temp_str_product_manu, temp_str_product_name);  // the rest string of temp_str_product_name removed the manufacturer name of product
 	 		 
		 // if the rest string isn't empty, replace all "_" with "-", and put each substring between "-"s in subparts_vec.
		 if(temp_str_product_subname.length() >= 1) {
 
			 temp_str_product_subname = temp_str_product_subname.replaceAll("_", "-");
 	         get_element_rest[1] = temp_str_product_subname;
 	         
 	         subparts_vec.clear();
	         
	         while(get_element_rest[1].indexOf("-") >= 0) {
	        	 
	        	 get_element_rest = getBeginningAndRestPart("-", get_element_rest[1]);  // manufacturer
	        	 subparts_vec.addElement(get_element_rest[0].trim());
 	        	 
 	         }
	         subparts_vec.addElement(get_element_rest[1].trim());
	         subparts_itr = subparts_vec.iterator();	         
		 }		 
  
		 listings_itr = listings_vec.iterator();
		 
		 temp_str_all = "";
		 // check if the string of listing name includes those sub parts of this product name
		 while(listings_itr.hasNext()) {
			 
			  temp_str_listing = listings_itr.next();

			  get_element_rest = getOneElementAndRestPart("\"manufacturer\":\"", "\",", temp_str_listing);  // the manufacturer name

			  temp_str_listing_manu = get_element_rest[0].trim();
 
			  get_element_rest = getOneElementAndRestPart("title\":\"", "\",", temp_str_listing); // the title
			  temp_str_listing_title = get_element_rest[0].trim();
			  
			  // remove the string behind " for " or " with " of the title
			  
			  if(temp_str_listing_title.indexOf(" for ") >= 0)
			    get_element_rest = getBeginningAndRestPart(" for ", temp_str_listing_title);
			  if(temp_str_listing_title.indexOf(" with ") >= 0)
				get_element_rest = getBeginningAndRestPart(" with ", temp_str_listing_title);
			  
			  temp_str_listing_title = get_element_rest[0];
			  
			  if(temp_str_listing_manu.indexOf(temp_str_product_manu) >=0 ) { // if the manufacturers are the same
				  
				int count_s = 0;  // how many sub parts of the product name is included in the title
				
				while(subparts_itr.hasNext()) { 
				  String temp_sub = subparts_itr.next();	
				  int listing_index = temp_str_listing_title.indexOf(temp_sub);	
				  if( listing_index == -1) break; // if this sub part isn't included, this listing is given up
				  else { 
					if(temp_str_listing_title.indexOf(temp_sub + " ") >= 0) {  // if it's a space behind, it's ok
				      count_s = count_s + 1;  
					}  else { // if not a space behind, if the end of this sub part and the character following it are both numbers, this won't be counted 
						
					  String temp_sub_sub = temp_str_listing_title.substring(listing_index + temp_sub.length()); 
					  String temp_sub_end = temp_sub.substring(temp_sub.length()-1);
					  
					  if (temp_sub_sub.length() >= 1) {
						String temp_s =  temp_sub_sub.substring(0, 1);
 
					    if(temp_sub_end.equals("0")||temp_sub_end.equals("1")||temp_sub_end.equals("2")||temp_sub_end.equals("3")||temp_sub_end.equals("4")||temp_sub_end.equals("5")||temp_sub_end.equals("6")||temp_sub_end.equals("7")||temp_sub_end.equals("8")||temp_sub_end.equals("9")) {	
					      if(temp_s.equals("0")||temp_s.equals("1")||temp_s.equals("2")||temp_s.equals("3")||temp_s.equals("4")||temp_s.equals("5")||temp_s.equals("6")||temp_s.equals("7")||temp_s.equals("8")||temp_s.equals("9")) {
					        break;  			      
					      }  else  count_s = count_s + 1;
					    }  
					  }	
					  else	
					    count_s = count_s + 1; 	
					  
					}  
				  }  
				}
				
				// if all the sub parts of the name are included in the title
				if(count_s == subparts_vec.size() && count_s != 0) 
				  temp_str_all = temp_str_all  + temp_str_listing + ",\n";	
			  }  
		  }
		 
		 // build output array
		 if(temp_str_all.length() > 0)  {
			 temp_str_all = temp_str_all.trim();
			 temp_str_all = temp_str_all.substring(0, temp_str_all.length()-1) ;
		     output_vec.addElement( "\n{\n\"product_name:\" " + temp_str_product_name + "\n\"listings\": [ " + temp_str_all + " ]\n}");
		 }  	 
 
	  }
 
	  
	  Iterator<String> output_vec_itr = output_vec.iterator(); 	
//	  output_vec.clear();
 	  while(output_vec_itr.hasNext()) 
 		    System.out.print(output_vec_itr.next() + "\n"); 
 
	} 
	
	public static Vector<String> ReadFileToString(String InputFile) {
		
	 	 Vector<String> element_vec  = new Vector<String>(3, 2);
 
	     try {
	     
	    	FileInputStream fstream = new FileInputStream(InputFile);
	        // Get the object of DataInputStream
	        DataInputStream in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	 
	        //Read File Line By Line    
 
	        String temp_line = "";
	        while ((temp_line = br.readLine()) != null) { //loop through each line  
              element_vec.addElement(temp_line)	;
 	        }
	        in.close();//Close the input stream
	     
	     } catch (Exception e)  {//Catch exception if any
	        
	    	 System.out.println("Error: " + e.getMessage());  
	     }
	     return element_vec;
	     
	    }
	
    public static String[] getOneElementAndRestPart(String search_str_1, String search_str_2, String content) {

        String[] get_link_rest = {"", ""};
 
        int link_1 = content.indexOf(search_str_1);
         
        if (link_1 < 0) {
        	
        	link_1 = 0;
        	
        } else {

          content = content.substring(link_1 + search_str_1.length()); 
 
          int link_2 = content.indexOf(search_str_2);
        
          if (link_2 >= 0) {  	  
            get_link_rest[0] = content.substring(0, link_2);
            get_link_rest[1] = content.substring(link_2 + search_str_2.length());
          }
        
        }

        return get_link_rest;
    }
    
    public static String removeFirstPart(String search_str_1, String content) {
 
    	int link_1 = content.indexOf(search_str_1);
        
        if (link_1 < 0) {
        	
        	link_1 = 0;
        	
        } else {

            content = content.substring(link_1 + search_str_1.length() + 1); 
          }

        return content;
    }

    public static String[] getBeginningAndRestPart(String search_str_1, String content) {

        String[] get_link_rest = {"", ""};
          
        int link_1 = content.indexOf(search_str_1);
        
        if (link_1 >= 0) {  	  

            get_link_rest[0] = content.substring(0, link_1);

            get_link_rest[1] = content.substring(link_1 + search_str_1.length());
          }
 
        return get_link_rest;
    }

}
