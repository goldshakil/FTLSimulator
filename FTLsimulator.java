/*
-  Company: VLDB Lab @ Sungkyunkwan University

- Programmer/Developer: Dahab Muhammad Shakeel

- Main Purpose:
1) Implementing the Greedy FTL
2) Creating an optimal garbage collector with lowest possible WAF

- Details:
1) Write Unit is Page (Rgardless of Page's size)
2) Each Block has 8 Pages
3) The Logical and Physical Addresses are the same so the FTL doesn't change the address value
4) Addresses are read from a text file called "Addresses.txt"
5) each address is 8 digits (MS 7 digits for block/ 1 digit for page)
6) All Operations are just write operations since read operations do not trigger key mechanisms in the FTL

*/
import java.io.*;

import java.util.*;

public class FTLsimulator
{
  public static int block_number=10; // change the block_number if you want -> max block_number=9999999 blocks
  public static void main(String[] args)
  {
    //Parse addresses from terminal
    /*Scanner scan=new Scanner(System.in);
    int number_of_addresses=scan.nextInt();
    int counter=0;
    while(counter<number_of_addresses)
    {
    int fullAddress=scan.nextInt();
    //Manipulate the address
  }
  */

  //Parse addresses from Addresses.txt
  try
  {
    File file=new File("Addresses.txt");
    Scanner scan=new Scanner(file);

    int fullAddress=0;
    int counter=0;
    int number_of_addresses=scan.nextInt();
    System.out.println("The addresses to be read "+number_of_addresses);

    //Initialize the Flash Memory with given block_number
    block blocks[]=new block[block_number]; //creating null blocks
    for(int i=0;i<block_number;i++)
    {
      blocks[i]=new block(); //initializing each block to zero
      System.out.printf("block[%d]:\t",i+1);
      blocks[i].printer();
    }
    //







  }catch(Exception e)
  {
    System.out.println("An Error Has Occurred");
  }
}

}
/*
- Block Class Notes:
1) If a page is set to 0 then it is not written to yet otherwise that page has been written to
2)
*/
class block
{
  int pages[]= new int[8]; //each block has 8 pages and each page is set to 1
  void printer()
  {
    for(int i=0;i<8;i++)
    {
      System.out.printf("%d\t",this.pages[i]);
    }
    System.out.println("\n------------------------------------------------------------------------------");
  }
}
