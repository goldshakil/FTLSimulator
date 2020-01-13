/*
-  Company: VLDB Lab @ Sungkyunkwan University

- Programmer/Developer: Dahab Muhammad Shakeel

- Main Purpose:
1) Implementing the Greedy FTL
2) Creating an optimal garbage collector with lowest possible WAF

- Details:
1) Write Unit is Page (Rgardless of Page's size)
2) Each Block has 8 Pages
3) The FTLsimulator translates a logical address read from the text file and translates it to a physical one (Wear Leveling)
4) Addresses are read from a text file called "Addresses.txt"
5) All Operations are just write operations since read operations do not trigger key mechanisms in the FTL
6) Assumption: L2P table size is small (same size as the addresses to be read)
*/

import java.io.*;

import java.util.*;

public class FTLsimulator
{
  public static int total_blocks=10; // change the total_blocks if you want -> max total_blocks=9999999 blocks
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

    int number_of_addresses=scan.nextInt();
    System.out.println("The addresses to be read "+number_of_addresses);

    //Initialize the Flash Memory with given total_blocks
    block blocks[]=new block[total_blocks]; //creating null blocks
    for(int i=0;i<total_blocks;i++)
    {
      blocks[i]=new block(); //initializing each block to zero
      System.out.printf("block[%d]:\t",i+1);
      blocks[i].printer();
    }

    //Creating an L2P table (the same size as given addresses)
    int L2Ptable[][]=new int [number_of_addresses][3]; // Logical Address | Physical Block Number | Physical Page Number
    //Read Each Access and Apply Wear Leveling to spread the data on the ssd equally
    for(int i=0;i<number_of_addresses;i++)
    {
      int address_read=scan.nextInt();
      //if(old_address())
      //Wear Leveling for new addresses:
      int possible_block=find_possible_block(blocks,total_blocks);
      System.out.printf("%d",possible_block);
      place_page(blocks,possible_block);


      //update the L2P
    }









  }catch(Exception e)
  {
    System.out.println("An Error Has Occurred");
    e.printStackTrace();
  }
}

/*
This function finds the best block (index) to place the new written page
*/
static int find_possible_block(block blocks[], int total_blocks)
{
  //scan all blocks' pages and return the block with least number of stale and used pages
  int counter_array[]=new int[total_blocks];

  for(int i=0;i<total_blocks;i++)
  {
    for(int j=0;j<8;j++) //each block has 8 pages
    {
      if(blocks[i].pages[j]!=0)
      {
        counter_array[i]++;
      }
    }
  }

  int lowest_index=0;
  for(int i=0;i<counter_array.length;i++)
  {
    if(counter_array[i]<counter_array[lowest_index])
    {
      lowest_index=i;
    }
  }
  return lowest_index;
}

/*
This function does the following:
1)place the page and set its data to 1
2) update the L2P table
*/
static void place_page(block blocks[], int possible_block)
{

}

}


/*
- Block Class Notes:
1) If a page is set to 0 then it is not written to yet otherwise that page has been written to
2)
*/
class block
{
  int pages[]= new int[8]; //each block has 8 pages and each page has a bit: 0)free 1)used 2)stale(invalid)
  void printer()
  {
    for(int i=0;i<8;i++)
    {
      System.out.printf("%d\t",this.pages[i]);
    }
    System.out.println("\n------------------------------------------------------------------------------");
  }
}
