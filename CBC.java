/**
 * 
 */

/**
 * @author Ananta Bhatt
 *
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CBC
{
    public static int padding;
    public static String plaintext="cryptography is an important tool for network security. but there are other issues for network security.";
   
    public static String key="101101010010100101101011";
    
    public static String[]k=new String[2];
    public static String shuffled_string="";
    public static String tex_data="";
    public static int padding_1;
    //sboxes
static int[][] s1={
            {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
            {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
            {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
            {13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9}
          };

static int[][] s2={
             {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
             {13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
             {10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
             {3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14}
            };
    
  //Binary to Decimal Function
public static int bin2dec (int decimal)
	{
		int rows=0,p=0;
		while(decimal!=0)
		{
			rows+=((decimal%10)*Math.pow(2,p));
			decimal=decimal/10;
		    p++;
		}
		return rows;
		
	}
//Exor function
public static String exor(String b,String key)
{
	   String sstr=b;
    StringBuilder sb = new StringBuilder();
    for(int i=0;i<b.length();i++)
    {
        if((i+1)%2==0)
     	   sstr+=b.charAt(i);
    }
    
    for (int i = 0; i < sstr.length(); i++) 
    {
        sb.append(sstr.charAt(i) ^ key.charAt(i));
    }

    String str= sb.toString();
    return str;
}

//Encryption
    public static List<String> enc(String str)
    {
        
        List<String> blocks = new ArrayList<String>();
        List<String> ciphertemp = new ArrayList<String>();
      
        //Calculating the length of string
        while(str.length()%16!=0)
        {
            str+="0";
            
        }
        
        //Block size 16bitstring
        for(int i=0;i<str.length();i=i+16)
        {
            blocks.add(str.substring(i, i+16));
        }
        
        for(int i=0;i<blocks.size();i++)
        {
        	//Dividing left and right 8bit block
            String L=blocks.get(i).substring(0,8);
            String R=blocks.get(i).substring(8,16);
            for(int j=1;j<=2;j++)
            {
                String prevL=L;
                String r="";
                L=R;
                for(int n=0;n<8;n++)
                {
                	//Call sbox function
                    String round=sbox_function(R,k[j-1]);
                    while(round.length()!=8)
                        round="0"+round;
                    r+=(prevL.charAt(n)^round.charAt(n));
                }
                R=r;
            }
            ciphertemp.add(L+R);
        }
        
        return ciphertemp;
   }
    
    //Sbox function
    public static String sbox_function(String b,String key)
    {
      
        String str=exor(b,key);
        String row1_string="";
        String row2_string="";
        String col1__string="";
        String col2__string="";
        //Dividing into 6 substring
        String b1=str.substring(0, 6);
        String b2=str.substring(6, str.length()); 
        //row decide by charAT(0)& charAt(5)
       row1_string += Character.toString(b1.charAt(0)) + Character.toString(b1.charAt(5));
       row2_string+=Character.toString(b2.charAt(0))+ Character.toString(b2.charAt(5));
       //row decide by charAT(1)& charAt(2)& charAt(3)& charAt(4)& charAt(5)
       col1__string+=Character.toString(b1.charAt(1)) + Character.toString(b1.charAt(2))+ Character.toString(b1.charAt(3))+ Character.toString(b1.charAt(4));
       col2__string+=Character.toString(b2.charAt(1)) + Character.toString(b2.charAt(2))+ Character.toString(b2.charAt(3))+ Character.toString(b2.charAt(4));
            
     //Convert to Integer and then to Decimal Value
        int row1=bin2dec(Integer.parseInt(row1_string));
        int col1=bin2dec(Integer.parseInt(col1__string));
        int row2=bin2dec(Integer.parseInt(row2_string));
        int col2=bin2dec(Integer.parseInt(col2__string));
        
        String s1_output=Integer.toBinaryString(s1[row1][col1]);
        
        //If length of sbox_output is less than 4 
        while(s1_output.length()<4)
            s1_output="0"+s1_output;
        
        String s2_output=Integer.toBinaryString(s2[row2][col2]);
        
        while(s1_output.length()<4)
            s2_output="0"+s2_output;
        
        //Concat the output from sbox1 and sbox2
        String final_output=s1_output+s2_output;        
        return final_output;  
    }
    
    
    
    public static List<String> en(String text)
    {
    //Checking the length of the string
      while(text.length()%16!=0)
      {
    
    	  text+="0";
         
      }   
      String y;     
      List<String> cipher=new ArrayList<String>();
      List<String> s=new ArrayList<String>();
      
      tex_data=CBC.shuffled_string;
      for(int p=0;p<text.length();p=p+16)
      {
       String temp=text.substring(p,p+16);
          
       y="";
     
       
       
       for(int q=0;q<16;q++)
       {
           y+=shuffled_string.charAt(q)^temp.charAt(q);
       }
       s =enc(y);

       shuffled_string=s.toString().substring(1,s.toString().length()-1);
       cipher.add(shuffled_string);
      }
        return cipher;
    }
    CBC()
    {
      	padding_1=0;
        ArrayList<Integer>  list= new ArrayList<Integer>();
        list.add(1);
        list.add(0);
 
        //Creating shuffle list
        for(int i=0;i<16;i++)
        {
            Collections.shuffle(list);
            shuffled_string+=list.get(0);
        }
     }
    
    public static void main(String[] args)
    {
        	
    	    
    	    k[0]=key.substring(0, 12);
            k[1]=key.substring(12,24);
    	    //ENCRYPT THE ORIGINAL PLAIN TEXT
    	    
    	    int[] num_plaintext=problem2.binaryConversion(plaintext);
    	    String tempa="";
    	    String ciphertext=problem2.binaryToStringConversion(tempa);
    	    
    
    	    
    	    String bin_plaintext= problem2.bitConversion(num_plaintext);
    	
 
        
          CBC  cbc = new CBC ();
          System.out.println("The Plain text for this program is:"+plaintext);
        
          System.out.println("\nThe Key for this porgram is:"+key);
               
             tempa="";
          
             num_plaintext=problem2.binaryConversion(plaintext);
             System.out.println("\nThe Z32 value:");
             for(int i=0;i<num_plaintext.length;i++)
             System.out.print(num_plaintext[i]);
            
             bin_plaintext= problem2.bitConversion(num_plaintext);
           System.out.println("\n\nThe Bit Value:"+bin_plaintext);
          
           List<String> ciphertextbin = new ArrayList<String>();
        ciphertextbin=new ArrayList<String>();
        ciphertextbin=CBC .en(bin_plaintext);
        
        for(int i=0;i<ciphertextbin.size();i++)
           tempa+=ciphertextbin.get(i);
        
        System.out.println("\nThe Cipher text value in Binary is :"+tempa);
      
                
        ciphertext=problem2.binaryToStringConversion(tempa);
        System.out.println("\nThe Cipher text Value:"+ciphertext);
   
     

    }
    }