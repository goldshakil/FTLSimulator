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
7) Assumption: The number of addresses is smaller than the number of total number of pages
8) Assumption: Garabge collection only occurs when the used blocks are full
*/

import java.io.*;

import java.util.*;

public class FTLsimulator
{

  public static int full_number_of_blocks=4; // half of the blocks are used for over provisioing
  public static int used_blocks=full_number_of_blocks/2; // change the used_blocks if you want -> 10% change
  public static int total_pages=8;
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

    //Initialize the Flash Memory with given used_blocks
    block blocks[]=new block[full_number_of_blocks]; //creating null blocks
    for(int i=0;i<full_number_of_blocks;i++)
    {
      blocks[i]=new block(); //initializing each block to zero
      if(i+1>(full_number_of_blocks/2))
      {
        blocks[i].set_over_provisioning(1);
      }
      else
      {
        blocks[i].set_over_provisioning(0);
      }
      System.out.printf("block[%d] ",i+1);
      if(blocks[i].over_provisioning==1)System.out.printf("|O|:\t");
      else System.out.printf("|U|:\t"); //used
      blocks[i].printer();
    }

    //L2Ptable: Logical Address | Physical Block Number | Physical Page Number
    //table can't be larger than the maximum number of addresses
    int L2Ptable[][]=new int [number_of_addresses][3];

    //Read Each Address and apply greedyFTL
    for(int i=0;i<number_of_addresses;i++)
    {
      int address_read=scan.nextInt();

      if(old_address(address_read,L2Ptable))
      {
        //Mark the orginal page location as stale
        for(int k=0;k<L2Ptable.length;k++)
        {
          if(L2Ptable[k][0]==address_read)//empty entry in the table
          {
            set_page_stale(L2Ptable[k][1],L2Ptable[k][2],blocks);
            break;
          }
        }

        //Place in a new position -> write to a new page and new block -> extreme wear leveling
        int possible_block=find_possible_block(blocks,full_number_of_blocks);
        if(!place_page(blocks,possible_block,L2Ptable,address_read))
        {
          //Garabge Collection

        }


      }

      else //new address
      {
        for(int k=0;k<L2Ptable.length;k++)
        {
          if(L2Ptable[k][0]==0)//empty entry in the table
          {
            L2Ptable[k][0]=address_read;
            break;
          }
        }

        int possible_block=find_possible_block(blocks,full_number_of_blocks);

        if(!place_page(blocks,possible_block,L2Ptable,address_read))
        {
          //Garabge Collection
          garbage_collection(blocks,full_number_of_blocks,L2Ptable);
          //Place Page

        }
      }

      System.out.println("\n\n");
      //Printing the blocks
      for(int y=0;y<full_number_of_blocks;y++)
      {
        System.out.printf("block[%d] ",y+1);
        if(blocks[y].over_provisioning==1)System.out.printf("|O|:\t");
        else System.out.printf("|U|:\t"); //used
        blocks[y].printer();
      }
      //Printing the L2Ptable
      for(int o=0;o<L2Ptable.length;o++)
      {
        if(L2Ptable[o][0]!=0) System.out.printf("%d\t%d\t%d\n",L2Ptable[o][0],L2Ptable[o][1],L2Ptable[o][2]);
      }
    }


  }catch(Exception e)
  {
    System.out.println("An Error Has Occurred");
    e.printStackTrace();
  }
}

static int find_corresponding_address(int victim_replaced_block,int copied_page, int L2Ptable[][])
{
  

}

/*
This function does the following:
1) Choose Victim Based on Greedy Policy -> block with highest number of stale pages -> THIS CAN BE FIXED FOR ANOTHER POLICY
2) Copy valid data to a an over_provisioning block with least number of writes -> No wear levelling for now
3) Change over_provisioning status
4) Erase original block
5) Update L2P table
*/
static void garbage_collection(block blocks[],int full_number_of_blocks,int L2Ptable[][])
{
  //Step 1:
  //counter_array[][]: Used and Stale Pages | Used(1) or OverProvisioing(0)
  int counter_array[][]=new int[full_number_of_blocks][2];
  for(int i=0;i<full_number_of_blocks;i++)
  {
    counter_array[i][1]=blocks[i].over_provisioning;
  }

  for(int i=0;i<full_number_of_blocks;i++)
  {
    for(int j=0;j<total_pages;j++) //each block has 8 pages
    {
      if(blocks[i].pages[j]==2)
      {
        counter_array[i][0]++;
      }
    }
  }

  int victim_replaced_block=0;
  for(int i=0;i<counter_array.length;i++)
  {
    if(counter_array[i][0]>counter_array[victim_replaced_block][0]&&counter_array[i][1]==0)
    {
      victim_replaced_block=i;
    }
  }

  //Step2:
  int new_replacing_block=0;
  for(int i=0;i<full_number_of_blocks;i++)
  {
    if(blocks[i].over_provisioning== 1)
    {
      new_replacing_block=i;
      break;
    }
  }
  for(int i=0;i<total_pages;i++)
  {
    if(blocks[victim_replaced_block].pages[i]==1)//valid data
    {
      int LogicalAddress=find_corresponding_address(victim_replaced_block,i,L2Ptable);

    }
  }






}

/*
This function set the page to stale state (dirty)
*/
static void set_page_stale(int block_number,int page_number,block blocks[])
{
  blocks[block_number].pages[page_number]=2;
}


/*
This function checks wether the address has been accesses before or not:
1) true -> OLD (Accessed Before)
2) False -> NEW (Wasn't accessed before)
*/
static boolean old_address(int address_read, int L2Ptable[][])
{
  boolean return_value=false;
  for(int i=0;i<L2Ptable.length;i++)
  {
    if(address_read==L2Ptable[i][0])
    {
      return_value=true;
      break;
    }
  }
  return return_value;
}


/*
This function finds the best block (index) to place the new written page -> Wear Leveling
Note: the best means the block with least number number of writes to it
*/
static int find_possible_block(block blocks[], int full_number_of_blocks)
{
  int lowest_index=0;
  for(int i=0;i<full_number_of_blocks;i++)
  {
    if(blocks[i].number_writes<blocks[lowest_index].number_writes&&blocks[i].over_provisioning==0)
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
3) determine GC need: if the best block has full pages
*/
static boolean place_page(block blocks[], int possible_block,int L2Ptable[][],int address_read)
{
  boolean return_value=false;

  int i=0;
  for( i=0;i<total_pages;i++) // check each page in the block
  {

    if(blocks[possible_block].pages[i]==0&&blocks[possible_block].over_provisioning==0) //find an empty page
    {
      blocks[possible_block].pages[i]=1;
      blocks[possible_block].number_writes++;
      return_value=true;
      break;
    }
  }
  //in L2P Table: find address index and update its block by possible block and its page by i
  if(return_value==true)
  {
    for(int k=0;k<L2Ptable.length;k++)
    {
      if(L2Ptable[k][0]==address_read) //found the address entry
      {
        L2Ptable[k][1]=possible_block;
        L2Ptable[k][2]=i;
        break;
      }
    }
  }
  return return_value;
}


} // End of FTL class


/*
- Block Class Notes:
1) If a page is set to 0 then it is not written to yet otherwise that page has been written to
2)
*/
class block
{
  int number_writes; //keeps track of how many writes have been committed to the block
  int over_provisioning; // 0 block in use  1 over provisioing block
  int pages[]= new int[8]; //each block has 8 pages and each page has a bit: 0)free 1)used 2)stale(invalid)

  void set_over_provisioning(int value)
  {
    this.over_provisioning=value;
  }

  void printer()
  {

    for(int i=0;i<8;i++)
    {
      System.out.printf("%d\t",this.pages[i]);
    }
    System.out.println("\n------------------------------------------------------------------------------");
  }
}
